package com.atguigu.utils;
/**
 * 功能:各种静态工具方法集合
 * 积累:所有有用的都放进来,做项目直接用;
 * 用法:把这个文件粘到项目的utils包内,跨项目是无法导包的;
 * @author WANGFEI
 *
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;





public class MyUtils {
    private static Scanner input = new Scanner(System.in);
    
    
    
    
    
    
    
    
    
    
    //以下是我自己的System.in键盘输入;转换流+标准输入流
	public static String readString() throws IOException{
		InputStreamReader isr =new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		String str=br.readLine();
		br.close();
		return str;
	}
	
	public static int readInt() throws  IOException{
		InputStreamReader isr =new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		Integer i = Integer.parseInt(br.readLine());
		br.close();
		return   i;
		
	}
	
	public static double readDouble() throws  IOException{
		InputStreamReader isr =new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		Double d = Double.parseDouble(br.readLine());
		br.close();
		return  d ;
	}
	
	public static float readFloat() throws IOException{
		InputStreamReader isr =new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		Float f =Float.parseFloat(br.readLine());
		br.close();
		return   f;
	}
	
	public static boolean readBoolean() throws  IOException{
		InputStreamReader isr =new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		Boolean b =  Boolean.parseBoolean(br.readLine());
		br.close();
		return  b;
	}
	
	public static short readShort() throws  IOException{
		InputStreamReader isr =new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		Short s = Short.parseShort(br.readLine());
		br.close();
		return   s;
	}
	
	public static byte readByte() throws  IOException{
		InputStreamReader isr =new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		Byte b =  Byte.parseByte(br.readLine());
		br.close();
		return  b;
	}
	
    
    
    
    
    
    
    
    
    //以下是一组IO文件流的操作
   
	/**
	 * 功能：缓冲流实现所有非文本文件的复制,图片、音频、视频、word等;
	 * @param source	     要复制的文件路径字符串,必须存在;如:"D:\\Iの曲库\\爱不爱我.mp3"
	 * @param destination 目标文件路径字符串,目录必须存在,文件可以不存在;如:"k:\\newMusic\\你到底爱不爱我.mp3"
	 */
	public static void copyByteFile(String source,String destination){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			FileInputStream fis = new FileInputStream(new File(source));
			FileOutputStream fos = new FileOutputStream(new File(destination));
			
			bis = new BufferedInputStream(fis);
			bos = new BufferedOutputStream(fos);
			
			byte[] b = new byte[1024];
			int len;
			while((len = bis.read(b))!=-1){
				bos.write(b, 0, len);
				bos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bos!=null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
	}
	

	/**
	 * 功能：实现纯文本文件的复制,txt;
	 * @param source      要复制的文件路径字符串,必须都存在;如:"D:\\book\\倚天屠龙记.txt"
	 * @param destination 目标文件路径字符串,目录必须存在,文件可以不存在;如:"K:\\book\\屠龙记.txt"
	 */
	public static void copyCharFile(String source,String destination){
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			FileReader fr = new FileReader(new File(source));
			FileWriter fw = new FileWriter(new File(destination));
			
			br = new BufferedReader(fr);
			bw = new BufferedWriter(fw);
			
			String str;
			while((str = br.readLine())!=null){
				bw.write(str);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bw!=null){				
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}	
			if(br!=null){			
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
	}
	
	
	
	
	
	
	//以下是一组File文件的操作
    /**
	 * 功能：删除指定目录，无论其内有没有文件和文件夹
	 * 输入：绝对目录形式创建的一个File文件夹
	 * @param file
	 * @param dir
	 */
	public static void deleteThisDir(File file){
		File[] listFiles = file.listFiles();
		//迭代遍历，删除其内各级文件击下的所有文件
		for (File subFile : listFiles) {
			if(subFile.isFile()){
				subFile.delete();		
			}else{									
				deleteThisDir(subFile);
			}
		}	
		//变成空文件夹之后一句话实现删除；
		file.delete();
	}
	
	
	
    /**
	 * 功能：删除当前File目录下的指定子目录
	 * @param 当前目录file
	 * @param 要删除的子目录名dir（String）
	 */
	public static void deleteDir(File file,String dir){
		File[] listFiles = file.listFiles();		//listFiles()会把文件和文件夹都列出来；
		for (File subFile : listFiles) {
			if(subFile.isFile()){
				
			}else{									//除了文件，就是文件夹，只有这两种情况；
				if(subFile.getName().equals(dir)){
					subFile.delete();
				}else{
					deleteDir(subFile, dir);
				}
			}
		}	
	}
	
	/**
	 * 功能：遍历输出当前file目录下的所有文件和文件夹
	 * @param file
	 * 注：找指定后缀文件只需要加个if判断即可；
	 */
	public static  void listAllSubFiles(File file){	//必须写成方法，待会要用递归；
		File[] listFiles = file.listFiles();		
		for (File subFile : listFiles) {			//遍历当前目录文件列表
			if(subFile.isFile()){
				System.out.println("文件："+subFile.getName());
			}else{
				System.out.println("文件夹："+subFile.getName());
				listAllSubFiles(subFile);			//必须使用递归
			}			
		}				
	}

	/**
	 * 功能：遍历某个File当前一级目录下的所有文件+文件夹
	 * @param file
	 */
	public static  void listSubFiles(File file){
		File[] listFiles = file.listFiles();
		for (File subFile : listFiles) {
			if(subFile.isFile()){
				System.out.println("文件："+subFile.getName());
			}else{
				System.out.println("文件夹："+subFile.getName());
			}	

		}				
	}


	/**
	 * 功能：统计指定目录及其子目录下的文件总个数 
	 * @param file
	 * @return
	 */
	public int getFileCount(File file){
		int fileCount = 0 ;
		File[] listFiles = file.listFiles();
		for (File subFile : listFiles) {
			if(subFile.isFile()){
				fileCount++;
			}else{
				getFileCount(subFile);
			}
		}
		return fileCount;	
	}
	
	/**
	 * 功能：统计指定目录及其子目录下的文件总大小
	 * @param file
	 * @return
	 */
	public long getFileSpace(File file){
		long fileSpace = 0;
		File[] listFiles = file.listFiles();
		for (File subFile : listFiles) {
			if(subFile.isFile()){
				fileSpace += subFile.length();
			}else{
				getFileCount(subFile);
			}
		}
		return fileSpace;
	}


    
    
    
    
    
	// 以下是一组集合的操作

	
	/**
	 * 功能：返回一个List对象，封装了多条员工数据
	 * @return
	 */
	public static List<Employee> getEmployeeList() {

		List<Employee> list = new ArrayList<>();

		list.add(new Employee("郑凯", 27, 10000));
		list.add(new Employee("李晨", 37, 20000));
		list.add(new Employee("邓超", 36, 20000));
		list.add(new Employee("a热巴", 22, 15000));
		list.add(new Employee("杨颖", 26, 18000));
		list.add(new Employee("王祖蓝", 28, 14000));
		list.add(new Employee("陈赫", 31, 20000));
		list.add(new Employee("c鹿晗", 22, 19000));

		return list;
	}
	
	/**
	 * 功能：遍历输出所有集合元素，通用于所有单列集合Collection
	 * @param coll
	 */
	public  static void printCollection(Collection<?> coll){
    	for (Object object : coll) {
			System.out.println(object);
		}
    }
    
      
	/**
	 * 功能：遍历输出所有集合元素，通用于所有双列集合Map
	 * @param map
	 */
	public static void printMap(Map<?,?> map){	
		Set<?> keySet = map.keySet();
		for (Object key : keySet) {
			System.out.println(key + "----->" + map.get(key));
		}
	}
	
	
	/**
	 * 功能：遍历输出前指定个数集合元素，通用于所有双列集合Map
	 * @param map   map集合名
	 * @param count	要输出前几个元素
	 */
	public static void printMapCount(Map<?,?> map,int count){	
		Set<?> keySet = map.keySet();
		int i = 0;
		for (Object key : keySet) {
			if(i>=count)
				break;
			System.out.println(key + "----->" + map.get(key));
			i++;
		}
	}
    
    
    
    
    
    
    
    //以下功能：提供一组字符串处理方法；
    
    /**
	 * 功能：搜素str字符串里有几个大写字母
	 * @param str
	 * @return 大写字母数
	 */
	public static int getUpCaseCount(String str){
		int count = 0;
		char[] charArr = str.toCharArray();
		for(int i=0;i<charArr.length;i++){
			if(Character.isUpperCase(charArr[i])){
				count++;
			}
		}
		return count;	
	}
	
    
    /**
	 * 功能：搜素str字符串里有几个子字符串sub
	 * @param str
	 * @param sub
	 * @return 数量
	 */
	public static  int  test1(String str,String sub){
		
		int count = 0;
		
		while(true){
			int index = str.indexOf(sub);
			if(index==-1)
				break;
			count++;
			str = str.substring(index+sub.length());
		}
		return count;
		
	}
    
    
    /**
	 * 功能：逆序打印字符串
	 * @param str
	 * @返回值：无返回值，直接逆序打印；
	 */
	public static void reversePrint(String str ){
		
		char[] c1 = str.toCharArray();
							
		for(int i=c1.length-1;i>=0;i--){
			System.out.print(c1[i]);	
		}
		
	}
	
	
    /**
	 * 功能：对传入的字符串进行逐个字符自然排序(a-z)，然后返回；
	 * @param str
	 * @return 排序完成的字符串
	 */
	public static String getSort(String str){
		
		char[] charArray = str.toCharArray();
		Arrays.sort(charArray);
		String newStr = "";
		
		for(int i=0;i<charArray.length;i++){
			newStr = newStr.concat(charArray[i]+"");
		}
		return newStr;
	}
    
    
    //以下功能：提供一组接受键盘输入的各种方法,各种加了条件的scanner；  
    /**
     * 功能：接受用户通过键盘输入的一个字符 ，肯定是1-6中的其中一个，如果不是，则继续循环
     * @return 1——6中的一个
     */
	public static char readMenuSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false);
            c = str.charAt(0);//  input.next().charAt(0)
            if (c != '0' &&c != '1' && c != '2' && 
                c != '3' && c != '4' && c != '5' && c!='6') {
                System.out.print("选择错误，请重新输入：");
            } else break;
        }
        return c;
    }

	/**
	 * 功能：接受用户通过键盘输入的一个字符
	 * @return 任意单个字符
	 */
    public static char readChar() {
        String str = readKeyBoard(1, false);
        return str.charAt(0);
    }
    
    /**
     * 功能：接受用户通过键盘输入的一个字符，如果用户直接回车，则返回默认值
     * @param defaultValue 默认值
     * @return  要么为用户输入的字符，要么为默认值
     */

    public static char readChar(char defaultValue) {
        String str = readKeyBoard(1, true);//str :""
        return (str.length() == 0) ? defaultValue : str.charAt(0);
    }
    
    
    /**
     * 功能：接受用户通过键盘输入的一个2位数的整型
     * @return 有效的整型
     */
    public static int readInte() {
        int n;
        for (; ; ) {
            String str = readKeyBoard(2, false);
            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("数字输入错误，请重新输入：");
            }
        }
        return n;
    }
    
    
    
    /**
     * 功能：接受用户通过键盘输入的一个2位数的整型，如果用户直接回车，则返回默认值
     * @param defaultValue 默认值
     * @return 要么输入的整型数，要么默认值
     */
    public static int readInt(int defaultValue) {
        int n;
        for (; ; ) {
            String str = readKeyBoard(2, true);
            if (str.equals("")) {
                return defaultValue;
            }

            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("数字输入错误，请重新输入：");
            }
        }
        return n;
    }
    
    
    /**
     * 功能：接受用户通过键盘输入的一关有效的字符串，长度不能超过limit
     * @param limit 限制的字符串的长度
     * @return 有效的字符串
     */
    public static String readString(int limit) {
        return readKeyBoard(limit, false);
    }
    
    
    /**
     * 功能：接受用户通过键盘输入的一关有效的字符串，长度不能超过limit，如果用户直接回车，则返回默认值
     * @param limit 限制的字符串的长度
     * @return 有效的字符串或默认值
     */
    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, true);
        return str.equals("")? defaultValue : str;
    }
    
    
    /**
     * 功能：接受用户通过键盘输入的确认选项y或n，如果输入错误，则继续循环
     * @return Y或N
     */
    public static char readConfirmSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false).toUpperCase();
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') {
                break;
            } else {
                System.out.print("选择错误，请重新输入：");
            }
        }
        return c;
    }

    
    /**
     * 功能：接受键盘输入的一个有效字符串，如果输入的无效则一直循环
     * @param limit
     * @param blankReturn
     * @return
     */
    private static String readKeyBoard(int limit, boolean blankReturn) {
        String line = "";

        while (input.hasNextLine()) {
            line = input.nextLine();//用于接受一行字符串  “”
            if (line.length() == 0) {//判断接收的字符串有无值
            	
                if (blankReturn) return line;//""
                else continue;
            }

            if (line.length() < 1 || line.length() > limit) {
                System.out.print("输入长度（不大于" + limit + "）错误，请重新输入：");
                continue;
            }
            break;
        }

        return line;
    }
}




//所用实体类
 class Employee {
	private String name;
	private int age;
	private double salary;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public Employee(String name, int age, double salary) {
		this.name = name;
		this.age = age;
	}
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", age=" + age + ", salary=" + salary + "]";
	}
}
 
 
 
 
 
 
