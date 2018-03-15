package com.atguigu.jdbc_dao_utils_2;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 处理事务的方式2，思路 ：
 * 
 * baseDAO里依然每个方法内部都拿连接,但是都从当前线程里拿，
 * 每1个事务在开始时新建一个连接放进当前线程，本事务处理完成进行销毁，
 * 在此期间从当前线程里拿的都是同一个conn，实现了一个事务对应一个连接的效果；
 * 
 */
//方式二版的JDBCUtils
public class JDBCUtils {

	// 创建数据库连接池
	private static DataSource dataSource = new ComboPooledDataSource("helloc3p0");

	
	// 创建ThreadLocale对象,threadLocal代表当前线程;
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

	
	
	
	// 1、使用C3P0数据库获取连接，考虑事务的改进版，每次getConnectionC3P0其实是从当前线程里拿conn，不是从C3P0连接池里拿新的；
	public static Connection getConnectionC3P0() {
		// 从当期线程里中拿出其中的那个连接，进行return;
		Connection connection = threadLocal.get();

		// 以防万一，如果当前线程里没有放连接，就自己建一个放进去，正常使用时不会走这里;
		if (connection == null) {
			try {
				connection = dataSource.getConnection();// 从数据库连接池中获取一个连接
				threadLocal.set(connection);// 将连接设置到threadLocal中
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 把当前线程里的那个conn返回给BaseDAO里的各个方法，使得虽然每个方法都有调用ConnectionC3P0，但是其实是同一个conn；
		return connection;
	}

	
	
	
	// 2、关闭连接
	public static void closeResource() {
		Connection connection = threadLocal.get();// 拿到当前线程里的conn
		if (connection != null) {
			try {
				connection.close(); // 把它关掉
				threadLocal.remove(); // 将关闭的连接从threadLocal中移除，清空位置，保证下次事务放进去和拿出来的都是新的conn；
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
