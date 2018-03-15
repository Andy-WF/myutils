package com.atguigu.jdbc_dao_utils_1;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


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
 * 处理事务的方式1，思路 ： JDBC增删改，查操作类，D A O ;[使用QueryRunner即dbutils版 ,DAO_Q]
 * 
 * 简单粗暴有点low，不如第二种处理事务的方式，所有连接层层外传进来，实现一个事务同一个conn；
 * 这里的所有方法都没有关闭连接，也不能关，必须在Service业务逻辑层建立连接--处理事务--关闭连接；
 * 
 * DAO和DAO_Q都是处理事务的方式1,使用方式1时应该优选DAO_Q，第三方dbutils写的肯定更严谨一些；
 * 
 */
//方式一版的DAO_Q
public class DAO_Q<T> {
	private Class<T> clazzT;
	private QueryRunner queryRunner = new QueryRunner();

	// 总：获取泛型T的Class对象，泛型是在被子类继承时才确定
	public DAO_Q() {
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
	// 这三步一起是 获取父类的第一个泛型T-User类 所对应的运行时类--User.class，给到clazzT;
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
	public List<T> getForList(Connection conn, String sql, Object... args) {
		List<T> list = null;
		try {
			list = queryRunner.query(conn, sql, new BeanListHandler<T>(clazzT), args);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

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
	public T getForOne(Connection conn, String sql, Object... args) { // 注意，这不是个泛型方法；
		T t = null;
		try {
			t = queryRunner.query(conn, sql, new BeanHandler<T>(clazzT), args);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
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
		try {
			queryRunner.update(conn, sql, objs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 4、组函数计算值查询 功能：组函数通用的查询方法，是个泛型方法; 考虑上事务;
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public <E> E getFunctionValue(Connection conn, String sql, Object... params) {
		Object value = null;
		try {
			value = queryRunner.query(conn, sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (E) value;
	}

	/**
	 * 5、批处理的方法 关于二维数组Object[][] params
	 * 
	 * 二维数组的第一维是sql语句要执行的次数 ；二维数组的第二维是sql语句要填充的占位符
	 * 
	 * 批量插入和更新均可 
	 * @param sql
	 * @param params
	 */
	public void batchUpdate(Connection conn, String sql, Object[][] params) {
		try {
			queryRunner.batch(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
