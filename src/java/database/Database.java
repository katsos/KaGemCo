package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;

public class Database {
    
    private static final String ip = "62.217.125.30";
    private static final String port = "3306";
    private static final String url = "jdbc:mysql://" + ip + ":" + port + "/";
    
    private static final String name = "it21251";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String username = "it21251";
    private static final String password = "changeit";
    private static Connection connection = null;
    
    public static Connection connect() {
        
        if ( Database.isConnected() )
            return connection;
        
        try {
            
            Class.forName(driver).newInstance();
            Database.connection = DriverManager.getConnection( url+name, username, password );
            
            return connection;
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connection;
    }
    
    private static boolean isConnected() {
        
        return ( connection != null );
    }

    public static ArrayList<User> getUsers() {
        
        Database.connect();
        if ( connection == null )
            return null;
        
        ArrayList<User> users = new ArrayList<User>();
        
        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {
            
            prepStatement = connection.prepareStatement("SELECT * FROM Users");

            results = prepStatement.executeQuery();

            while (results.next()) {
                String username = results.getString("Username");
                //String password = results.getString("Password");
                String role = results.getString("Role");
                //String regDate = results.getString("RegDate");

                users.add( new User ( username, "****", role) );
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
        
        return users;
    }

    public static boolean deleteUser(String username) {

        Database.connect();
        if ( connection == null )
            return false;
        
        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {
            
            prepStatement = connection.prepareStatement("DELETE FROM Users WHERE Username=?");
            prepStatement.setString(1, username);
            prepStatement.execute();

            return true;
            
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
        return false;
    }
    
}
