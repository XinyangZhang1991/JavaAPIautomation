package SoloPactice;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBpractice {

    public static void main(String[] args) {
        String url="jdbc:mysql://47.113.180.81/yami_shops?useUnicode=true&characterEncoding=utf-8&useSSL=true";
        String user="lemon";
        String password="lemon123";

        // con database
        try {
            Connection conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
