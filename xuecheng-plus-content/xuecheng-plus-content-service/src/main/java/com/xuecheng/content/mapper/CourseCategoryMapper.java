package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.model.po.CourseCategoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author RShL
 * @since 2023-08-07
 */
@Mapper
@MapperScan("com.xuecheng.content.mapper.xml")
public interface CourseCategoryMapper extends BaseMapper<CourseCategoryPO> {

    List<CourseCategoryDto> selectTreeNodes();
}
