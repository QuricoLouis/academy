package com.louisblogs.eduservice.entity.vo;

import lombok.Data;

/**
 * @author ：luqi
 * @description：TODO
 * @date ：2021/4/15 22:07
 */

@Data
public class CoursePublishVo {

	private String id;
	private String title;
	private String cover;
	private Integer lessonNum;
	private String subjectLevelOne;
	private String subjectLevelTwo;
	private String teacherName;
	private String price; //只用于显示

}
