package com.xuecheng.test;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.model.dto.QueryCourseParamDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CourseBasePO;
import com.xuecheng.content.service.ICourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class XuechengPlusContentServiceApplicationTests {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    ICourseCategoryService iCourseCategoryService;

    @Autowired
    TeachplanMapper teachplanMapper;

    @Test
    void contextLoads(){
        CourseBasePO courseBasePO = courseBaseMapper.selectById(22);
        log.info("查询到数据：{}", courseBasePO);
        Assertions.assertNotNull(courseBasePO);
    }

    @Test
    void teachplanTest(){
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(25L);
//        System.out.println();
    }

    @Test
    void courseCategoryTest(){
        List<CourseCategoryDto> categoryDtoList = iCourseCategoryService.queryTreeNodes("1");
        categoryDtoList.forEach(item ->{
            log.info("查询到数据：{}", item.toString());
        });
    }

    @Test
    void courseBaseQuery(){
        PageParams pageParams = new PageParams(1L, 5L);
        QueryCourseParamDto queryCourseParamDto = new QueryCourseParamDto("202004", "", "");

        Page<CourseBasePO> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        LambdaQueryWrapper<CourseBasePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamDto.getAuditStatus()), CourseBasePO::getAuditStatus, queryCourseParamDto.getAuditStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamDto.getPublishStatus()), CourseBasePO::getStatus, queryCourseParamDto.getPublishStatus());
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamDto.getCourseName()), CourseBasePO::getName, queryCourseParamDto.getCourseName());

        Page<CourseBasePO> courseBasePOPage = courseBaseMapper.selectPage(page, queryWrapper);
        List<CourseBasePO> records = courseBasePOPage.getRecords();
        long total = courseBasePOPage.getTotal();
        long pages = courseBasePOPage.getPages();
        long size = courseBasePOPage.getSize();
        PageResult<CourseBasePO> courseBasePOPageResult = new PageResult<>(records, total, pages, size);
        log.info("查询到数据：{}", courseBasePOPageResult);


    }
}
