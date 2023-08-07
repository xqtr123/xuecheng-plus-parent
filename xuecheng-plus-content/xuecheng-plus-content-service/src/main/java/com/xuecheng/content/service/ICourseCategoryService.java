package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryDto;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author RShL
 * @since 2023-08-07
 */
public interface ICourseCategoryService {

    List<CourseCategoryDto> queryTreeNodes(String id);
}
