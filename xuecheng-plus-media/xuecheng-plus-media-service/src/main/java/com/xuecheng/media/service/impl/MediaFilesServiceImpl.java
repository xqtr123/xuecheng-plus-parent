package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFilesPO;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.IMediaFilesService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static com.xuecheng.base.util.FileUtil.getContentType;

/**
 * <p>
 * 媒资信息 服务实现类
 * </p>
 *
 * @author RShL
 * @since 2023-08-15
 */
@Service
@Slf4j
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFilesPO> implements IMediaFilesService {

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private MediaProcessMapper mediaProcessMapper;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.files}")
    private String bucket_files;

    @Value("${minio.bucket.video}")
    private String video_files;

    @Autowired
    IMediaFilesService currentProxy;

    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName) {
        String fileMD5 = DigestUtils.md5DigestAsHex(bytes);
        if (StringUtils.isEmpty(folder)) {
            // 如果目录不存在，则自动生成一个目录
            folder = getFileFolder(true, true, true);
        } else if (!folder.endsWith("/")) {
            // 如果目录末尾没有 / ，替他加一个
            folder = folder + "/";
        }
        if (StringUtils.isEmpty(objectName)) {
            // 如果文件名为空，则设置其默认文件名为文件的md5码 + 文件后缀名
            String filename = uploadFileParamsDto.getFilename();
            objectName = fileMD5 + filename.substring(filename.lastIndexOf("."));
        }
        objectName = folder + objectName;
        try {
            addMediaFilesToMinio(bytes, bucket_files, objectName);
            MediaFilesPO mediaFiles = currentProxy.addMediaFilesToDb(companyId, uploadFileParamsDto, objectName, fileMD5, bucket_files);
            UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
            BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
            return uploadFileResultDto;
        } catch (Exception e) {
            XueChengPlusException.cast("上传过程中出错");
        }
        return null;
    }

    private void addMediaFilesToMinio(byte[] bytes, String bucket, String objectName) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        String contentType = getContentType(objectName);
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            log.debug("上传到文件系统出错:{}", e.getMessage());
            throw new XueChengPlusException("上传到文件系统出错");
        }
    }

    @NotNull
    public MediaFilesPO addMediaFilesToDb(Long companyId, UploadFileParamsDto uploadFileParamsDto, String objectName, String fileMD5, String bucket) {
        // 根据文件名获取Content-Type
        String contentType = getContentType(objectName);
        // 保存到数据库
        MediaFilesPO mediaFiles = mediaFilesMapper.selectById(fileMD5);
        if (mediaFiles == null) {
            mediaFiles = new MediaFilesPO();
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            mediaFiles.setId(fileMD5);
            mediaFiles.setFileId(fileMD5);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setBucket(bucket);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setStatus("1");
            mediaFiles.setFilePath(objectName);
           // 如果是图片格式或者mp4格式，则设置URL属性，否则不设置
           if (contentType.contains("image") || contentType.contains("mp4")) {
               mediaFiles.setUrl("/" + bucket + "/" + objectName);
           }
            // 查阅数据字典，002003表示审核通过
            mediaFiles.setAuditStatus("002003");
        }
        int insert = mediaFilesMapper.insert(mediaFiles);
        if (insert <= 0) {
            XueChengPlusException.cast("保存文件信息失败");
        }
        if ("video/x-msvideo".equals(contentType)) {
           MediaProcess mediaProcess = new MediaProcess();
           BeanUtils.copyProperties(mediaFiles, mediaProcess);
           mediaProcess.setStatus("1"); // 未处理
           int processInsert = mediaProcessMapper.insert(mediaProcess);
           if (processInsert <= 0) {
               XueChengPlusException.cast("保存avi视频到待处理表失败");
           }
       }
        return mediaFiles;
    }

    @Override
    public boolean checkFile(String fileMd5) {
        MediaFilesPO mediaFilesPO = mediaFilesMapper.selectById(fileMd5);
        if(mediaFilesPO == null){
            return false;
        }
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(mediaFilesPO.getBucket())
                    .object(mediaFilesPO.getFilePath())
                    .build());
            if(inputStream == null){
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkChunk(String fileMd5, int chunkIndex) {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        String chunkFilePath = chunkFileFolderPath + chunkIndex;
        try {
            // 判断分块是否存在
            InputStream inputStream = minioClient.getObject(GetObjectArgs
                    .builder()
                    .bucket(video_files)
                    .object(chunkFilePath)
                    .build());
            // 不存在返回false
            if (inputStream == null) {
                return false;
            }
        } catch (Exception e) {
            // 出异常也返回false
            return false;
        }
        // 否则返回true
        return true;
    }

    @Override
    public RestResponse uploadChunk(byte[] bytes, String fileMd5, int chunk) {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        try {
            addMediaFilesToMinio(bytes, video_files, chunkFileFolderPath);
            return RestResponse.success(true);
        } catch (Exception e) {
            log.debug("上传分块文件：{}失败：{}", chunkFileFolderPath, e.getMessage());
        }
        return RestResponse.validfail("上传文件失败", false);
    }

    @Override
    public RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
//        检查分块文件是否存在
        File[] files = checkChunkStatus(fileMd5, chunkTotal);
        String filename = uploadFileParamsDto.getFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        File mergeFile = null;
        try {
            mergeFile = File.createTempFile(filename, extension);
        } catch (IOException e) {
            XueChengPlusException.cast("创建临时合并文件出错");
        }
        try {
            byte[] buffer = new byte[1024];
            try(RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw")){
                for(File file : files){
                    try(RandomAccessFile raf_read = new RandomAccessFile(file, "r")){
                        int len;
                        while((len = raf_read.read(buffer)) != -1){
                            raf_write.write(buffer, 0, len);
                        }
                    }
                }
            }catch (Exception e){
                XueChengPlusException.cast("合并文件过程出错");
            }
            uploadFileParamsDto.setFileSize(mergeFile.length());
            try (FileInputStream fileInputStream = new FileInputStream(mergeFile)) {
                String mergemd5 = DigestUtils.md5DigestAsHex(fileInputStream);
                if(!fileMd5.equals(mergemd5)){
                    XueChengPlusException.cast("合并文件校验失败");
                }
                log.debug("合并文件校验通过：{}", mergeFile.getAbsolutePath());
            }catch (Exception e){
                XueChengPlusException.cast("合并文件校验异常");
            }
            String mergeFilePath = getFilePathByMd5(fileMd5, extension);
            // 将本地合并好的文件，上传到minio中，这里重载了一个方法
            addMediaFilesToMinIO(mergeFile.getAbsolutePath(), video_files, mergeFilePath);
            log.debug("合并文件上传至MinIO完成{}", mergeFile.getAbsolutePath());
            // 将文件信息写入数据库
            MediaFilesPO mediaFiles = addMediaFilesToDb(companyId, uploadFileParamsDto, mergeFilePath, fileMd5, video_files);
            if (mediaFiles == null) {
                XueChengPlusException.cast("媒资文件入库出错");
            }
            log.debug("媒资文件入库完成");
            return RestResponse.success();
        } finally {
            for (File chunkFile : files) {
                try {
                    chunkFile.delete();
                } catch (Exception e) {
                    log.debug("临时分块文件删除错误：{}", e.getMessage());
                }
            }
            try {
                mergeFile.delete();
            } catch (Exception e) {
                log.debug("临时合并文件删除错误：{}", e.getMessage());
            }
        }
    }

    @Override
    public MediaFilesPO getFileById(String mediaId) {
        MediaFilesPO mediaFilesPO = mediaFilesMapper.selectById(mediaId);
        if (mediaFilesPO == null || StringUtils.isEmpty(mediaFilesPO.getUrl())) {
            XueChengPlusException.cast("视频还没有转码处理");
        }
        return mediaFilesPO;
    }

    @Override
    public void addMediaFilesToMinIO(String filePath, String bucket, String objectName) {
        String contentType = getContentType(objectName);
        try {
            minioClient.uploadObject(UploadObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(objectName)
                    .filename(filePath)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            XueChengPlusException.cast("上传到文件系统出错");
        }
    }

    @Override
    public String getFilePathByMd5(String fileMd5, String extension) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + extension;
    }

    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "chunk" + "/";
    }

    private String getFileFolder(boolean year, boolean month, boolean day) {
        StringBuilder stringBuffer = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(new Date());
        String[] split = dateString.split("-");
        if (year) {
            stringBuffer.append(split[0]).append("/");
        }
        if (month) {
            stringBuffer.append(split[1]).append("/");
        }
        if (day) {
            stringBuffer.append(split[2]).append("/");
        }
        return stringBuffer.toString();
    }

    /**
     * 下载分块文件
     * @param fileMd5       文件的MD5
     * @param chunkTotal    总块数
     * @return 分块文件数组
     */
    private File[] checkChunkStatus(String fileMd5, int chunkTotal) {
        // 作为结果返回
        File[] files = new File[chunkTotal];
        // 获取分块文件目录
        String chunkFileFolder = getChunkFileFolderPath(fileMd5);
        for (int i = 0; i < chunkTotal; i++) {
            // 获取分块文件路径
            String chunkFilePath = chunkFileFolder + i;
            File chunkFile = null;
            try {
                // 创建临时的分块文件
                chunkFile = File.createTempFile("chunk" + i, null);
            } catch (Exception e) {
                XueChengPlusException.cast("创建临时分块文件出错：" + e.getMessage());
            }
            // 下载分块文件
            chunkFile = downloadFileFromMinio(chunkFile, video_files, chunkFilePath);
            // 组成结果
            files[i] = chunkFile;
        }
        return files;
    }

    /**
     * 从Minio中下载文件
     * @param file          目标文件
     * @param bucket        桶
     * @param objectName    桶内文件路径
     * @return
     */
    @Override
    public File downloadFileFromMinio(File file, String bucket, String objectName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             InputStream inputStream = minioClient.getObject(GetObjectArgs
                     .builder()
                     .bucket(bucket)
                     .object(objectName)
                     .build())) {
            IOUtils.copy(inputStream, fileOutputStream);
            return file;
        } catch (Exception e) {
            XueChengPlusException.cast("查询文件分块出错");
        }
        return null;
    }
}
