package com.atguigu.jdbc_dao_utils_1;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
《我的两套DAO_utils   两种处理事务的方式》
1、
DAO ：自写版 + 处理事务方式1；
DAO_Q：使用queryRunner版 + 处理事务方式1，所有连接层层外传进来，实现一个事务同一个conn；
JDBCUtils：配套使用对应文件夹下的JDBCUtils；
2、
baseDao：使用queryRunner版 + 处理事务方式2，baseDao内每个方法依然后获取连接，但都是从当前线程里拿的同一个，实现一个事务同一个conn；
JDBCUtils：配套使用对应文件夹下的JDBCUtils；
*/

/**
 * 处理事务的方式1，思路 ： JDBC增删改，查操作类，D A O ;[完全自写版]
 * 
 * 简单粗暴有点low，不如第二种处理事务的方式，所有连接层层外传进来，实现一个事务同一个conn；
 * 这里的所有方法都没有关闭连接，也不能关，必须在Service业务逻辑层建立连接--处理事务--关闭连接；
 * 
 */
//方式一版的DAO
public class DAO<T> {
	private Class<T> clazzT;

	public DAO() {
		Class clazz = this.getClass();
		ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
		Type[] types = parameterizedType.getActualTypeArguments();

		this.clazzT = (Class<T>) types[0];
	}

	/*
	 * 手法分析：
	 * 1、巧用构造器手法，构造器将被子类层层调用，到时this指的就是Dao子类对象CustomerDao，第一句获取子类运行时类,即UserDao.
	 * class； 2、获取带泛型的父类，就是获取父类，即DAO；紧接着强转，是为了下一句可以调一个特别的方法；
	 * 3、调一个方法，这个方法功能特别，专用于得到一个类的所有泛型，即拿到了T-User，只不过在一个数组的首位，暂时是type类型；
	 * 4、取出T，并且转换类型，拿到User.class，ok完成目的；
	 */

	// 目的说明
	// 通过一个UserDao对象获得DAO的泛型T对应类型的运行时类；UserDao -- DAO -- T -- T.class；
	// 这三步一起是 获取这个父类的第一个泛型T-User类 所对应的运行时类--User.class，给到clazzT;
	// 一切都是为了使得Dao的子类UserDao也能获得User.class,赋给clazzT；
	// 需求特殊，处理手法也相对复杂一些;

	// 方法说明：
	// getGenericSuperclass()---获取当前类的带泛型的父类；
	// ParameterizedType ---一种带泛型的类型，是Type的子类；
	// getActualTypeArguments---获取本类的泛型，返回是个Type型数组；

	// 继承关系说明
	// Type > parameterizedType、Class

	/**
	 * 1、查多条 功能：JDBC里不同的表通用的 查方法 ,可查任意条，返回是集合---version 2.0
	 * 强大,可查任意条1、2、3、、、、,返回固定是个集合,单查一条推荐使用getForOne();
	 * 
	 * @param conn
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> List<T> getForList(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		ArrayList<T> list = new ArrayList<T>();

		try {
			// 1.传进来连接，连接得ps
			ps = conn.prepareStatement(sql);
			// 2.ps填充
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			// 3.ps执行
			rs = ps.executeQuery();

			// 4、处理结果集
			ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集rs的元数据rsmd
			int columnCount = rsmd.getColumnCount(); // 获取结果集的列数

			while (rs.next()) { // 一个while是一条查到的数据；
				T t = (T) clazzT.newInstance();
				for (int i = 0; i < columnCount; i++) {
					Object columnValue = rs.getObject(i + 1);// 获得列值
					String columnLabel = rsmd.getColumnLabel(i + 1); // 获得列的别名

					// 利用反射，将表对象中名为columnLabel的列属性，赋值为columnValue；
					Field field = clazzT.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}

				list.add(t); // 一条数据走完一个for，把所有的列都过一遍，才变成一个对象，加进去；
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5.关闭资源,注意关的是ps和rs，不能关连接，考虑到事务；
			JDBCUtils.closeResource(null, ps, rs); // 一句话关流，方法里已经try-catch；
		}
		return null;
	}

	/**
	 * 2、查1条 功能：JDBC里不同的表通用的 查方法 ,只可查1条，返回是对象---version 2.0
	 * 完全同理，特点就是返回的是单个对象，专用于查询特定一条；
	 * 
	 * @param conn
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> T getForOne(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				T t = (T) clazzT.newInstance();
				for (int i = 0; i < columnCount; i++) {
					Object columnValue = rs.getObject(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Field field = clazzT.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源,注意关的是ps和rs，不能关连接，考虑到事务；
			JDBCUtils.closeResource(null, ps, rs);
		}
		return null;

	}

	/**
	 * 3、增删改 功能：JDBC里不同表通用的 增删改方法 ，考虑上事务---version 2.0 2.0版本 -------
	 * 就是把连接从外面传进来，其他都一样；
	 * 
	 * @param conn
	 * @param sql
	 * @param objs
	 */
	public void operate(Connection conn, String sql, Object... objs) {
		PreparedStatement ps = null;
		try {
			// 1.传进来连接，连接得ps
			ps = conn.prepareStatement(sql);
			// 2.ps填充
			for (int i = 0; i < objs.length; i++) {
				ps.setObject(i + 1, objs[i]);
			}
			// 3.ps执行
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 4.关闭资源,注意关的是ps和rs，不能关连接，考虑到事务；
			JDBCUtils.closeResource(null, ps, null);

		}
	}

	/**
	 * 4、组函数计算值查询 功能：组函数查询方法，是个泛型方法; 考虑上事务;
	 * 
	 * @param conn
	 * @param sql
	 * @param args
	 * @return
	 */
	public <E> E getFunctionValue(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			rs = ps.executeQuery();
			if (rs.next()) {
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源,注意关的是ps和rs，不能关连接，考虑到事务；
			JDBCUtils.closeResource(null, ps, rs);
		}
		return null;
	}

}
