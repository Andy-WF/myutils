package com.atguigu.jdbc_dao_utils_1;

import java.sql.Connection;

import org.apache.commons.dbutils.QueryRunner;


public class TetsQueryRunner {
	
	public static void main(String[] args) throws Exception {
		
		//开连接
		Connection conn = JDBCUtils.getConnectionC3P0();
		
		//QueryRunner就是能代替DAO通用增删改，查 的一个工具类
		String sql = "delete from customers where id = ?";
		QueryRunner qr = new QueryRunner();
		qr.update(conn, sql, 1001);
		
		//关连接
		JDBCUtils.closeResource(conn, null,null);
		
		
	}

}
