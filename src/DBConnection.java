import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/library_db_v2";

    private static final String USER = "root";

    private static final String PASSWORD = "Root@234";

    public static Connection getConnection() {

        try {

            Connection conn =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

            return conn;

        } catch (Exception e) {

            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }
}