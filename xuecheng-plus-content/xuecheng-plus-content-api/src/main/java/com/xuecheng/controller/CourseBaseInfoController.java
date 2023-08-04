package com.xuecheng.controller;


import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamDto;
import com.xuecheng.content.model.po.CourseBasePO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@Api(value = "课程信息编辑接口", tags = "课程信息编辑接口")
public class CourseBaseInfoController {

    /**
     * 查询，根据传入的参数进行查询，返回课程基本信息
     * @param pageParams 查找所需的页码信息
     * @param queryCourseParamDto 查找所需的审核状态等信息
     * @return
     */
    @PostMapping("/course/list")
    @ApiOperation("课程查询接口")
    public PageResult<CourseBasePO> list(PageParams pageParams, @RequestBody QueryCourseParamDto queryCourseParamDto){
        System.out.println("request...");
        CourseBasePO courseBasePO = new CourseBasePO();
        courseBasePO.setId(15L);
        courseBasePO.setDescription("测试查询课程基本信息");
        PageResult<CourseBasePO> pageResult = new PageResult<>();
        pageResult.setPage(1);
        pageResult.setPageSize(5);
        pageResult.setCounts(1);
        pageResult.setItems(Collections.singletonList(courseBasePO));
        return pageResult;
    }
}
