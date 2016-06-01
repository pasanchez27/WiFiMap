package com.android.wifimap.service.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.h2.tools.DeleteDbFiles;

import com.android.wifimap.service.NetPoint;

public class DataBase {
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_CONNECTION = "jdbc:h2:~/WifiMap";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";
	private static final double DELTA_OF_PLACES = 3;

	public static void createTable() throws SQLException {
		Connection connection = getDBConnection();
		DeleteDbFiles.execute("~", "WifiMap", true);
		
		PreparedStatement createPreparedStatement = null;
		String createQuery = "CREATE TABLE places (id INT AUTO_INCREMENT PRIMARY KEY, latitude DOUBLE, longitude DOUBLE, place VARCHAR(100), netName VARCHAR(100), netPwd VARCHAR(100))";
		connection.setAutoCommit(false);

		try {
			createPreparedStatement = connection.prepareStatement(createQuery);
			createPreparedStatement.executeUpdate();
			createPreparedStatement.close();

			connection.commit();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
			connection.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.close();
		}
	}

	public static void insertWithPreparedStatement(NetPoint netPoint) throws SQLException {
		Connection connection = getDBConnection();
		PreparedStatement insertPreparedStatement = null;
		String insertQuery = "INSERT INTO places" + "(latitude, longitude, place, netName, netPwd) values" + "(?,?,?,?,?)";
		connection.setAutoCommit(false);
		
		try {
			insertPreparedStatement = connection.prepareStatement(insertQuery);
			insertPreparedStatement.setDouble(1, netPoint.getLat());
			insertPreparedStatement.setDouble(2, netPoint.getLon());
			insertPreparedStatement.setString(3, netPoint.getPlace());
			insertPreparedStatement.setString(4, netPoint.getNetName());
			insertPreparedStatement.setString(5, netPoint.getNetPwd());
			insertPreparedStatement.executeUpdate();
			insertPreparedStatement.close();

			connection.commit();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public static List<NetPoint> getNearestConnections(double latitude, double longitude) throws SQLException {
		Connection connection = getDBConnection();
		PreparedStatement selectPreparedStatement = null;

		String selectQuery = "SELECT * FROM places WHERE latitude BETWEEN " + (latitude - DELTA_OF_PLACES) + " AND " + (latitude + DELTA_OF_PLACES) + " AND longitude BETWEEN " + (longitude - DELTA_OF_PLACES) +" AND " + (longitude + DELTA_OF_PLACES)+ ";";

		List<NetPoint> points = new ArrayList<NetPoint>();
		try {
			selectPreparedStatement = connection.prepareStatement(selectQuery);
			ResultSet rs = selectPreparedStatement.executeQuery();
			
			while (rs.next()) {
				NetPoint point = new NetPoint(rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("place"), rs.getString("netName"), rs.getString("netPwd"));
				points.add(point);
			}
			selectPreparedStatement.close();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		
		return points;
	}
	

	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
}
