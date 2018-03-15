package com.atguigu.runtime;




public abstract class Template {

	public abstract void code();

	public void spendTime() {
		long start = System.currentTimeMillis();

		this.code();

		long end = System.currentTimeMillis();
		System.out.println("花费的时间为：" + (end - start));
	}
}
