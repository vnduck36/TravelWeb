package bsn_traveladvisor;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    public static Connection getConnection() {
        
                try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }  
        Connection conn = null;
        

        try {
            String db_name = "dudhatb1581";
            String db_url = "jdbc:mysql://mis-sql.uhcl.edu/" + db_name;
            conn = DriverManager.getConnection(db_url, "dudhatb1581", "1654958");
        }  catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       // System.out.println("DB Connected..................");
        return conn;
    }

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();               
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           // System.out.println("XXXXXXXXX Connection closed XXXXXXXXXX");
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
      public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
