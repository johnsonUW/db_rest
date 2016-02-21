package coen268.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	private static final String USER = "dbuser";
	private static final String PASS = "dbuser";
	private static final String SERVER = "localhost"; // 52.193.219.37
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver"); 
		conn = DriverManager.getConnection("jdbc:mysql://" + SERVER + ":3306" + "/yelpdb", USER, PASS);
		return conn;
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		System.out.println(getConnection());
	}
}
