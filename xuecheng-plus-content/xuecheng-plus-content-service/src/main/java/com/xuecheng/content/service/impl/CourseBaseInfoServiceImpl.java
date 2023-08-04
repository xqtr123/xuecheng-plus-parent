package com.xuecheng.content.service.impl;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamDto;
import com.xuecheng.content.model.po.CourseBasePO;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.springframework.stereotype.Service;

@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Override
    public PageResult<CourseBasePO> queryCourseBaseList(PageParams pageParams, QueryCourseParamDto queryCourseParamDto) {
        return null;
    }
}
