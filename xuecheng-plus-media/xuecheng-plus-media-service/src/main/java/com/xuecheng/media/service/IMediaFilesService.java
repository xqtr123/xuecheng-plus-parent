package com.xuecheng.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFilesPO;

import java.io.File;

/**
 * <p>
 * 媒资信息 服务类
 * </p>
 *
 * @author RShL
 * @since 2023-08-15
 */
public interface IMediaFilesService extends IService<MediaFilesPO> {

    UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName);

    MediaFilesPO addMediaFilesToDb(Long companyId, UploadFileParamsDto uploadFileParamsDto, String objectName, String fileMD5, String bucket);

    void addMediaFilesToMinIO(String filePath, String bucket, String objectName);

    boolean checkFile(String fileMd5);

    boolean checkChunk(String fileMd5, int chunk);

    RestResponse uploadChunk(byte[] bytes, String fileMd5, int chunk);

    RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto);

    MediaFilesPO getFileById(String mediaId);

    String getFilePathByMd5(String fileMd5, String extension);

    File downloadFileFromMinio(File file, String bucket, String objectName);
}
