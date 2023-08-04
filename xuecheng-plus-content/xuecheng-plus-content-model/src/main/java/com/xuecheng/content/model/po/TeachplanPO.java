package com.xuecheng.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程计划
 * </p>
 *
 * @author RShL
 * @since 2023-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TeachplanPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程计划名称
     */
    private String pname;

    /**
     * 课程计划父级Id
     */
    private Long parentid;

    /**
     * 层级，分为1、2、3级
     */
    private Integer grade;

    /**
     * 课程类型:1视频、2文档
     */
    private String media_type;

    /**
     * 开始直播时间
     */
    private LocalDateTime start_time;

    /**
     * 直播结束时间
     */
    private LocalDateTime end_time;

    /**
     * 章节及课程时介绍
     */
    private String description;

    /**
     * 时长，单位时:分:秒
     */
    private String timelength;

    /**
     * 排序字段
     */
    private Integer orderby;

    /**
     * 课程标识
     */
    private Long course_id;

    /**
     * 课程发布标识
     */
    private Long course_pub_id;

    /**
     * 状态（1正常  0删除）
     */
    private Integer status;

    /**
     * 是否支持试学或预览（试看）
     */
    private String is_preview;

    /**
     * 创建时间
     */
    private LocalDateTime create_date;

    /**
     * 修改时间
     */
    private LocalDateTime change_date;


}
