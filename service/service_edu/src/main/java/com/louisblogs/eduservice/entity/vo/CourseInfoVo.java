package com.louisblogs.eduservice.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：luqi
 * @description：TODO
 * @date ：2021/4/11 20:43
 */

@ApiModel(value = "课程基本信息", description = "编辑课程基本信息的表单对象")
@Data
public class CourseInfoVo {

	@ApiModelProperty(value = "课程ID")
	@TableId(value = "id", type = IdType.ID_WORKER_STR)
	private String id;

	@ApiModelProperty(value = "课程讲师ID")
	private String teacherId;

	@ApiModelProperty(value = "课程专业ID")
	private String subjectId;

	@ApiModelProperty(value = "一级分类ID")
	private String subjectParentId;

	@ApiModelProperty(value = "课程标题")
	private String title;

	@ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
	private BigDecimal price;

	@ApiModelProperty(value = "总课时")
	private Integer lessonNum;

	@ApiModelProperty(value = "课程封面图片路径")
	private String cover;

	@ApiModelProperty(value = "课程简介")
	private String description;
}

