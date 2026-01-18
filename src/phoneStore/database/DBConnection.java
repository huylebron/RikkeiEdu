package phoneStore.database;

import phoneStore.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL =
            "jdbc:postgresql://localhost:5432/phoneshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "huylebron2828";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Không tìm thấy PostgreSQL Driver", e);
        }
    }

    private DBConnection() {}

    /**
     * Lấy connection mới
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException("Không thể kết nối database", e);
        }
    }

    /**
     * Test nhanh kết nối DB
     */
    public static void main(String[] args) {
        try (Connection c = DBConnection.getConnection()) {
            System.out.println("Kết nối database thành công!");
            System.out.println("DB: " + c.getMetaData().getDatabaseProductName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
