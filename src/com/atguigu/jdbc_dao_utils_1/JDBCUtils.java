package com.atguigu.jdbc_dao_utils_1;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
//import org.apache.commons.dbcp.BasicDataSourceFactory;	//用DBCP数据库连接池时再导;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/*
整个JDBC里:
连接、关闭====》JDBCUtils工具类，JDBCUtils里面就是2个连接,2个关闭;
增删改,查====》DAO工具类
*/

/**
 * 处理事务的方式1，思路 ： JDBC增删改，查操作类，D A O ;[使用QueryRunner即dbutils版 ,DAO_Q]
 * 
 * 简单粗暴有点low，不如第二种处理事务的方式，所有连接层层外传进来，实现一个事务同一个conn；
 * 这里的所有方法都没有关闭连接，也不能关，必须在Service业务逻辑层建立连接--处理事务--关闭连接；
 * 
 * DAO和DAO_Q都是处理事务的方式1,使用方式1时应该优选DAO_Q，第三方dbutils写的肯定更严谨一些；
 * 
 */
// 方式一版的JDBCUtils
public class JDBCUtils {

	/**
	 * 2大数据库连接池技术之一：使用c3p0数据库连接池,获取的数据库连接 (推荐使用); 使用配置文件c3p0-config.xml; 更常用
	 */
	private static DataSource cpds = new ComboPooledDataSource("helloc3p0"); // xml配置名

	public static Connection getConnectionC3P0() {
		Connection conn = null;
		try {
			conn = cpds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;

	}

	/**
	 * 2大数据库连接池技术之一：使用DBCP数据库连接池,获取的数据库连接 (推荐使用); 使用配置文件dbcp.properties;
	 * 更常用的是c3p0，平时注释掉DBCP，用的时候再打开，省的DBCP静态代码块每次都加载，省的只用c3p0还必须同时导DBCP的jar包；
	 */
	// private static DataSource dataSource = null;
	// static {
	// try {
	// Properties pros = new Properties();
	// InputStream is =
	// JDBCUtils.class.getClassLoader().getResourceAsStream("dbcp.properties");//
	// 用流怼到文件上；
	// pros.load(is); // 读取文件;
	// dataSource = BasicDataSourceFactory.createDataSource(pros);//
	// 获取一个DataSource对象,需要一个Properties形参;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public static Connection getConnectionDBCP() {
	// Connection conn = null;
	// try {
	// conn = dataSource.getConnection();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return conn;
	// }

	/**
	 * 功能：连接,获取数据库连接对象(手动实现,不建议使用,原理性不会再用)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnectionDM() throws Exception {
		// 1.读取配置文件,获取四个基本信息;
		FileInputStream fis = new FileInputStream("src\\jdbc.properties");
		Properties pros = new Properties();
		pros.load(fis);

		String driverName = pros.getProperty("driverName");
		String url = pros.getProperty("url");
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");

		// 2.加载驱动
		Class.forName(driverName);

		// 3.获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	/**
	 * 功能：关闭资源 (很简单,建议使用) 三关版，连接+预编译+结果集,不需要关的填null即可; 用于DAO里，自写增删改查方法的DAO里，关资源；
	 * 
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：关闭资源 ,重载方法(很简单,建议使用) 1关版，只关连接； 专用于Service业务逻辑层里，这里只需要建连接、关连接；
	 * 
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeResource(Connection conn) {

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
