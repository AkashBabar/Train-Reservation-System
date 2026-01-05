package com.train.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.train.beans.TrainException;
import com.train.constant.ResponseCode;

public class DBUtil {

	private static final String DRIVER_NAME;
	private static final String CONNECTION_STRING;
	private static final String USERNAME;
	private static final String PASSWORD;

	static {
		ResourceBundle rb = ResourceBundle.getBundle("application");
		DRIVER_NAME = rb.getString("driverName");
		CONNECTION_STRING = rb.getString("connectionString");
		USERNAME = rb.getString("username");
		PASSWORD = rb.getString("password");

		try {
			Class.forName(DRIVER_NAME);
			System.out.println("Driver loaded: " + DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Get a new connection every time
	public static Connection getConnection() throws TrainException {
		try {
			Connection con = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			System.out.println("Database connection established!");
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TrainException(ResponseCode.DATABASE_CONNECTION_FAILURE + " : " + e.getMessage());
		}
	}
}
