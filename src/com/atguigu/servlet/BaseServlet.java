//package com.atguigu.servlet;
//
//import java.io.IOException;
//import java.lang.reflect.Method;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 通用的BaseServlet ,直接拿过去; 只用来被继承，不被直接调用;
// * 
// */
//public class BaseServlet extends HttpServlet {
//
//	private static final long serialVersionUID = 1L;
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		//这里是第一次获取请求参数,在这里解决post请求乱码,更改接受请求字符集
//		request.setCharacterEncoding("UTF-8");
//		
//		String methodName = request.getParameter("method");// 1.获取要调用的方法名,注意参数名要和表单携带的参数名一致;
//		try {
//			// 2.利用反射,获取方法对象
//			Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class,
//					HttpServletResponse.class);
//
//			method.invoke(this, request, response);// 3.调用invoke方法
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		doGet(request, response);
//	}
//}
