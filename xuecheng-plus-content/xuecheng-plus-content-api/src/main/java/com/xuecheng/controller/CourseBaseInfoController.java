package com.xuecheng.controller;


import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamDto;
import com.xuecheng.content.model.po.CourseBasePO;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "课程信息编辑接口", tags = "课程信息编辑接口")
public class CourseBaseInfoController {

    @Resource
    CourseBaseInfoService courseBaseInfoService;

    /**
     * 查询，根据传入的参数进行查询，返回课程基本信息
     * @param pageParams 查找所需的页码信息
     * @param queryCourseParamDto 查找所需的审核状态等信息
     * @return
     */
    @PostMapping("/course/list")
    @ApiOperation("课程查询接口")
    public PageResult<CourseBasePO> list(PageParams pageParams, @RequestBody QueryCourseParamDto queryCourseParamDto){
        return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamDto);
    }

    @ApiOperation("新增课程基础信息接口")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Insert.class) AddCourseDto addCourseDto){
        Long companyId = 22L;
        return courseBaseInfoService.createCourseBase(companyId, addCourseDto);
    }

    @ApiOperation("根据课程id查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId) {
        return courseBaseInfoService.getCourseBaseInfo(courseId);
    }

    @ApiOperation("修改课程基础信息接口")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody EditCourseDto editCourseDto) {
        long companyId = 22L;
        return courseBaseInfoService.updateCourseBase(companyId, editCourseDto);
    }
}
