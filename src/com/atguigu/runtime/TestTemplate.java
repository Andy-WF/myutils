package com.atguigu.runtime;

/**
 * 功能：本类用于调试一段代码一共耗费了多少ms运行时间；
 * 使用方法：直接粘到子类方法内部，运行main（）即可；
 * @author WANGFEI
 *
 */

public class TestTemplate {
	
	public static void main(String[] args) {
		
		new SubTemplate().spendTime();
		
	}
}