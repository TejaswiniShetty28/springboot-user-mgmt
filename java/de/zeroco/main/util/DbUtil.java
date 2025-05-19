package de.zeroco.main.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.zeroco.main.constant.Constant;
import de.zeroco.main.query.QueryBuilder;

public class DbUtil {
	
	public static Connection getConnection() {
		Connection connect = null;
		try {
			Class.forName(Constant.REGISTER_DRIVER);
			connect = DriverManager.getConnection(Constant.DB_URL.replaceAll("\"", ""), Constant.DB_USER.replaceAll("\"", ""), Constant.DB_PASSWORD.replaceAll("\"", ""));
//			System.out.println(Constant.DB_URL);
//			System.out.println(Constant.DB_USER);
//			System.out.println(Constant.DB_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connect;
	}
	
	public static boolean closeConnection(Connection connection) {
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static int getGeneratedKey(String schema, String tableName, List<String> columns, List<Object> value) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && Util.isBlank(columns) && Util.isBlank(value)) return 0;
		PreparedStatement statement;
		int rowId = 0;
		try {
			statement = getConnection().prepareStatement(QueryBuilder.getInsertQuery(schema, tableName, columns), Statement.RETURN_GENERATED_KEYS);
			for (int i = 1; i <= columns.size(); i++) {
				statement.setObject(i, value.get(i - 1));
			}
			statement.executeUpdate();
			ResultSet set = statement.getGeneratedKeys();
			if (set.next()) {
				rowId = set.getInt(1);
			}
			statement.close();
			set.close();
			closeConnection(getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowId;
	}
	
	public static List<Map<String, Object>> list(String schema, String tableName, List<String> columns) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && Util.isBlank(columns)) return null;
		List<Map<String, Object>> listOfMaps = new ArrayList<>();
		String columnName = "";
		Object columnValue = "";
		int countColumns = 0;
		Connection connection = null;
		try {
			connection = DbUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(QueryBuilder.getListQuery(schema, tableName, columns));
			ResultSet set = statement.executeQuery();
			ResultSetMetaData metaData = set.getMetaData();
			countColumns = metaData.getColumnCount();
			while (set.next()) {
				Map<String, Object> map = new HashMap<>();
				for (int i = 1; i <= countColumns; i++) {
					columnName = metaData.getColumnName(i);
					columnValue = set.getObject(i);
					map.put(columnName, columnValue);
				}
				listOfMaps.add(map);
			}
			statement.close();
			set.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(connection);
		}
		return listOfMaps;
	}
	
	public static int delete(String schema, String tableName, String conditionColumn, Object value) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && (Util.isBlank(conditionColumn) && (Util.isBlank(value)))) return 0;
		Connection conection = DbUtil.getConnection();
		int rowsDeleted = 0;
		try {
			PreparedStatement statement = conection.prepareStatement(QueryBuilder.getDeleteQuery(schema, tableName, conditionColumn));
			statement.setObject(1, value);
			rowsDeleted = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowsDeleted;
	}
	
	public static int update(String schema, String tableName, List<String> columns, List<Object> values, String conditionColumn, Object value) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && (Util.isBlank(columns)) && (Util.isBlank(conditionColumn)) && (Util.isBlank(values)) && (Util.isBlank(value))) return 0;
		Connection connection = getConnection();
		int effectedRows = 0;
		try {
			String query = QueryBuilder.getUpdateQuery(schema, tableName, columns, conditionColumn, value);
			PreparedStatement statement = connection.prepareStatement(query);
			int i = 1;
				for (Object key : values) {
					statement.setObject(i , key);
					i++;
				}
			effectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return effectedRows;
	}
	
	public static Map<String, Object> get(String schema, String tableName, List<String> columns, String conditionColumn, List<Object> values) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && (Util.isBlank(columns)) && (Util.isBlank(conditionColumn)) && (Util.isBlank(values))) return null;
		String columnName = "";
		Object columnValue = "";
		int countColumns = 0;
		Connection connection = null;
		Map<String, Object> map = new HashMap<>();
//		System.out.println(QueryBuilder.getQuerys(schema, tableName, columns, conditionColumn, values));
		try {
			connection = DbUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(QueryBuilder.getQuerys(schema, tableName, columns, conditionColumn, values));
			ResultSet set = statement.executeQuery();
			ResultSetMetaData metaData = set.getMetaData();
			countColumns = metaData.getColumnCount();
			while (set.next()) {
				for (int i = 1; i <= countColumns; i++) {
					columnName = metaData.getColumnName(i);
					columnValue = set.getObject(i);
					if (!map.containsKey(columnName)) {
						map.put(columnName, columnValue);
					}
				}
			}
			statement.close();
			set.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(connection);
		}
		return map;
	}
	
//	public static boolean isUserExists(String email, String phone) {
//	    return isUserExists(email, phone, 0);
//	}

	public static boolean isUserExists(String schema, String tableName, List<String> columns, List<Object> values) {
		if (Util.isBlank(schema) || Util.isBlank(tableName) || Util.isBlank(schema) || Util.isBlank(tableName)) return false;
		boolean result = true;
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement;
			statement = connection.prepareStatement(QueryBuilder.getUsers(schema, tableName, columns));
			for (int i = 0; i< columns.size(); i++) {
				statement.setObject(i, values.get(i));
			}
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				result = false;
			}
			rs.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(connection);
		}
		return result;
	}

	public static int getId(String schema, String tableName, String conditionColumn, int pk_id) {
		if (Util.isBlank(pk_id)) return 0;
		int result = 0;
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(QueryBuilder.getCount(schema, tableName, conditionColumn));
			statement.setInt(1, pk_id);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
	            result = 1; 
	        }
	        rs.close();
	        statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(connection);
		}
		return result;
	}
	
	public static Map<String, Object> getUser(String schema, String tableName, List<String> columnNames, List<Object> values) {
		if (Util.isBlank(schema) || Util.isBlank(values) || Util.isBlank(columnNames) || Util.isBlank(tableName)) return new HashMap<String, Object>();
		Map<String, Object> rowData = new HashMap<String, Object>();
		String columnName = "";
		Object columnValue = "";
		int countColumns = 0;
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(QueryBuilder.getUsers(schema, tableName, columnNames));
			for (int i = 0; i < columnNames.size(); i++) {
				statement.setObject(i + 1, values.get(i));
			}
			ResultSet rs = statement.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			countColumns = metaData.getColumnCount();
			if (rs.next()) {
				for (int i = 1; i <= countColumns; i++) {
					columnName = metaData.getColumnName(i);
					columnValue = rs.getObject(i);
					rowData.put(columnName, columnValue);
				}
			}
	        rs.close();
	        statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(connection);
		}
		return rowData;
	}
	
	public static boolean findUser(String schema, String tableName, List<String> columns, List<Object> values, String columnCondition, Object conditionValue) {
		if (Util.isBlank(schema) || Util.isBlank(tableName) || Util.isBlank(columns) || Util.isBlank(values) || Util.isBlank(columnCondition) || Util.isBlank(conditionValue)) return false;
		boolean result = true;
		Connection connection = null;
		try {
			connection = getConnection();
			String query = QueryBuilder.findUser(schema, tableName, columns, columnCondition);
			PreparedStatement statement = connection.prepareStatement(query);
			for (int i = 0; i < columns.size(); i++) {
//				System.out.println(columns.size());
//				System.out.println(i + 1);
//				System.out.println( values.get(i));
				statement.setObject(i + 1, values.get(i));
			}
			statement.setObject(columns.size() + 1, conditionValue);
//			System.out.println(columns.size());
//			System.out.println(conditionValue);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				result = false;
			}
			rs.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(connection);
		}
		return result;
	}
}
