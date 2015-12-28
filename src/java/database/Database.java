package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.*;

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

        if (Database.isConnected()) {
            return connection;
        }

        try {

            Class.forName(driver).newInstance();
            Database.connection = DriverManager.getConnection(url + name, username, password);

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

        return (connection != null);
    }

    public static ArrayList<User> getUsers() {

        Database.connect();
        if (connection == null) {
            return null;
        }

        ArrayList<User> users = new ArrayList<User>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM Users");

            results = prepStatement.executeQuery();

            while (results.next()) {
                String username = results.getString("Username");
                String role = results.getString("Role");
                String regDate = results.getDate("RegDate").toString();

                users.add(new User(username, "****", role, regDate));
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {

            if (prepStatement != null) {
                try {
                    prepStatement.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }
        }

        return users;
    }

    public static boolean deleteUser(String username) {

        Database.connect();
        if (connection == null) {
            return false;
        }

        PreparedStatement prepStatement = null;

        try {

            prepStatement = connection.prepareStatement("DELETE FROM Users WHERE Username=?");
            prepStatement.setString(1, username);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {

            if (prepStatement != null) {
                try {
                    prepStatement.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }
        }

        return false;
    }

    public static ArrayList<Customer> getCustomers() {

        Database.connect();
        if (connection == null) {
            return null;
        }

        ArrayList<Customer> customers = new ArrayList<Customer>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM Customers");

            results = prepStatement.executeQuery();

            while (results.next()) {

                Customer customer = new Customer();

                customer.setFirstname(results.getString("Firstname"));
                customer.setLastname(results.getString("Lastname"));
                customer.setBirthDate(results.getDate("BirthDate").toString());
                customer.setGender(results.getString("Gender").charAt(0));
                customer.setFamilyStatus(results.getString("FamilyStatus"));
                customer.setHomeAddress(results.getString("FamilyStatus"));
                customer.setTaxID(results.getLong("TaxID"));
                customer.setBankAccountNo(results.getLong("BankAccountNo"));
                customer.setPersonalCode(results.getString("PersonalCode"));

                customers.add(customer);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {

            if (prepStatement != null) {
                try {
                    prepStatement.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }

            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }
        }

        return customers;
    }

    public static ArrayList<Salesman> getSalesmen() {

        Database.connect();
        if (connection == null) {
            return null;
        }

        ArrayList<Salesman> salesmen = new ArrayList<Salesman>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM Users WHERE role='salesman'");
            results = prepStatement.executeQuery();

            while (results.next()) {
                String username = results.getString("Username");
                String role = results.getString("Role");
                String regDate = results.getDate("RegDate").toString();

                salesmen.add(new Salesman(username, "****", role, regDate));
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {

            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }

            if (prepStatement != null) {
                try {
                    prepStatement.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }

        }

        return salesmen;
    }

    public static ArrayList<Manager> getManagers() {

        Database.connect();
        if (connection == null) {
            return null;
        }

        ArrayList<Manager> managers = new ArrayList<Manager>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM Users WHERE role='manager'");
            results = prepStatement.executeQuery();

            while (results.next()) {
                String username = results.getString("Username");
                String role = results.getString("Role");
                String regDate = results.getDate("RegDate").toString();

                managers.add(new Manager(username, "****", role, regDate));
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {

            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }

            if (prepStatement != null) {
                try {
                    prepStatement.close();
                } catch (SQLException e) {
                    System.err.println(e.toString());
                }
            }

        }

        return managers;
    }
}
