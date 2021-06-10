package icpc_club_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class databaseConnection {
    static Connection con=null;
   
    public static Connection getConnection()
    {
        
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/icpc_club_management","Username","Password");
                
            } catch (SQLException ex) {
                Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
 
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con; }
       public static void closeConnection(){
        
           try {
               if(con!=null){
                    con.close();
               }
       } catch (SQLException ex) {
           Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       
       
       }
    
}
