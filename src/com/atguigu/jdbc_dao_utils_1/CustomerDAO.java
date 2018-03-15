package com.atguigu.jdbc_dao_utils_1;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;





//D A O 的一个子类应用演示：CustomerDAO,就是针对customers表的增删改查操作;
public class CustomerDAO extends DAO_Q<Customer> { // 继承,DAO里方法直接用;

	public CustomerDAO() {		// 获取当前对象所属类的父类的泛型
		super(); 
	}
	
	public void batchAdd(Connection conn,Object[][] params) {
		// 写sql语句
		String sql = "insert into customers(name,email,birth)values(?,?,?)";
		// 调用BaseDao中批处理的方法
		batchUpdate(conn,sql, params);
	}

	// 增
	public void addCustomer(Connection conn, Customer cust) {
		String sql = "insert into customers(name,email,birth)values(?,?,?)";
		operate(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
	}

	// 删
	public void deleteById(Connection conn, int id) {
		String sql = "delete from customers where id = ?";
		operate(conn, sql, id);
	}

	// 改
	public void update(Connection conn, Customer cust) {
		String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
		operate(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
	}

	// 查 指定id的一个客户
	public Customer getCustomer(Connection conn, int id) {
		String sql = "select id,name,email,birth from customers where id = ?";
		Customer customer = getForOne(conn, sql, id);
		return customer;
	}

	// 查所有
	public List<Customer> getAll(Connection conn) {
		String sql = "select id,name,email,birth from customers";
		List<Customer> list = getForList(conn, sql);
		return list;
	}

	// 组函数查询    查总条数
	public long getCount(Connection conn) {
		String sql = "select count(*) from customers";
		return getFunctionValue(conn, sql);
	}
	//注：count(*)返回值默认是long，他认为表里数据很多，int装不下，所以此方法返回值必须设为long型；
	
	
	// 组函数查询    查最大值
	public Object getMaxId(Connection conn) {
		String sql = "select max(id) from customers ";
		Object max =getFunctionValue(conn,sql);
		return max;
	}
	
	
	
	
	
	
}


