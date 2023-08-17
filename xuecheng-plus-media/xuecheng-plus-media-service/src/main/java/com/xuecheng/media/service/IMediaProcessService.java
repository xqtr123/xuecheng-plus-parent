package com.xuecheng.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.media.model.po.MediaProcess;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author RShL
 * @since 2023-08-16
 */
public interface IMediaProcessService extends IService<MediaProcess> {

    /**
     * 获取待处理任务
     * @param shardIndex    分片序号
     * @param shardTotal    分片总数
     * @param count         获取记录数
     * @return  待处理任务集合
     */
    List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count);

    void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);
}
