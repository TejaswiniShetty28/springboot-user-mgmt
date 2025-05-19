package de.zeroco.main.query;

import java.util.List;

import de.zeroco.main.util.Util;

public class QueryBuilder {
	
	public static final String GRAVE = "`";

	public static String getInsertQuery(String schema, String tableName, List<String> columns) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && Util.isBlank(columns)) return null;
		String columnNames = "";
		String values = "";
		for (String column : columns) {
			columnNames += "," + GRAVE + column + GRAVE;
			values += ", ?";
		}
		return "INSERT INTO " + GRAVE + schema + GRAVE + "." + GRAVE + tableName + GRAVE + " (" + columnNames.substring(1) + ") VALUES (" + values.substring(1) + ") ;";
	}

	public static String getUpdateQuery(String schema, String tableName, List<String> columns, String conditionColumn,Object value) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && Util.isBlank(columns) && Util.isBlank(conditionColumn)) return null;
		String columnNames = "";
		for (String column : columns) {
			columnNames += "," + GRAVE + column + GRAVE + " = ?";
		}
		if(value instanceof String) {
			value = ",\""+value + "\"";
			value= ((String) value).substring(1);
		} 
		return "UPDATE " + GRAVE + schema + GRAVE + "." + GRAVE + tableName + GRAVE + " SET " + columnNames.substring(1) + " WHERE " + conditionColumn + " = "+value+";";
	}

	public static String getDeleteQuery(String schema, String tableName, String conditionColumn) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && Util.isBlank(conditionColumn)) return null;
		return "DELETE FROM " + GRAVE + schema + GRAVE + "." + GRAVE + tableName + GRAVE + " WHERE " + conditionColumn + "  = ? ;";
	}

	/**
	 * this method is used to generate query for get the list of Column Data
	 * 
	 * @author 
	 * @param schema
	 * @param tableName
	 * @param columns
	 * @return select query
	 */
	public static String getListQuery(String schema, String tableName, List<String> columns) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && Util.isBlank(columns)) return null;
		String columnName = "";
		for (String column : columns) {
			columnName += "," + GRAVE + column + GRAVE;
		}
		return "SELECT " + (columns.isEmpty() ? "*" : columnName.substring(1)) + " FROM " + GRAVE + schema + GRAVE + "." + GRAVE + tableName + GRAVE + ";";
	}

	public static String getQuery(String schema, String tableName, List<String> columns, String conditionColumn, String values) {
		if ((Util.isBlank(schema) && Util.isBlank(tableName)) && Util.isBlank(columns)) return null;
		String query = getListQuery(schema, tableName, columns);
		return query.substring(0, query.length() - 1) + " WHERE " + conditionColumn + " IN (" + values + ") ;";
	}

	public static String getQuerys(String schema, String tableName, List<String> columns, String conditionColumn, List<Object> values) {
		if ((Util.isBlank(schema)) && (Util.isBlank(tableName)) && (Util.isBlank(columns))) return null;
		String query = getListQuery(schema, tableName, columns);
		String valueString = "";
		for (Object value : values) {
			valueString += (value instanceof String) ? ",\"" + value + "\"" : "," + value;
		}
		return query.substring(0, query.length() - 1) + " WHERE " + conditionColumn + " IN (" + (valueString.substring(1)) + ") ;";
	}
	
//	public static String findUser() {
//	    return "SELECT * FROM user WHERE (email = ? OR phone = ?) AND pk_id != ?;";
//	}
	
	//update
	public static String findUser(String schema, String tableName, List<String> columns, String columnName) {
		if (Util.isBlank(schema) || Util.isBlank(tableName) || Util.isBlank(columns) || Util.isBlank(columnName)) return null;
		String columnNames = "";
		for (String column : columns) {
			columnNames += column +  " = ? OR ";
		}
		return "SELECT * FROM " + GRAVE + schema + GRAVE + "." + GRAVE + tableName + GRAVE + " WHERE " + "( " + columnNames.substring(0, columnNames.length() - 4) + ")" + " AND " + columnName + " != ?;";
	}
	
	public static String getCount(String schema, String tableName, String conditionColumn) {
		if (Util.isBlank(conditionColumn) || Util.isBlank(tableName) && Util.isBlank(schema)) return null;
		return "SELECT COUNT(*) FROM " + GRAVE + schema + GRAVE + "." + GRAVE + tableName + GRAVE + " WHERE " + conditionColumn + " = ?;";
	}
	
	//get user for specific id
	public static String getUser(String schema, String tableName, String conditionColumn) {
		if (Util.isBlank(conditionColumn) || Util.isBlank(tableName) || Util.isBlank(schema)) return null;
		return "SELECT * FROM " + GRAVE + schema + GRAVE + "." + GRAVE + tableName + GRAVE + " WHERE " + conditionColumn + " = ?;";
	}
	
	//save
	public static String getUsers(String schema, String tableName, List<String> columns) {
		if (Util.isBlank(columns) || Util.isBlank(tableName) || Util.isBlank(schema)) return null;
		String columnNames = "";
		for (String column : columns) {
			columnNames += column +  " = ? OR ";
		}
		return "SELECT * FROM " + GRAVE + schema + GRAVE + "." + GRAVE + tableName + GRAVE + " WHERE " + columnNames.substring(0, columnNames.length() - 4) + ";";
	}

}
