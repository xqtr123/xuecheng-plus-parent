package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMediaPO;
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

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

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
            teachplanPO.setCreateDate(LocalDateTime.now());
            teachplanPO.setOrderby(getTeachplanCount(teachplanPO.getCourseId(), teachplanPO.getParentid()));
            int insert = teachplanMapper.insert(teachplanPO);
            if (insert <= 0) XueChengPlusException.cast("新增失败");
        }else{
            TeachplanPO teachplanPO = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplan, teachplanPO);
            teachplanPO.setChangeDate(LocalDateTime.now());
            int i = teachplanMapper.updateById(teachplanPO);
            if(i <= 0) XueChengPlusException.cast("修改失败");
        }
    }

    @Override
    public void deleteTeachplan(Long teachplanId) {
        if(teachplanId == null){
            XueChengPlusException.cast("课程计划id为空");
        }
        LambdaQueryWrapper<TeachplanPO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TeachplanPO::getParentid, teachplanId);
        Integer count = teachplanMapper.selectCount(lambdaQueryWrapper);
        if(count > 0){
            XueChengPlusException.cast("课程计划信息还有子级信息，无法操作");
        }else {
            teachplanMapper.deleteById(teachplanId);
            LambdaQueryWrapper<TeachplanMediaPO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachplanMediaPO::getTeachplanId, teachplanId);
            teachplanMediaMapper.delete(queryWrapper);
        }
    }

    @Override
    @Transactional
    public void orderByTeachplan(String moveType, Long teachplanId) {
        TeachplanPO teachplanPO = teachplanMapper.selectById(teachplanId);
        Long courseId = teachplanPO.getCourseId();
        Long parentid = teachplanPO.getParentid();
        Integer grade = teachplanPO.getGrade();
        Integer orderby = teachplanPO.getOrderby();
        if(moveType.equals("moveup")){
            if(grade == 1){
                LambdaQueryWrapper<TeachplanPO> teachplanPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
                teachplanPOLambdaQueryWrapper.eq(TeachplanPO::getCourseId, courseId)
                        .eq(TeachplanPO::getGrade, grade)
                        .lt(TeachplanPO::getOrderby, orderby)
                        .orderByDesc(TeachplanPO::getOrderby)
                        .last("LIMIT 1");
                TeachplanPO selectOne = teachplanMapper.selectOne(teachplanPOLambdaQueryWrapper);
                exchangeOrderby(teachplanPO, selectOne);
            }else if(grade == 2){
                LambdaQueryWrapper<TeachplanPO> teachplanPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
                teachplanPOLambdaQueryWrapper.eq(TeachplanPO::getParentid, parentid)
                        .lt(TeachplanPO::getOrderby, orderby)
                        .orderByDesc(TeachplanPO::getOrderby)
                        .last("LIMIT 1");
                TeachplanPO selectOne = teachplanMapper.selectOne(teachplanPOLambdaQueryWrapper);
                exchangeOrderby(teachplanPO, selectOne);
            }
        }else if(moveType.equals("movedown")){
            if (grade == 1) {
                // 章节下移
                // SELECT * FROM teachplan WHERE courseId = 117 AND grade = 1 AND orderby > 1 ORDER BY orderby ASC LIMIT 1
                LambdaQueryWrapper<TeachplanPO> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TeachplanPO::getCourseId, courseId)
                        .eq(TeachplanPO::getGrade, grade)
                        .gt(TeachplanPO::getOrderby, orderby)
                        .orderByAsc(TeachplanPO::getOrderby)
                        .last("LIMIT 1");
                TeachplanPO tmp = teachplanMapper.selectOne(queryWrapper);
                exchangeOrderby(teachplanPO, tmp);
            } else if (grade == 2) {
                // 小节下移
                // SELECT * FROM teachplan WHERE parentId = 268 AND orderby > 1 ORDER BY orderby ASC LIMIT 1
                LambdaQueryWrapper<TeachplanPO> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TeachplanPO::getParentid, parentid)
                        .gt(TeachplanPO::getOrderby, orderby)
                        .orderByAsc(TeachplanPO::getOrderby)
                        .last("LIMIT 1");
                TeachplanPO tmp = teachplanMapper.selectOne(queryWrapper);
                exchangeOrderby(teachplanPO, tmp);
            }
        }
    }

    private void exchangeOrderby(TeachplanPO teachplanPO, TeachplanPO tmp){
        if(tmp == null){
            XueChengPlusException.cast("已经到头啦，不能再移啦");
        }else {
            Integer poOrderby = teachplanPO.getOrderby();
            Integer tmpOrderby = tmp.getOrderby();
            teachplanPO.setOrderby(tmpOrderby);
            tmp.setOrderby(poOrderby);
            teachplanMapper.updateById(teachplanPO);
            teachplanMapper.updateById(tmp);
        }
    }

    private int getTeachplanCount(Long courseId, Long parentId) {
        LambdaQueryWrapper<TeachplanPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanPO::getCourseId, courseId);
        queryWrapper.eq(TeachplanPO::getParentid, parentId);
        return teachplanMapper.selectCount(queryWrapper);
    }
}
