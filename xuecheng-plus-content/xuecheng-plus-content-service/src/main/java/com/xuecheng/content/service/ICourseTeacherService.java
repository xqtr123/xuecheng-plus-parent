package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.CourseTeacherPO;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author RShL
 * @since 2023-08-10
 */
public interface ICourseTeacherService extends IService<CourseTeacherPO> {

    List<CourseTeacherPO> queryCourseTeacher(Long courseId);

    CourseTeacherPO saveCourseTeacher(CourseTeacherPO courseTeacher);

    void deleteCourseTeacher(Long courseId, Long teacherId);

}
