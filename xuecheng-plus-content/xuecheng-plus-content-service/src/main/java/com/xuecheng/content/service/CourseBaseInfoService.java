package com.xuecheng.content.service;


import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
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
}