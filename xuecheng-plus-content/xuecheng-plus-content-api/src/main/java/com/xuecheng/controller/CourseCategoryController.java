package com.xuecheng.controller;

import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.service.ICourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(value = "课程分类相关接口", tags = "课程分类相关接口")
public class CourseCategoryController {

    @Autowired
    ICourseCategoryService iCourseCategoryService;

    @GetMapping("/course-category/tree-nodes")
    @ApiOperation("课程分类相关接口")
    public List<CourseCategoryDto> queryTreeNodes() {
        return iCourseCategoryService.queryTreeNodes("1");
    }
}
