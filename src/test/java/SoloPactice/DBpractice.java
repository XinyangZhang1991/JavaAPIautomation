package SoloPactice;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBpractice {

    public static void main(String[] args) {
        String url="jdbc:mysql://shop.lemonban.com:3306/yami_shops?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String user="lemon_auto";
        String password="lemon!@123";

        // con database
        try {
            Connection conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
