package com.atguigu.jdbc_dao_utils_2;

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
 * 处理事务的方式2，思路 ：
 * 
 * baseDAO里依然每个方法内部都拿连接,但是都从当前线程里拿， 每1个事务在开始时新建一个连接放进当前线程，本事务处理完成进行销毁，
 * 在此期间从当前线程里拿的都是同一个conn，实现了一个事务对应一个连接的效果；
 * 
 */
//方式二版的BaseDao
public class BaseDao<T> {
	private QueryRunner queryRunner = new QueryRunner();
	// 定义一个变量来接收泛型的类型
	private Class<T> type;

	public BaseDao() {
		Class clazz = this.getClass();
		ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
		Type[] types = parameterizedType.getActualTypeArguments();
		this.type = (Class<T>) types[0];
	}

	/**
	 * 1、查多条 获取所有对象
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<T> getBeanList(String sql, Object... params) {
		// 获取连接
		Connection connection = JDBCUtils.getConnectionC3P0();
		List<T> list = null;
		try {
			list = queryRunner.query(connection, sql, new BeanListHandler<T>(type), params);
		} catch (SQLException e) {
			// 将编译时异常转换为运行时异常，向上抛，使得发生异常将抛到事务过滤器TransactionFilter，使过滤器知道发生了异常；
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 为保证一个事务一个连接conn，一个事务处理完成才能关闭当前conn，关闭要放在事务过滤器TransactionFilter里，这里就不能关了；
			// JDBCUtils.closeResource();
		}
		return list;
	}

	/**
	 * 2、查一条 获取一个对象
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public T getBean(String sql, Object... params) {
		// 获取连接
		Connection connection = JDBCUtils.getConnectionC3P0();
		T t = null;
		try {
			t = queryRunner.query(connection, sql, new BeanHandler<T>(type), params);
		} catch (SQLException e) {
			// 将编译时异常转换为运行时异常，向上抛，使得发生异常将抛到事务过滤器TransactionFilter，使过滤器知道发生了异常；
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 为保证一个事务一个连接conn，一个事务处理完成才能关闭当前conn，关闭要放在事务过滤器TransactionFilter里，这里就不能关了；
			// JDBCUtils.closeResource();
		}
		return t;
	}

	/**
	 * 3、增删改 通用的增删改操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object... params) {
		// 获取连接
		Connection connection = JDBCUtils.getConnectionC3P0();
		int count = 0;
		try {
			count = queryRunner.update(connection, sql, params);
		} catch (SQLException e) {
			// 将编译时异常转换为运行时异常，向上抛，使得发生异常将抛到事务过滤器TransactionFilter，使过滤器知道发生了异常；
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 为保证一个事务一个连接conn，一个事务处理完成才能关闭当前conn，关闭要放在事务过滤器TransactionFilter里，这里就不能关了；
			// JDBCUtils.closeResource();
		}
		return count;
	}

	/**
	 * 4、组函数计算值查询 功能：组函数通用的查询方法
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public <E> E getFunction(String sql, Object... params) {
		// 获取连接
		Connection connection = JDBCUtils.getConnectionC3P0();
		Object value = null;
		try {
			value = queryRunner.query(connection, sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			// 将编译时异常转换为运行时异常，向上抛，使得发生异常将抛到事务过滤器TransactionFilter，使过滤器知道发生了异常；
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 为保证一个事务一个连接conn，一个事务处理完成才能关闭当前conn，关闭要放在事务过滤器TransactionFilter里，这里就不能关了；
			// JDBCUtils.closeResource();
		}
		return (E) value;
	}

	/**
	 * pass 为Web项目先保留，以后可删除，专为count写的一个组函数查询方法，使用组函数通用的查询方法getFunction()完全可以；
	 * 获取一个单一值的方法，专门用来执行像select count(*)... 这样的sql语句
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public Object getSingelValue(String sql, Object... params) {
		// 获取连接
		Connection connection = JDBCUtils.getConnectionC3P0();
		Object count = null;
		try {
			count = queryRunner.query(connection, sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			// 将编译时异常转换为运行时异常，向上抛，使得发生异常将抛到事务过滤器TransactionFilter，使过滤器知道发生了异常；
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 为保证一个事务一个连接conn，一个事务处理完成才能关闭当前conn，关闭要放在事务过滤器TransactionFilter里，这里就不能关了；
			// JDBCUtils.closeResource();
		}
		return count;
	}

	/**
	 * 5、批处理的方法 关于二维数组Object[][] params
	 * 
	 * 二维数组的第一维是sql语句要执行的次数 ； 二维数组的第二维是sql语句要填充的占位符
	 * 
	 * @param sql
	 * @param params
	 */
	public void batchUpdate(String sql, Object[][] params) {
		// 获取连接
		Connection conn = JDBCUtils.getConnectionC3P0();
		try {
			queryRunner.batch(conn, sql, params);
		} catch (SQLException e) {
			// 将编译时异常转换为运行时异常，向上抛，使得发生异常将抛到事务过滤器TransactionFilter，使过滤器知道发生了异常；
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 为保证一个事务一个连接conn，一个事务处理完成才能关闭当前conn，关闭要放在事务过滤器TransactionFilter里，这里就不能关了；
			// JDBCUtils.closeResource();
		}
	}
}
