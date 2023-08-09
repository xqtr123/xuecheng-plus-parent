package com.xuecheng.content.service;


import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamDto;
import com.xuecheng.content.model.po.CourseBasePO;

public interface CourseBaseInfoService {

    /**
     * 课程查询接口
     * @param pageParams 分页参数
     * @param queryCourseParamDto 查询条件
     * @return
     */
    PageResult<CourseBasePO> queryCourseBaseList(PageParams pageParams, QueryCourseParamDto queryCourseParamDto);

    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    CourseBaseInfoDto getCourseBaseInfo(Long courseBaseId);

    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto);
}
