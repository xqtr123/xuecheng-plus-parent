package com.xuecheng.controller;

import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanPO;
import com.xuecheng.content.service.ITeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(value = "课程计划编辑接口", tags = "课程计划编辑接口")
public class TeachplanController {

    @Autowired
    private ITeachplanService iTeachplanService;

    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return iTeachplanService.getTreeNodes(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody TeachplanPO teachplanDto) {
        iTeachplanService.saveTeachplan(teachplanDto);
    }

    @ApiOperation("课程计划删除")
    @DeleteMapping("/content/teachplan/{teachplanId}")
    public void deleteTeachplan(@PathVariable Long teachplanId) {
        iTeachplanService.deleteTeachplan(teachplanId);
    }

    @ApiOperation("课程计划排序")
    @PostMapping("/teachplan/{moveType}/{teachplanId}")
    public void orderByTeachplan(@PathVariable String moveType, @PathVariable Long teachplanId) {
        iTeachplanService.orderByTeachplan(moveType, teachplanId);
    }
}
