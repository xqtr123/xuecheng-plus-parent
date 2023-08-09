package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanPO;
import com.xuecheng.content.service.ITeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author RShL
 * @since 2023-08-09
 */
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, TeachplanPO> implements ITeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Override
    public List<TeachplanDto> getTreeNodes(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Override
    @Transactional
    public void saveTeachplan(TeachplanPO teachplan) {
        Long id = teachplan.getId();
        if(id == null){
            TeachplanPO teachplanPO = new TeachplanPO();
            BeanUtils.copyProperties(teachplan, teachplanPO);
            teachplanPO.setCreate_date(LocalDateTime.now());
            teachplanPO.setOrderby(getTeachplanCount(teachplanPO.getCourseId(), teachplanPO.getParentid()));
            int insert = teachplanMapper.insert(teachplanPO);
            if (insert <= 0) XueChengPlusException.cast("新增失败");
        }else{
            TeachplanPO teachplanPO = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplan, teachplanPO);
            teachplanPO.setChange_date(LocalDateTime.now());
            int i = teachplanMapper.updateById(teachplanPO);
            if(i <= 0) XueChengPlusException.cast("修改失败");
        }
    }

    private int getTeachplanCount(Long courseId, Long parentId) {
        LambdaQueryWrapper<TeachplanPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanPO::getCourseId, courseId);
        queryWrapper.eq(TeachplanPO::getParentid, parentId);
        return teachplanMapper.selectCount(queryWrapper);
    }
}
