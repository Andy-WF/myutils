package com.atguigu.runtime;



class SubTemplate extends Template {
	
	public void code() {
		
		
		//*****************************程序放进这里，运行test就能看到执行时间；
		boolean flag = false;
		for(int i = 2;i <= 10000;i++){
			for(int j = 2;j <= Math.sqrt(i);j++){
				if(i % j == 0){
					flag = true;
					break;
				}
			}
			if(!flag){
				System.out.println(i);
			}
			flag = false;
		}
		//********************************
		
	}
	
	
	
}