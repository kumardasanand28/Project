package main.java.com.aws;

import java.sql.Connection;
import java.sql.DriverManager;

public class AWSConnectionManager {


	public static Connection getRemoteConnection() throws Exception {

		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String dbName = "mysampleapp";
			String userName = "root";
			String password = "rootadmin";
			String hostname = "aau9yl99e7iq7e.cnire5ax1omy.us-west-2.rds.amazonaws.com";
			String port = "3306";
			con = DriverManager.getConnection("jdbc:mysql://"+hostname+":"+port+"/"+dbName, userName,password);

		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return con;
	}

}
