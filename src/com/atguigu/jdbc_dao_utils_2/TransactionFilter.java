//package com.atguigu.jdbc_dao_utils_2;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 处理事务的方式2，思路 ：
// * 
// * baseDAO里依然每个方法内部都拿连接,但是都从当前线程里拿， 每1个事务在开始时新建一个连接放进当前线程，本事务处理完成进行销毁，
// * 在此期间从当前线程里拿的都是同一个conn，实现了一个事务对应一个连接的效果；
// * 
// * 新建事务过滤器处理事务，每一个事务的新建连接和关闭连接都在这里，
// * 新建conn放进当前线程，供当前事务处理时各个方法那连接，拿的都是同一个；
// * 
// */
//public class TransactionFilter extends HttpFilter {
//
//	@Override
//	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		// 给此事务建立唯一的连接，BaseDao里每次get的都是这个conn；
//		Connection conn = JDBCUtils.getConnectionC3P0();
//		try {
//			// 开启事务
//			conn.setAutoCommit(false);
//			// 放行请求
//			chain.doFilter(request, response);
//			// 提交事务
//			conn.commit();
//		} catch (Exception e) { // 必须使用最大的异常来抓；
//			try {
//				// 出现异常回滚事务
//				conn.rollback();
//				// 重定向到一个异常页面
//				response.sendRedirect(request.getContextPath() + "/pages/error/error.jsp");
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		} finally {
//			// 关闭连接
//			JDBCUtils.closeResource();
//		}
//
//	}
//
//}
//
///*
// * web.xml注册信息，拦截所有请求： <filter> <display-name>TransactionFilter</display-name>
// * <filter-name>TransactionFilter</filter-name>
// * <filter-class>com.atguigu.bookstore.filter.TransactionFilter</filter-class>
// * </filter> <filter-mapping> <filter-name>TransactionFilter</filter-name>
// * <url-pattern>/*</url-pattern> </filter-mapping>
// */
