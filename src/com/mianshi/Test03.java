package com.mianshi;

public class Test03 {
	
	public static void main(String[] args)  {
		
		C c = new D();
		System.out.println(c.getResult());
		
		
	}
	
}

class C{
	int i = 10;
	
	public int getResult() {
		return getI()+10;
	}
	
	public int getI() {
		return i;
	}
	
}

class D extends C{
	int i = 20;
	
//	public int getResult() {
//		return i+20;
//	}
	
	public int getI() {
		return i;
	}
}

