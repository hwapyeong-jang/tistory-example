package jdbc;

import java.sql.*;

public class PreparedStatementExample {

    public static void main(String[] args) {
        MyDb.loadDriver();
        try (Connection connection = DriverManager.getConnection(MyDb.URL, MyDb.USER, MyDb.PASSWORD)) {
            /**
             * connection.prepareStatement(sql); 실행 시 다음과 같음.
             * (1) Parsing (문장 분석)
             * (2) Compile
             * 할당 받은 Connection 에 대해서 Application Layer 에 Compile 된 SQL 이 Caching 된다.
             * 또한 Statement 와 마찬가지로 DB 에도 SQL 에 대한 접근 계획이 Caching 된다.
             * 사실 DB 에 SQL 접근 계획이 Caching 되는 것은 Statement, PreparedStatement 와는 무관하다.
             * (Connection 이 끊기면 GC 에 의해서 Caching 된 SQL 이 사라진다. DB 에 Caching 되어 있는 접근 계획은 유효하다.)
             * (Application Layer 에서 SQL Caching 은 Connection 에 연결되어 있으나
             * 다른 Connection 에서 완전히 일치하는 SQL 로 PreparedStatement 객체를 생성할 경우 Caching 된 객체를 가져온다.)
             *
             * preparedStatement.executeQuery(); 실행 시 다음과 같음.
             * (3) Execute
             * Caching 된 SQL 이 있기 때문에 바로 실행된다.
             * i 에 의해서 Parameter 값이 바뀌더라도 ? 가 포함된 SQL 로 Caching 되어 있기 때문에 Caching 된 SQL 을 재사용할 수 있고,
             * (2)에 의해서 DB 에서도 ? 가 포함된 SQL 로 접근 계획인 Caching 되어 있으므로 DB 에서도 Caching 되어 있는 SQL 접근 계획을 사용한다.
             */
            for (int i = 0; i < 100; i++) {
                String sql = findByNameQuery();
                PreparedStatement preparedStatement = connection.prepareStatement(sql); // 매 번 새로 생성되지 않음. 메모리에 존재하는 경우 재사용함.
                preparedStatement.setString(1, "장화평" + i);
                ResultSet resultSet = preparedStatement.executeQuery();
                printResultSet(resultSet);
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private static String findByNameQuery() {
        return "SELECT id, name FROM user WHERE name = ?";
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
