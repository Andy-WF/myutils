//package com.atguigu.filter;
//
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class EncodingFilter extends HttpFilter {
//
//	@Override
//	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//
//		// 1、web.xml注册，将所有请求都先拦到这里EncodingFilter，设置一下服务器本次解码方式再放行；
//
//		///2、FilterConfig的作用 ==》ServletContext，打点getInitParameter==》可得到全局配置
//		FilterConfig filterConfig = getFilterConfig();
//		ServletContext servletContext = filterConfig.getServletContext();
//		String encoding = servletContext.getInitParameter("encoding");
//
//		///3、得到全局配置中的字符集，设为服务器本次解码方式，这是post请求乱码的解决方式；
//		request.setCharacterEncoding(encoding);
//
//		///4、设完放行，就是为了解决浏览器==》服务器乱码 ，如加中文名的书出现乱码；
//		chain.doFilter(request, response);
//
//	}
//}
//
///*
// * web.xml配合:
// * 
// * 1、添加当前工程全局参数：字符集指定写在配置文件里，不能写死在代码里，方便用户进行更改； <context-param>
// * <param-name>encoding</param-name> <param-value>UTF-8</param-value>
// * </context-param>
// * 
// * 2、给EncodingFilter注册和映射，设为拦截所有请求/*，都先走到这里被设置一下字符集再进行下一步操作； <filter>
// * <display-name>EncodingFilter</display-name>
// * <filter-name>EncodingFilter</filter-name>
// * <filter-class>com.atguigu.bookstore.filter.EncodingFilter</filter-class>
// * </filter>
// * 
// * <filter-mapping> <filter-name>EncodingFilter</filter-name>
// * <url-pattern>/*</url-pattern> </filter-mapping>
// * 
// * 
// */
