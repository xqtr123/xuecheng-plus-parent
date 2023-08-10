package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanPO;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author RShL
 * @since 2023-08-09
 */
public interface ITeachplanService extends IService<TeachplanPO> {

    List<TeachplanDto> getTreeNodes(Long courseId);

    void saveTeachplan(TeachplanPO teachplan);

    void deleteTeachplan(Long teachplanId);

    void orderByTeachplan(String moveType, Long teachplanId);
}
