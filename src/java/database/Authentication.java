package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    
    public static boolean isValid( String username, String password) {
        
        PreparedStatement prepStatement = null;
        ResultSet results = null;
        boolean status = false;

        Connection connection = Database.connect();
        if ( connection == null)
            return false;
        
        try {
            
            prepStatement = connection.prepareStatement("SELECT * FROM Users where username=? and password=?");
                    
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            
            results = prepStatement.executeQuery();
            status = results.next();

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
        
        return status;
    }
}