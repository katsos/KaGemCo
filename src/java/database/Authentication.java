package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    
    public static String getRole( String username, String password) {

        Connection connection = Database.connect();
        if ( connection == null)
            return "error";
        
        String role = "error";
        PreparedStatement prepStatement = null;
        ResultSet results = null;
        
        try {
            
            prepStatement = connection.prepareStatement("SELECT role FROM Users where username=? and password=?");
                    
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            
            results = prepStatement.executeQuery();
            while( results.next() ) {
                role = results.getString("role");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {

            if ( prepStatement != null ) {
                try {
                    prepStatement.close();
                } catch ( SQLException e ) {
                    System.err.println( e.toString() );
                }
            }
            if ( results != null ) {
                try {
                    results.close();
                } catch ( SQLException e ) {
                    System.err.println( e.toString() );
                }
            }
        }
        
        return role;
    }
}