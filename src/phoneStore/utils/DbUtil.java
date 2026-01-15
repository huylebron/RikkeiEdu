package phoneStore.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    private static final String URL =  "jdbc:postgresql://localhost:5432/phoneshop";
    private static final String USER = "postgres" ;
    private static final String PASS = "huylebron2828" ;

    public static Connection getConnection() {

        Connection conn = null;

        try{
            Class.forName("org.postgresql.Driver") ;
            conn = DriverManager.getConnection(URL , USER , PASS) ;


        } catch (ClassNotFoundException e) {
            System.err.println(" lỗi kết nối csdl : " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  conn;
    }

    public  static  void closeConnection( Connection conn) {
        if ( conn != null) {
            try {
                conn.close();
            } catch (SQLException e ) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Connection conn = DbUtil.getConnection();
        if (conn != null) {
            System.out.println("ket noi thanh cong");
            DbUtil.closeConnection(conn);
        }
        else {
            System.err.println(" ket noi thatj bai ");

        }
    }

}
