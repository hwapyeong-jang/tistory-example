package jdbc;

import java.sql.*;

public class StatementExample {

    public static void main(String[] args) {
        MyDb.loadDriver();
        try (Connection connection = DriverManager.getConnection(MyDb.URL, MyDb.USER, MyDb.PASSWORD)) {
            /**
             * statement.executeQuery(sql); 실행 시 다음과 같음.
             * (1) Parsing (문장 분석)
             * (2) Compile
             * (3) Execute
             * Caching 이 지원되지 않고, 미리 Compile 된 Query 가 아니기 때문에 호출될 때 항상 위의 세 단계를 수행할 수 밖에 없는 구조.
             * 엄밀히 말하자면, Application Layer 에서만 Caching 이 안 되는 것이다.
             * DB 는 SQL 접근 계획 에 대해 Caching 을 하고 있다. 그러므로 DB Cache 에 있는 SQL 접근 계획과 완전히 일치하는 SQL 이 요청으로 들어온다면
             * DB 는 SQL 접근 계획을 다시 실행하지 않고 Caching 되어 있는 접근 계획을 재사용한다.
             * 즉, 아래 로직은 항상 같은 SQL 이 만들어지므로 첫 (2) 시도 이후에는 DB 에 Caching 된 접근 계획을 재사용한다.
             */
            for (int i = 0; i < 100; i++) {
                String sql = findByNameQuery("장화평");
                Statement statement = connection.createStatement(); // 매 번 새로 생성됨.
                ResultSet resultSet = statement.executeQuery(sql);
                printResultSet(resultSet);
            }

            /**
             * i 에 의해서 SQL 이 변경되므로, DB 는 Cache 에서 SQL 접근 계획을 찾지 못해 매번 접근 계획을 다시 수행한다.
             * 즉, 아래 로직은 DB 에 Caching 된 접근 계획을 재사용할 수 없다.
             */
            for (int i = 0; i < 100; i++) {
                String sql = findByNameQuery("장화평" + i);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                printResultSet(resultSet);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private static String findByNameQuery(String name) {
        return "SELECT id, name FROM user WHERE name = " + name;
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
