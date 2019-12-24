import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

   public static Connection connection;

   public static void getConnection() {
      try {
         Class.forName("org.sqlite.JDBC");

         String url = "jdbc:sqlite:C:\\Users\\Administrator\\Desktop\\books.db copy\\books.db";

         connection = DriverManager.getConnection(url);
      } catch(ClassNotFoundException ex) {
         ex.printStackTrace();
      } catch(SQLException ex) {
         ex.printStackTrace();
      }
   }
}
