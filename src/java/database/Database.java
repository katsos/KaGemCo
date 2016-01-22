package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Account;
import models.Customer;
import models.CustomerOnline;
import models.Log;
import models.Manager;
import models.ManagerRequest;
import models.Salesman;
import models.Transaction;
import models.User;

public class Database {

    // Fields //
    private static final String ip = "62.217.125.30";
    private static final String port = "3306";
    private static final String url = "jdbc:mysql://" + ip + ":" + port + "/";

    private static final String name = "it21251";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String username = "it21251";
    private static final String password = "changeit";
    private static Connection connection = null;

    // Methods //
    public static Connection connect() {

        // Check database connection, return same object if already connected
        if (Database.isConnected()) {
            return connection;
        }

        // Configure connection objectc
        try {
            Class.forName(driver).newInstance();
            Database.connection = DriverManager.getConnection(url + name, username, password);

            return connection;

        } catch (Exception ex) {	// Log exception
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return connection;
    }

    // Returns true if connected to database, false otherwise
    private static boolean isConnected() {

        return (connection != null);
    }

    // Gets an array list of users from the database
    public static ArrayList<User> getUsers() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<User> users = new ArrayList<User>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM users");

            results = prepStatement.executeQuery();

            while (results.next()) {
                String firstname = results.getString("firstname");
                String lastname = results.getString("lastname");
                String username = results.getString("username");
                //String password = results.getString("password");
                String role = results.getString("role");
                String regDate = results.getString("regDate");

                users.add(new User(firstname, lastname, username, "****", role, regDate));
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return users;
    }

    // Inserts a user to databse
    public static boolean addUser(User user) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO users "
                    + "(firstname, lastname, username, password, role, regDate)"
                    + " VALUES (?, ?, ?, ?, ?, NOW())";

            if (userExists(user.getUsername())) {
                return false;
            }

            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, user.getFirstname());
            prepStatement.setString(2, user.getLastname());
            prepStatement.setString(3, user.getUsername());
            prepStatement.setString(4, user.getPassword());
            prepStatement.setString(5, user.getRole());

            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    /**
     * Deletes a particular user from the database. Return true if user deleted
     * successfully, false if user to be deleted does not exist or if error
     * occurs.
     */
    public static boolean deleteUser(String username) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            if (!userExists(username)) {
                return false;
            }

            prepStatement = connection.prepareStatement("DELETE FROM users WHERE username=?");
            prepStatement.setString(1, username);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Checks if there is a user with the given username
    public static boolean userExists(String username) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT count(*) as count FROM users "
                    + "WHERE username=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, username);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                count = results.getInt("count");
            }

            if (count == 0) {
                return false;
            } else if (count == 1) {
                return true;
            } else {
                throw new SQLException("More than one users with the same credentials found");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Searches and returns a user with the given taxID or null if does not exist
    public static User searchUser(String username) {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM users "
                    + "WHERE username=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, username);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                String firstname = results.getString("firstname");
                String lastname = results.getString("lastname");
                String regDate = results.getDate("regDate").toString();
                String role = results.getString("role");
                return new User(firstname, lastname, username, "****", role, regDate);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    public static ArrayList<Customer> getCustomers() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<Customer> customers = new ArrayList<Customer>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM customers");

            results = prepStatement.executeQuery();

            while (results.next()) {

                Customer customer = new Customer();

                customer.setFirstname(results.getString("firstname"));
                customer.setLastname(results.getString("lastname"));
                customer.setBirthDate(results.getDate("birthDate").toString());
                customer.setGender(results.getString("gender").charAt(0));
                customer.setFamilyStatus(results.getString("familyStatus"));
                customer.setHomeAddress(results.getString("familyStatus"));
                customer.setTaxID(results.getLong("taxID"));
                customer.setBankAccountNo(results.getLong("bankAccountNo"));
                customer.setPersonalCode(results.getString("personalCode"));
                customer.setRelateTaxID(results.getLong("relateTaxID"));

                customers.add(customer);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return customers;
    }

    // Inserts a customer to databse
    public static boolean addCustomer(Customer customer) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO customers "
                    + " (firstname, lastname, birthDate, gender, familyStatus,"
                    + "homeAddress, taxID, bankAccountNo, personalCode, relateTaxID )"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            if (customerExists(customer.getTaxID())) {
                return false;
            }

//			If Date was stored as String in Customer class			
//			DateFormat format = new SimpleDateFormat("YY-MM-DD", Locale.ENGLISH);
//			Date birthDate = format.parse(customer.getBirthDate());
            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, customer.getFirstname());
            prepStatement.setString(2, customer.getLastname());
            prepStatement.setDate(3, Date.valueOf(customer.getBirthDate()));
            prepStatement.setString(4, String.valueOf(customer.getGender()));
            prepStatement.setString(5, customer.getFamilyStatus());
            prepStatement.setString(6, customer.getHomeAddress());
            prepStatement.setLong(7, customer.getTaxID());
            prepStatement.setLong(8, customer.getBankAccountNo());
            prepStatement.setString(9, customer.getPersonalCode());
            prepStatement.setLong(10, customer.getRelateTaxID());
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Checks if there is a customer with the given taxID
    public static boolean customerExists(long taxID) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT count(*) as count FROM customers "
                    + "WHERE taxID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, taxID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                count = results.getInt("count");
            }

            if (count == 0) {
                return false;
            } else if (count == 1) {
                return true;
            } else {
                throw new SQLException("More than one users with the same credentials found");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Searches and returns a customer with the given taxID or null if does not exist
    public static Customer searchCustomer(long taxID) {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM customers WHERE taxID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, taxID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                Customer customer = new Customer();

                customer.setFirstname(results.getString("firstname"));
                customer.setLastname(results.getString("lastname"));
                customer.setBirthDate(results.getDate("birthDate").toString());
                customer.setGender(results.getString("gender").charAt(0));
                customer.setFamilyStatus(results.getString("familyStatus"));
                customer.setHomeAddress(results.getString("familyStatus"));
                customer.setTaxID(results.getLong("taxID"));
                customer.setBankAccountNo(results.getLong("bankAccountNo"));
                customer.setPersonalCode(results.getString("personalCode"));
                customer.setRelateTaxID(results.getLong("relateTaxID"));

                return customer;
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    // Searches and returns a list of customers that satisfy the given
    // criteria. In order to ignore some criteria insert null to the 
    // corresponding parameter. The last parameter designates whether
    // the list of criteria will be connected with AND if true or with OR
    // if false
    public static ArrayList<Customer> searchCustomer(String firstname,
            String lastname, String birthDate, Character gender, String homeAddress,
            String familyStatus, Long bankAccountNo, Long personalCode,
            Long relateTaxID, boolean strict) throws IllegalArgumentException {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        String operator = strict ? "AND" : "OR";

        String whereList = "";
        ArrayList<Customer> customers = new ArrayList<>();

        try {

            // Query that returns the number of users with the particular username
            if (firstname != null) {
                whereList += " firstname=? " + operator;
            }
            if (lastname != null) {
                whereList += " lastname=? " + operator;
            }
            if (birthDate != null) {
                whereList += " birthDate=? " + operator;
            }
            if (gender != null) {
                whereList += " gender=? " + operator;
            }
            if (homeAddress != null) {
                whereList += " homeAddress=? " + operator;
            }
            if (familyStatus != null) {
                whereList += " familyStatus=? " + operator;
            }
            if (bankAccountNo != null) {
                whereList += " bankAccountNo=? " + operator;
            }
            if (personalCode != null) {
                whereList += " personalCode=? " + operator;
            }
            if (relateTaxID != null) {
                whereList += " relateTaxID=? " + operator;
            }

            if (whereList.isEmpty()) {
                throw new IllegalArgumentException("No search criteria defined");
            }

            // remove the extra OR and AND that was inserted in the end of the
            // string concatenations above
            if (strict) {
                whereList = whereList.substring(0, whereList.length() - 1 - 3);
            } else {
                whereList = whereList.substring(0, whereList.length() - 1 - 2);
            }

            int fieldCount = 0;
            String query = "SELECT * FROM customers WHERE" + whereList;

            System.err.println(query);

            prepStatement = connection.prepareStatement(query);

            if (firstname != null) {
                prepStatement.setString(++fieldCount, firstname);
            }
            if (lastname != null) {
                prepStatement.setString(++fieldCount, lastname);
            }
            if (birthDate != null) {
                prepStatement.setDate(++fieldCount, Date.valueOf(birthDate));
            }
            if (gender != null) {
                prepStatement.setString(++fieldCount, gender.toString());
            }
            if (homeAddress != null) {
                prepStatement.setString(++fieldCount, homeAddress);
            }
            if (familyStatus != null) {
                prepStatement.setString(++fieldCount, familyStatus);
            }
            if (bankAccountNo != null) {
                prepStatement.setLong(++fieldCount, bankAccountNo);
            }
            if (personalCode != null) {
                prepStatement.setLong(++fieldCount, personalCode);
            }
            if (relateTaxID != null) {
                prepStatement.setLong(++fieldCount, relateTaxID);
            }

            results = prepStatement.executeQuery();

            while (results.next()) {
                Customer customer = new Customer();

                customer.setFirstname(results.getString("firstname"));
                customer.setLastname(results.getString("lastname"));
                customer.setBirthDate(results.getDate("birthDate").toString());
                customer.setGender(results.getString("gender").charAt(0));
                customer.setFamilyStatus(results.getString("familyStatus"));
                customer.setHomeAddress(results.getString("familyStatus"));
                customer.setTaxID(results.getLong("taxID"));
                customer.setBankAccountNo(results.getLong("bankAccountNo"));
                customer.setPersonalCode(results.getString("personalCode"));
                customer.setRelateTaxID(results.getLong("relateTaxID"));

                customers.add(customer);
            }

            return customers;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Deletes a particular customer from the database. Return true if customer
     * deleted successfully, false if customer to be deleted does not exist or
     * if error occurs.
     */
    public static boolean deleteCustomer(long taxID) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            if (!customerExists(taxID)) {
                return false;
            }

            prepStatement = connection.prepareStatement("DELETE FROM customers WHERE taxID=?");
            prepStatement.setLong(1, taxID);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    public static ArrayList<Log> getLogs() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<Log> logs = new ArrayList<Log>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM log");

            results = prepStatement.executeQuery();

            while (results.next()) {

                Log log = new Log();

                log.setActionTime(results.getTimestamp("actionTime").toString());
                log.setActorUsername(results.getString("actorUsername"));
                log.setDescription(results.getString("description"));
                log.setLogID(results.getLong("logID"));

                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return logs;
    }

    // Inserts a customer to databse
    public static boolean addLog(Log log) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO log "
                    + "(actorUsername, description ) VALUES (?, ?)";

//			If Date was stored as String in Customer class			
//			DateFormat format = new SimpleDateFormat("YY-MM-DD", Locale.ENGLISH);
//			Date birthDate = format.parse(customer.getBirthDate());
            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, log.getActorUsername());
            prepStatement.setString(2, log.getDescription());

            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Searches and returns a customer with the given taxID or null if does not exist
    public static Log searchLog(long logID) {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM log "
                    + "WHERE logID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, logID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                Log log = new Log();

                log.setActorUsername(results.getString("actorUsername"));
                log.setActionTime(results.getTimestamp("actionTime").toString());
                log.setDescription(results.getString("description"));

                return log;
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    public static ArrayList<Account> getAccounts() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<Account> accounts = new ArrayList<>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM accounts");

            results = prepStatement.executeQuery();

            while (results.next()) {

                Account account = new Account();

                account.setBalance(results.getDouble("balance"));
                account.setOwnerTaxID(results.getLong("ownerTaxID"));
                account.setPhoneNumber(results.getLong("phoneNumber"));

                accounts.add(account);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return accounts;
    }

    // Inserts a customer to databse
    public static boolean addAccount(Account account) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO accounts "
                    + "(phoneNumber, ownerTaxID, balance) "
                    + "VALUES (?, ?, ?)";

            if (accountExists(account.getPhoneNumber())) {
                return false;
            }

//			If Date was stored as String in Customer class			
//			DateFormat format = new SimpleDateFormat("YY-MM-DD", Locale.ENGLISH);
//			Date birthDate = format.parse(customer.getBirthDate());
            prepStatement = connection.prepareStatement(query);

            prepStatement.setLong(1, account.getPhoneNumber());
            prepStatement.setLong(2, account.getOwnerTaxID());
            prepStatement.setDouble(3, account.getBalance());
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Checks if there is a customer with the given taxID
    public static boolean accountExists(long phoneNumber) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT count(*) as count FROM accounts "
                    + "WHERE phoneNumber=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, phoneNumber);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                count = results.getInt("count");
            }

            if (count == 0) {
                return false;
            } else if (count == 1) {
                return true;
            } else {
                throw new SQLException("More than one users with the same credentials found");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Searches and returns a customer with the given taxID or null if does not exist
    public static Account searchAccount(long phoneNumber) {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM accounts "
                    + "WHERE phoneNumber=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, phoneNumber);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {

                Account account = new Account();

                account.setPhoneNumber(results.getLong("phoneNumber"));
                account.setOwnerTaxID(results.getLong("ownerTaxID"));
                account.setBalance(results.getDouble("balance"));

                return account;
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Deletes a particular user from the database. Returns true if account
     * deleted successfully, false if account to be deleted does not exist or if
     * error occurs.
     */
    public static boolean deleteAccount(long phoneNumber) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            if (!accountExists(phoneNumber)) {
                return false;
            }

            prepStatement = connection.prepareStatement("DELETE FROM accounts WHERE phoneNumber=?");
            prepStatement.setLong(1, phoneNumber);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    public static ArrayList<ManagerRequest> getManagerRequests() {

        Database.connect();
        if (connection == null) {
            return null;
        }

        ArrayList<ManagerRequest> managerRequests = new ArrayList();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM manager_requests");

            results = prepStatement.executeQuery();

            while (results.next()) {

                ManagerRequest managerRequest = new ManagerRequest();

                managerRequest.setRequestID(results.getLong("requestID"));
                //managerRequest.setConfirmDate(results.getTimestamp("confirmDate").toString());
                managerRequest.setManagerUsername(results.getString("managerUsername"));
                //managerRequest.setRequestTime(results.getTimestamp("requestTime").toString());
                managerRequest.setSalesmanUsername(results.getString("salesmanUsername"));
                managerRequest.setStatus(results.getString("status"));
                managerRequest.setDescription(results.getString("description"));

                managerRequests.add(managerRequest);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return managerRequests;
    }

    // Inserts a customer to databse
    public static boolean addManagerRequest(ManagerRequest managerRequest) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO manager_requests "
                    + "(salesmanUsername, managerUsername, status, description) "
                    + "VALUES (?, ?, ?, ?)";

//			If Date was stored as String in Customer class			
//			DateFormat format = new SimpleDateFormat("YY-MM-DD", Locale.ENGLISH);
//			Date birthDate = format.parse(customer.getBirthDate());
            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, managerRequest.getSalesmanUsername());
            prepStatement.setString(2, managerRequest.getManagerUsername());
            prepStatement.setString(3, managerRequest.getStatus());
            prepStatement.setString(4, managerRequest.getDescription());
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Checks if there is a customer with the given taxID
    public static boolean managerRequestExists(long requestID) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT count(*) as count FROM manager_requests "
                    + "WHERE requestID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, requestID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                count = results.getInt("count");
            }

            if (count == 0) {
                return false;
            } else if (count == 1) {
                return true;
            } else {
                throw new SQLException("More than one users with the same credentials found");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Searches and returns a customer with the given taxID or null if does not exist
    public static ManagerRequest searchManagerRequest(long requestID) {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM manager_requests "
                    + "WHERE requestID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, requestID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                ManagerRequest managerRequest = new ManagerRequest();

                managerRequest.setRequestID(requestID);
                managerRequest.setManagerUsername(results.getString("managerUsername"));
                managerRequest.setSalesmanUsername(results.getString("salesmanUsername"));
                managerRequest.setStatus(results.getString("status"));
                managerRequest.setDescription(results.getString("description"));

                return managerRequest;
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Deletes a particular customer from the database. Return true if customer
     * deleted successfully, false if customer to be deleted does not exist or
     * if error occurs.
     */
    public static boolean deleteManagerRequest(long requestID) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            if (!managerRequestExists(requestID)) {
                return false;
            }

            prepStatement = connection.prepareStatement(
                    "DELETE FROM manager_requests WHERE requestID=?");
            prepStatement.setLong(1, requestID);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    public static ArrayList<Transaction> getTransactions() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<Transaction> transactions = new ArrayList();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM transactions");

            results = prepStatement.executeQuery();

            while (results.next()) {

                Transaction transaction = new Transaction();

                transaction.setMerit(results.getDouble("merit"));
                transaction.setPhoneNumber(results.getLong("phoneNumber"));
                transaction.setTime(results.getTimestamp("time").toString());
                transaction.setTransactionID(results.getLong("transactionID"));

                transactions.add(transaction);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return transactions;
    }

    // Inserts a customer to databse
    public static boolean addTransaction(Transaction transaction) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO transactions "
                    + "(phoneNumber, merit) VALUES (?, ?)";

//			If Date was stored as String in Customer class			
//			DateFormat format = new SimpleDateFormat("YY-MM-DD", Locale.ENGLISH);
//			Date birthDate = format.parse(customer.getBirthDate());
            prepStatement = connection.prepareStatement(query);

            prepStatement.setLong(1, transaction.getPhoneNumber());
            prepStatement.setDouble(2, transaction.getMerit());
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Checks if there is a customer with the given taxID
    public static boolean transactionExists(long transactionID) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT count(*) as count FROM transactions "
                    + "WHERE transactionID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, transactionID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                count = results.getInt("count");
            }

            if (count == 0) {
                return false;
            } else if (count == 1) {
                return true;
            } else {
                throw new SQLException("More than one transactions with the same credentials found");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Searches and returns a customer with the given taxID or null if does not exist
    public static Transaction searchTransaction(long transactionID) {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM transactions "
                    + "WHERE transactionID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, transactionID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                Transaction transaction = new Transaction();

                transaction.setPhoneNumber(results.getLong("phoneNumber"));
                transaction.setMerit(results.getDouble("merit"));
                transaction.setTime(results.getTimestamp("time").toString());

                return transaction;
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Deletes a particular customer from the database. Return true if customer
     * deleted successfully, false if customer to be deleted does not exist or
     * if error occurs.
     */
    public static boolean deleteTransaction(long transactionID) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            if (!transactionExists(transactionID)) {
                return false;
            }

            prepStatement = connection.prepareStatement(
                    "DELETE FROM transactions WHERE transactionID=?");
            prepStatement.setLong(1, transactionID);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    public static ArrayList<Salesman> getSalesmen() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<Salesman> salesmen = new ArrayList<>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement
                    = connection.prepareStatement("SELECT * FROM users"
                            + " WHERE role='salesman'");

            results = prepStatement.executeQuery();

            while (results.next()) {
                String firstname = results.getString("firstname");
                String lastname = results.getString("lastname");
                String username = results.getString("username");
                //String password = results.getString("Password");
                String role = results.getString("role");
                String regDate = results.getString("RegDate");

                salesmen.add(new Salesman(firstname, lastname, username, "****", role, regDate));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            release(results);
            release(prepStatement);
        }

        return salesmen;
    }

    public static ArrayList<Manager> getManagers() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<Manager> managers = new ArrayList<>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement
                    = connection.prepareStatement("SELECT * FROM users"
                            + " WHERE role='manager'");

            results = prepStatement.executeQuery();

            while (results.next()) {
                String firstname = results.getString("firstname");
                String lastname = results.getString("lastname");
                String username = results.getString("username");
                //String password = results.getString("Password");
                String role = results.getString("role");
                String regDate = results.getString("RegDate");

                managers.add(new Manager(firstname, lastname, username, "****", role, regDate));
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return managers;
    }

    public static ArrayList<CustomerOnline> getCustomersOnline() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<CustomerOnline> customersOnline = new ArrayList<>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM customers_online");

            results = prepStatement.executeQuery();

            while (results.next()) {

                CustomerOnline customerOnline = new CustomerOnline();

                customerOnline.setUsername(results.getString("username"));
                customerOnline.setPassword(results.getString("password"));
                customerOnline.setTaxID(results.getLong("taxID"));
                customerOnline.setRole(results.getString("role"));
                customerOnline.setRegDate(results.getString("regDate"));

                customersOnline.add(customerOnline);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return customersOnline;
    }

    // Inserts a customer to databse
    public static boolean addCustomerOnline(CustomerOnline customerOnline) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO customers_online "
                    + "(username, password, taxID, role, regDate)"
                    + " VALUES (?, ?, ?, ?, NOW())";

            if (customerOnlineExists(customerOnline.getUsername())) {
                return false;
            }

//			If Date was stored as String in Customer class			
//			DateFormat format = new SimpleDateFormat("YY-MM-DD", Locale.ENGLISH);
//			Date birthDate = format.parse(customer.getBirthDate());
            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, customerOnline.getUsername());
            prepStatement.setString(2, customerOnline.getPassword());
            prepStatement.setString(3, customerOnline.getRole());
            prepStatement.setLong(4, customerOnline.getTaxID());
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Checks if there is a customer with the given taxID
    public static boolean customerOnlineExists(String username) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT count(*) as count FROM customers_online "
                    + "WHERE username=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, username);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                count = results.getInt("count");
            }

            if (count == 0) {
                return false;
            } else if (count == 1) {
                return true;
            } else {
                throw new SQLException("More than one users with the same credentials found");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Checks if there is a customer with the given taxID
    public static boolean customerOnlineExists(long taxID) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT count(*) as count FROM customers_online "
                    + "WHERE taxID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, taxID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                count = results.getInt("count");
            }

            if (count == 0) {
                return false;
            } else if (count == 1) {
                return true;
            } else {
                throw new SQLException("More than one users with the same credentials found");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    // Searches and returns a customer with the given taxID or null if does not exist
    public static CustomerOnline searchCustomerOnline(long taxID) {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM customers_online "
                    + "WHERE taxID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, taxID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                CustomerOnline customerOnline = new CustomerOnline();

                customerOnline.setUsername(results.getString("username"));
                customerOnline.setPassword(results.getString("password"));
                customerOnline.setTaxID(results.getLong("taxID"));
                customerOnline.setRole(results.getString("role"));
                customerOnline.setRegDate(results.getString("regDate"));

                return customerOnline;
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    // Searches and returns a customer with the given taxID or null if does not exist
    public static CustomerOnline searchCustomerOnline(String username) {

        if (!checkConnection()) {
            return null;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM customers_online "
                    + "WHERE username=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, username);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                CustomerOnline customerOnline = new CustomerOnline();

                customerOnline.setUsername(results.getString("username"));
                customerOnline.setPassword(results.getString("password"));
                customerOnline.setTaxID(results.getLong("taxID"));
                customerOnline.setRole(results.getString("role"));
                customerOnline.setRegDate(results.getString("regDate"));

                return customerOnline;
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Deletes a particular customer from the database. Return true if customer
     * deleted successfully, false if customer to be deleted does not exist or
     * if error occurs.
     */
    public static boolean deleteCustomerOnline(long taxID) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            if (!customerOnlineExists(taxID)) {
                return false;
            }

            prepStatement = connection.prepareStatement(
                    "DELETE FROM customers_online WHERE taxID=?");
            prepStatement.setLong(1, taxID);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    /**
     * Deletes a particular customer from the database. Return true if customer
     * deleted successfully, false if customer to be deleted does not exist or
     * if error occurs.
     */
    public static boolean deleteCustomerOnline(String username) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            if (!customerOnlineExists(username)) {
                return false;
            }

            prepStatement = connection.prepareStatement(
                    "DELETE FROM customers_online WHERE username=?");
            prepStatement.setString(1, username);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }
        return false;
    }

    public void close() throws SQLException {
        connection.close();
    }

    // Checks the connection to the database and creates a new one if it does
    // not exist.
    private static boolean checkConnection() {
        Database.connect();
        if (isConnected()) {
            return true;
        }
        return false;
    }

    // Closes a resource
    private static void release(AutoCloseable resource) {

        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

}