package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanPO;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author RShL
 * @since 2023-08-09
 */
@Mapper
@MapperScan("com.xuecheng.content.mapper")
public interface TeachplanMapper extends BaseMapper<TeachplanPO> {

    List<TeachplanDto> selectTreeNodes(Long courseId);

}
