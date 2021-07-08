package jdbc;

import java.sql.*;

public class StatementExample {

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(MyDb.URL, MyDb.USER, MyDb.PASSWORD)) {
            Statement statement = getStatement(connection);

            for (int i = 0; i < 100; i++) {
                ResultSet resultSet = findByName(statement, "장화평");
                printResultSet(resultSet);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private static Statement getStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    /**
     * (1) Sentence Analysis
     * (2) Compile
     * (3) Execute
     */
    private static ResultSet findByName(Statement statement, String name) throws SQLException {
        String sql = "SELECT id, name FROM user WHERE name = " + name;
        return statement.executeQuery(sql);
    }

    private static void printResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            do {
                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getString("name"));
            } while (resultSet.next());
        }
    }

}
