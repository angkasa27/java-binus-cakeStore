import java.sql.*;

public class Database {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/finalexam";

  private static final String USER = "root";
  private static final String PASSWORD = "1sampai8";

  public static void insert(String tableName, String[] columns, Object[] values) {
    String sql = "INSERT INTO " + tableName + " (";
    for (int i = 0; i < columns.length; i++) {
      sql += columns[i];
      if (i != columns.length - 1) {
        sql += ", ";
      }
    }
    sql += ") VALUES (";
    for (int i = 0; i < values.length; i++) {
      sql += "'" + values[i] + "'";
      if (i != values.length - 1) {
        sql += ", ";
      }
    }
    sql += ")";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ResultSet select(String tableName, String[] columns, String condition) {
    String sql = "SELECT ";
    for (int i = 0; i < columns.length; i++) {
      sql += columns[i];
      if (i != columns.length - 1) {
        sql += ", ";
      }
    }
    sql += " FROM " + tableName;
    if (condition != null && !condition.isEmpty()) {
      sql += " WHERE " + condition;
    }

    try {
      Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      return rs;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void update(String tableName, String[] columns, Object[] values, String condition) {
    String sql = "UPDATE " + tableName + " SET ";
    for (int i = 0; i < columns.length; i++) {
      sql += columns[i] + "='" + values[i] + "'";
      if (i != columns.length - 1) {
        sql += ", ";
      }
    }
    sql += " WHERE " + condition;

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void delete(String tableName, String condition) {
    String sql = "DELETE FROM " + tableName + " WHERE " + condition;

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
