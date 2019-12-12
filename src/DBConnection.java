import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
   public static Connection connection;
   private static boolean hasData = false;

   public static void getConnection() {
      try {
         Class.forName("org.sqlite.JDBC");
         connection = DriverManager.getConnection("jdbc:sqlite:books.db");
      } catch(ClassNotFoundException ex) {
         ex.printStackTrace();
      } catch(SQLException ex) {
         ex.printStackTrace();
      }
   }
}
