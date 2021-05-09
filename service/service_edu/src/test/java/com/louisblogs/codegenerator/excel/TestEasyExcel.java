package com.louisblogs.codegenerator.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：luqi
 * @description：TODO
 * @date ：2021/4/10 12:14
 */

public class TestEasyExcel {

	public static void main(String[] args) {
		//实现excel写操作
		//1 设置写入文件夹地址和excel文件名称
	//	String filename = "C:\\Users\\卢琦\\Desktop\\table.xlsx";

		//2 调用easyexcel里面的方法实现写操作
		//write方法两个参数：第一个参数文件路径名称，第二个人参数实体类class
	//	EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(getData());

		//实现excel读操作
		String fileName = "C:\\Users\\卢琦\\Desktop\\table.xlsx";

		EasyExcel.read(fileName,DemoData.class,new ExcelListener()).sheet().doRead();
	}

	//创建方法返回list集合
	private static List<DemoData> getData() {
		List<DemoData> list = new ArrayList<>();
		for (int i = 0; i < 10; i++){
			DemoData data = new DemoData();
			data.setSno(i);
			data.setSname("lucy"+i);
			list.add(data);
		}
		return list;
	}
}













