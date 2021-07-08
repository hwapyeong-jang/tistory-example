package jdbc;

import java.sql.*;

public class PreparedStatementExample {

    public static void main(String[] args) {
        MyDb.loadDriver();
        try (Connection connection = DriverManager.getConnection(MyDb.URL, MyDb.USER, MyDb.PASSWORD)) {
            PreparedStatement preparedStatement = getPreparedStatementFindByName(connection);

            for (int i = 0; i < 100; i++) {
                ResultSet resultSet = findByName(preparedStatement, "장화평");
                printResultSet(resultSet);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * (1) Sentence Analysis
     * (2) Compile
     */
    private static PreparedStatement getPreparedStatementFindByName(Connection connection) throws SQLException {
        String sql = "SELECT id, name FROM user WHERE name = ?";
        return connection.prepareStatement(sql);
    }

    /**
     * (3) Execute
     */
    private static ResultSet findByName(PreparedStatement preparedStatement, String name) throws SQLException {
        preparedStatement.setString(1, name);
        return preparedStatement.executeQuery(); // (3) 실행
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
