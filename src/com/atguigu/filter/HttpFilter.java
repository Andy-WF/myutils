//package com.atguigu.filter;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public abstract class HttpFilter implements Filter {
//
//	private FilterConfig config;
//
//	// 1、 获取FilterConfig的方法；FilterConfig的作用
//	// ==》ServletContext，打点getInitParameter==》可得到全局配置
//	public FilterConfig getFilterConfig() {
//		return config;
//	}
//
//	// 2、仿照HttpServlet，从大接口重写的方法里面 强转，然后调自己写的重载方法；
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse res = (HttpServletResponse) response;
//		// 调用重载的doFilter方法
//		doFilter(req, res, chain);
//	}
//
//	// 3、自己写的重载方法，设为抽象方法，具体内容在具体子类里写，继承自写HttpFilter的各个子Filter里面只需要实现一个这方法即可;
//	public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//			throws IOException, ServletException;
//
//	// 4、Filter接口的，重写的init初始化方法；
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		this.config = filterConfig;
//		this.init();
//	}
//
//	// 5、自己加的，重载的init初始化方法，专门用来被子类有初始化需求时重写的方法，子类的初始化代码写在这个方法里面；
//	public void init() {
//
//	}
//	
//	//6、销毁方法
//	@Override
//	public void destroy() {
//
//	}
//
//}
