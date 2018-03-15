package com.mianshi;

public class Test08 {
	
	public static void main(String[] args) throws InterruptedException {
		
		
		Thread t = new Thread();
		Thread t1 = new Thread();
		
		t.start();
		t1.start();
		
		t.sleep(1000);	//mian线程在休眠，t在运行
		t1.wait();		//t1在等待
		
	}
	
	
	
	

	
	
}
