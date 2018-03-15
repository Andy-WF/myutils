package com.mianshi;

public class Test04 {
	
	
	
	public static void main(String[] args) {
		
		int i = getResult();
		System.out.println(i);
		
		
	}
	
	public static int getResult() {
		int i = 0;
		
		try {
			return i++;		//_b = i++; 则：先_b=0;后i=1;
		} finally {
			++i;		//_b=++i;   则：先i=2,后_b=2; ,return的就是_b = 2;
		}
		
	}
	

}
