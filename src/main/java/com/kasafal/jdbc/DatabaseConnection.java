package com.kasafal.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DatabaseConnection {
	
	private static BlockingQueue<Connection> connectionPool;

	private static final int MAX_POOL_SIZE=50;
	private static int poolSize = Integer.valueOf(System.getProperty("database.connection.poolsize"));
	private static String Url;
	private static String driverName;
	private static String userName;
	private static String password;
	private static Connection con;

	public DatabaseConnection() {
		// TODO Auto-generated constructor stub
	}

	public void setUrl(String url) {
		Url = url;
	}

	public void setDrivarName(String drivarName) {
		this.driverName = drivarName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	

	public static void setPoolSize(int poolSize) {
		DatabaseConnection.poolSize = poolSize;
	}

	public static void setDriverName(String driverName) {
		DatabaseConnection.driverName = driverName;
	}

	public static void setCon(Connection con) {
		DatabaseConnection.con = con;
	}

	public void close() throws SQLException {
		if (con != null)
			con.close();
	}

	public static Connection getConnaction() {

		if (null == connectionPool) {
			synchronized (DatabaseConnection.class) {

				if (null == connectionPool) {
					try {
						Class.forName(driverName);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					connectionPool = new ArrayBlockingQueue<Connection>(poolSize);

					for (int i = 0; i < poolSize; i++) {
						try {
							connectionPool.put(DriverManager.getConnection(Url, userName, password));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		try {
			connectionPool.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
