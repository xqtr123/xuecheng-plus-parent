package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacherPO;
import com.xuecheng.content.service.ICourseTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author RShL
 * @since 2023-08-10
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacherPO> implements ICourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacherPO> queryCourseTeacher(Long courseId) {
        LambdaQueryWrapper<CourseTeacherPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacherPO::getCourseId, courseId);
        return courseTeacherMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public CourseTeacherPO saveCourseTeacher(CourseTeacherPO courseTeacher) {
        Long id = courseTeacher.getId();
        if (id == null) {
            // id为null，新增教师
            CourseTeacherPO teacher = new CourseTeacherPO();
            BeanUtils.copyProperties(courseTeacher, teacher);
            teacher.setCreateDate(LocalDateTime.now());
            int flag = courseTeacherMapper.insert(teacher);
            if (flag <= 0)
                XueChengPlusException.cast("新增失败");
            return getCourseTeacher(teacher);
        } else {
            // id不为null，修改教师
            CourseTeacherPO teacher = courseTeacherMapper.selectById(id);
            BeanUtils.copyProperties(courseTeacher, teacher);
            int flag = courseTeacherMapper.updateById(teacher);
            if (flag <= 0)
                XueChengPlusException.cast("修改失败");
            return getCourseTeacher(teacher);
        }
    }

    @Override
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacherPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacherPO::getId, teacherId);
        queryWrapper.eq(CourseTeacherPO::getCourseId, courseId);
        int flag = courseTeacherMapper.delete(queryWrapper);
        if (flag < 0)
            XueChengPlusException.cast("删除失败");
    }

    public CourseTeacherPO getCourseTeacher(CourseTeacherPO courseTeacher) {
        return courseTeacherMapper.selectById(courseTeacher.getId());
    }
}
