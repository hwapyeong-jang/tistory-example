package jdbc;

public final class MyDb {

    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/yourdb";
    public static final String USER = "yourUserName";
    public static final String PASSWORD = "yourPassword";

    public static void loadDriver() {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

}
