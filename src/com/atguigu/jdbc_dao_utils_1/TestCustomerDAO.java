package com.atguigu.jdbc_dao_utils_1;

import java.sql.Connection;
import java.sql.Date;

import org.junit.jupiter.api.Test;


public class TestCustomerDAO {



	@Test
	public void testQuery() {
		CustomerDAO custDao = new CustomerDAO();
		Connection conn = JDBCUtils.getConnectionC3P0();

		// 查
		Customer customer1002 = custDao.getCustomer(conn, 1002);
		System.out.println(customer1002);
		JDBCUtils.closeResource(conn, null, null); // 每次用完连接都要对称关闭连接conn；

	}

	@Test
	public void testQueryCount() {
		CustomerDAO custDao = new CustomerDAO();
		Connection conn = JDBCUtils.getConnectionC3P0();

		// 查 组函数结果
		long count = custDao.getCount(conn);
		System.out.println(count);
		JDBCUtils.closeResource(conn, null, null); // 每次用完连接都要对称关闭连接conn；

	}

	@Test
	public void testQueryMax() {
		CustomerDAO custDao = new CustomerDAO();
		Connection conn = JDBCUtils.getConnectionC3P0();

		Object max = custDao.getMaxId(conn);
		System.out.println(max);
		JDBCUtils.closeResource(conn, null, null); // 每次用完连接都要对称关闭连接conn；

	}
	
	
	@Test
	public void testAdd() {
		CustomerDAO custDao = new CustomerDAO();
		Connection conn = JDBCUtils.getConnectionC3P0();
		// 增的使用示例,其他同理：
		Customer cust = new Customer(1000, "张浩110", "zhang@126.com", new Date(4243243245234L));
		custDao.addCustomer(conn, cust);

		System.out.println("添加成功");
		JDBCUtils.closeResource(conn, null, null);
	}
	
	
	@Test
	public void testBatchAdd() {
		CustomerDAO custDao = new CustomerDAO();
		Connection conn = JDBCUtils.getConnectionC3P0();
		
		Object[][] params = new Object[3][];	
		params[0] = new Object[]{"A","A@126.com", new Date(4243243245234L)};	//填充每条sql语句中的占位符
		params[1] = new Object[]{"B","B@126.com", new Date(4243243245234L)};	//"insert into customers(name,email,birth)values(?,?,?)";
		params[2] = new Object[]{"C","C@126.com", new Date(4243243245234L)};	
		
		custDao.batchAdd(conn,params );	//测试批量插入3条数据
		
		JDBCUtils.closeResource(conn, null, null);
	}
	
	
	

}
