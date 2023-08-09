package com.xuecheng.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 课程营销信息
 * </p>
 *
 * @author RShL
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("course_market")
public class CourseMarketPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，课程id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 收费规则，对应数据字典
     */
    private String charge;

    /**
     * 现价
     */
    private Float price;

    /**
     * 原价
     */
    private Float original_price;

    /**
     * 咨询qq
     */
    private String qq;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 电话
     */
    private String phone;

    /**
     * 有效期天数
     */
    private Integer valid_days;


}
