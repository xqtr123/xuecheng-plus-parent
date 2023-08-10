package com.xuecheng.controller;

import com.xuecheng.content.model.po.CourseTeacherPO;
import com.xuecheng.content.service.ICourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(value = "教师信息相关接口", tags = "教师信息相关接口")
public class CourseTeacherController {

    @Autowired
    private ICourseTeacherService courseTeacherService;

    @ApiOperation("查询教师信息接口")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacherPO> getCourseTeacherList(@PathVariable Long courseId) {
        return courseTeacherService.queryCourseTeacher(courseId);
    }

    @ApiOperation("添加/修改教师信息接口")
    @PostMapping("/courseTeacher")
    public CourseTeacherPO saveCourseTeacher(@RequestBody CourseTeacherPO courseTeacher) {
        return courseTeacherService.saveCourseTeacher(courseTeacher);
    }

    @ApiOperation("删除教师信息接口")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable Long courseId, @PathVariable Long teacherId) {
        courseTeacherService.deleteCourseTeacher(courseId, teacherId);
    }
}
