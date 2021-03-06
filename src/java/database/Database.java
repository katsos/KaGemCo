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

/**
 * Class that encompasses all methods needed for connecting and exchanging data
 * with the KaGemCo database. It is used by a major part of the servlets for
 * getting information about the users and the data that each one owns and
 * processes.
 *
 * @author pgmank
 */
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
    /**
     * Initializes a connection to the Database. This methods is called in the
     * beggining of every other method that executes transactions with the
     * database.
     *
     * @return	the Connection object created by opening new connection to the
     * database.
     */
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

    /**
     * Checks connection to the database.
     *
     * @return returns {@code true} if connected to database, {@code false}
     * otherwise
     */
    private static boolean isConnected() {

        return (connection != null);
    }

    /**
     * Gets a list of all the users stored in the database.
     *
     * @return	list of users or null if error occurs.
     */
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

    /**
     * Inserts a user to database
     *
     * @param user User to be inserted
     *
     * @return {@code true} for successful insertion, {@code false} if error
     * occurred.
     */
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
	 * Updates a user given its username and the values of the fields to be 
	 * updated. Passing null as an argument to a parameter, indicates that
	 * this parameter should remain unchanged.
	 * 
	 * @param username	username of the user to be updated
	 * @param password	new password
	 * @param firstname new firstname
	 * @param lastname	new lastname
	 * @param role		new role
	 * @throws SQLException	if database error occurs
	 */
    public static void updateUser(String username, String password, 
		String firstname, String lastname, String role) throws SQLException {

        String updateQuery = "UPDATE users SET ";

        if (!checkConnection()) {
            throw new SQLException("Connection error to database");
        }

        PreparedStatement prepStatement = null;
        int updateCount = -1;

        String whereList = "";
			
		if (!userExists(username)) {
			throw new SQLException(
				"User with username: '" + username + "' doesn't exist.");
		}

		if (password != null) {
			whereList += "password=?, ";
		}
		if (firstname != null) {
			whereList += "firstname=?, ";
		}
		if (lastname != null) {
			whereList += "lastname=?, ";
		}
		if (role != null) {
			whereList += "role=?, ";
		}


		if (whereList.isEmpty()) {
			throw new IllegalArgumentException("No update criteria defined");
		}

		// remove the extra comma and space in the end of whereList string
		whereList = whereList.substring(0, whereList.length() - 1 - 1);

		int fieldCount = 0;

		updateQuery += whereList + " WHERE username=?";
		
		try {
				 
            prepStatement = connection.prepareStatement(updateQuery);

            if (password != null) {
                prepStatement.setString(++fieldCount, password);
            }
            if (firstname != null) {
                prepStatement.setString(++fieldCount, firstname);
            }
            if (lastname != null) {
                prepStatement.setString(++fieldCount, lastname);
            }
            if (role != null) {
                prepStatement.setString(++fieldCount, role);
            }
            

            prepStatement.setString(++fieldCount, username);

            updateCount = prepStatement.executeUpdate();
			
		} catch (SQLException e) {
            System.err.println(e);
			throw new SQLException("Database error");
			
        } finally {
            release(prepStatement);
        }

		// If the updated customer count is 1, then update was successful.
		if (updateCount != 1) {
			throw new SQLException("More than one users with the same username");
		}
    }

    /**
     * Deletes a particular user from the database.
     *
     * @param username username of the user to be deleted
     *
     * @return {@code true} if user deleted successfully, {@code false} if user
     * to be deleted does not exist or if error occurs.
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

    /**
     * Checks if there is a user with the given username
     *
     * @param username String representing the user's username to be searched
     *
     * @return	{@code true} if user exists, {@code false}, if user does not
     * exist.
	 * 
	 * @throws java.sql.SQLException if database error occurs
	 * 
     */
    public static boolean userExists(String username) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Connection error to database");
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
			throw new SQLException("Database error");
        } finally {
            release(results);
            release(prepStatement);
        }
    }

    /**
     * Searches a user with the given username.
     *
     * @param	username username of the user to be searched
     *
     * @return	the user object initialized from data fetch from the database if
     * the user exists, or {@code null} if the user does not exist.
     * @throws java.sql.SQLException	If database error occurs
     */
    public static User searchUser(String username) throws SQLException,
            IllegalArgumentException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        if (username == null) {
            throw new IllegalArgumentException("Username undefined");
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
            throw new SQLException("Database Error");
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Gets a list of all the customers stored in the database.
     *
     * @return	list of customers or null if error occurs.
     */
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

    /**
     * Inserts a customer to database
     *
     * @param customer Customer to be inserted
     *
     * @return {@code true} for successful insertion, {@code false} if customer
     * to be inserted already exists.
     *
     * @throws java.sql.SQLException if database error occurs.
     */
    public static boolean addCustomer(Customer customer) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO customers "
                    + " (firstname, lastname, birthDate, gender, familyStatus,"
                    + "homeAddress, taxID, bankAccountNo, personalCode, relateTaxID)"
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
            throw new SQLException("Database error");
        } finally {
            release(results);
            release(prepStatement);
        }
    }

    /**
     * Checks if there is a customer with the given taxID
     *
     * @param taxID tax ID of the customer to be searched
     *
     * @return	{@code true} if customer exists, {@code false}, if customer does
     * not exists or error occurs.
     */
    public static boolean customerExists(long taxID) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
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
            throw new SQLException("Database error");
        } finally {
            release(results);
            release(prepStatement);
        }
    }

    /**
     * Searches a customer with the given taxID.
     *
     * @param	taxID taxID of customer to be searched
     * @return	the customer object initialized from data fetch from the database
     * if the customer exists, or {@code null} if the customer does not exist or
     * error occurs.
     * @throws java.sql.SQLException if database error occurs
     */
    public static Customer searchCustomer(long taxID) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

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
            throw new SQLException("Database Error");
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Searches a customer given its username in the internet system.
     *
     * @param	username username of the online customer
     * @return	the customer object initialized from data fetch from the database
     * if the customer exists, or {@code null} if the customer does not exist or
     * error occurs.
     * @throws java.sql.SQLException if database error occurs
     */
    public static Customer searchCustomer(String username) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "SELECT c.* FROM customers c "
                    + "JOIN customers_online co "
                    + "ON c.taxID=co.taxID "
                    + "WHERE co.username=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, username);

            results = prepStatement.executeQuery();

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
            throw new SQLException("Database Error");
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Searches and returns a list of customers that satisfy the given criteria.
     * In order to ignore some criteria insert null to the corresponding
     * parameter.
     *
     * @param firstname	Customer's firstname
     * @param lastname	Customer's lastname
     * @param birthDate	Customer's birth date
     * @param gender	Customer's gender
     * @param homeAddress	Customer's home address
     * @param familyStatus	Customer's family status
     * @param bankAccountNo	Customer's bank account number
     * @param personalCode	Customer's personal code
     * @param relateTaxID	Customer's relative tax ID
     * @param strict	designates whether the list of criteria will be connected
     * with AND if true or with OR if false
     *
     * @return	a list of customers that satisfy the given criteria
     *
     * @throws	IllegalArgumentException if no search criteria are defined, (i.e
     * null is passed in all the parameters apart from the last)..
     */
    public static ArrayList<Customer> searchCustomers(String firstname,
            String lastname, String birthDate, Character gender, String homeAddress,
            String familyStatus, Long bankAccountNo, String personalCode,
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

            // remove the extra space and extra OR or AND that was inserted in 
            // the end of the string concatenations above
            if (strict) {
                whereList = whereList.substring(0, whereList.length() - 1 - 3);
            } else {
                whereList = whereList.substring(0, whereList.length() - 1 - 2);
            }

            int fieldCount = 0;
            String query = "SELECT * FROM customers WHERE" + whereList;

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
                prepStatement.setString(++fieldCount, personalCode);
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
     *
     * Updates a particular customer from the database. Passing null to the
     * parameters apart from the last, ignores the particular search criterion.
     *
     * @param taxID	taxID of the customer to be updated.
     * @param firstname	firstname of the customer to be updated.
     * @param lastname	lastname of the customer to be updated.
     * @param birthDate	birthDate of the customer to be updated.
     * @param gender	gender of the customer to be updated.
     * @param familyStatus	familyStatus of the customer to be updated.
     * @param homeAddress	homeAddress of the customer to be updated.
     * @param bankAccountNo	bankAccountNo of the customer to be updated.
     * @param personalCode	personalCode of the customer to be updated.
     * @param relateTaxID	relateTaxID of the customer to be updated.
     *
     * @return {@code true} if customer updated successfully, false if customer
     * to be updated does not exist.
     *
     * @throws java.sql.SQLException If an SQL error occurs.
     */
    public static boolean updateCustomer(long taxID, String firstname, String lastname,
            String birthDate, Character gender, String familyStatus, String homeAddress,
            Long bankAccountNo, String personalCode, Long relateTaxID) throws
            SQLException, IllegalArgumentException {

        String updateQuery = "UPDATE customers SET ";

        if (!checkConnection()) {
            throw new SQLException("Connection error to database");
        }

        PreparedStatement prepStatement = null;
        int updateCount = -1;

        String whereList = "";

        try {

            if (!customerExists(taxID)) {
                return false;
            }

            if (firstname != null) {
                whereList += "firstname=?, ";
            }
            if (lastname != null) {
                whereList += "lastname=?, ";
            }
            if (birthDate != null) {
                whereList += "birthDate=?, ";
            }
            if (gender != null) {
                whereList += "gender=?, ";
            }
            if (homeAddress != null) {
                whereList += "homeAddress=?, ";
            }
            if (familyStatus != null) {
                whereList += "familyStatus=?, ";
            }
            if (bankAccountNo != null) {
                whereList += "bankAccountNo=?, ";
            }
            if (personalCode != null) {
                whereList += "personalCode=?, ";
            }
            if (relateTaxID != null) {
                whereList += "relateTaxID=?, ";
            }

            if (whereList.isEmpty()) {
                throw new IllegalArgumentException("No update criteria defined");
            }

            // remove the extra comma and space in the end of whereList string
            whereList = whereList.substring(0, whereList.length() - 1 - 1);

            int fieldCount = 0;

            updateQuery += whereList + " WHERE taxID=?";

            prepStatement = connection.prepareStatement(updateQuery);

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
                prepStatement.setString(++fieldCount, personalCode);
            }
            if (relateTaxID != null) {
                prepStatement.setLong(++fieldCount, relateTaxID);
            }

            prepStatement.setLong(++fieldCount, taxID);

            updateCount = prepStatement.executeUpdate();

            // If the updated customer count is 1, then update was successful.
            if (updateCount == 1) {
                return true;
            } else {
                throw new SQLException("More than one customers with the same tax ID");
            }

        } catch (SQLException e) {
            System.err.println(e);

            if (updateCount != -1) {
                throw e;
            }
            throw new SQLException("Database error");
        } finally {
            release(prepStatement);
        }
    }

    /**
     * Deletes a particular customer from the database.
     *
     * @param taxID	taxID of the customer to be deleted.
     *
     * @return {@code true} if customer deleted successfully, false if customer
     * to be deleted does not exist or if error occurs.
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

    /**
     * Gets an list of all the logs stored in the database.
     *
     * @return	list of logs or null if error occurs.
     */
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

    /**
     * Inserts a log to database
     *
     * @param log Log to be inserted
     *
     * @return {@code true} for successful insertion, {@code false} if error
     * occurred.
     */
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

    /**
     * Searches a log with the given logID.
     *
     * @param	logID logID of the log to be searched
     *
     * @return	the log object initialized from data fetch from the database if
     * the log exists, or {@code null} if the user does not exist or error
     * occurs.
     */
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

    /**
     * Gets an list of all the accounts stored in the database.
     *
     * @return	list of accounts or null if error occurs.
     */
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

    /**
     * Inserts an account to database
     *
     * @param account Account to be inserted
     *
     * @return	{@code true} for successful insertion, {@code false} if account
     * already exists.
     *
     * @throws java.sql.SQLException if database error occurs
     */
    public static boolean addAccount(Account account) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;

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
            throw new SQLException("Database error");
        } finally {
            release(prepStatement);
        }
    }

    /**
     * Checks if there is an account with the given phone number
     *
     * @param phoneNumber phone number of the account to be searched
     *
     * @return	{@code true} if account exists, {@code false}, if account does
     * not exist or error occurs.
     */
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

    /**
     * Searches an account with the given phone number.
     *
     * @param	phoneNumber phone number of the account to be searched
     * @return	the account object initialized from data fetch from the database
     * if the account exists, or {@code null} if the account does not exist or
     * error occurs.
     */
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
     * Searches the accounts that a particular customer has given his tax ID.
     *
     * @param	ownerTaxID tax ID of the accounts' owner
     *
     * @return	the list of accounts owned by the customer with the given tax ID.
     *
     * @throws	java.sql.SQLException if database error occurs
     */
    public static ArrayList<Account> searchAccounts(long ownerTaxID) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT * FROM accounts "
                    + "WHERE ownerTaxID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, ownerTaxID);

            results = prepStatement.executeQuery();

            ArrayList<Account> accounts = new ArrayList<>();

            while (results.next()) {

                Account account = new Account();

                account.setPhoneNumber(results.getLong("phoneNumber"));
                account.setOwnerTaxID(results.getLong("ownerTaxID"));
                account.setBalance(results.getDouble("balance"));

                accounts.add(account);
            }

            return accounts;

        } catch (SQLException e) {
            System.err.println(e);
            throw new SQLException("Database error");
        } finally {
            release(results);
            release(prepStatement);
        }
    }

    /**
     * Gets the number of accounts that a particular customer has.
     *
     * @param	ownerTaxID tax ID of the accounts' owner
     *
     * @return	the number of accounts that a customer possesses.
     *
     * @throws	java.sql.SQLException if database error occurs
     */
    public static int getCustomerAccountCount(long ownerTaxID) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            // Query that returns the number of users with the particular username
            String query = "SELECT count(*) as count FROM accounts "
                    + "WHERE ownerTaxID=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setLong(1, ownerTaxID);

            results = prepStatement.executeQuery();

            int count = 0;

            if (results.next()) {
                count = results.getInt("count");
            } else {
                throw new SQLException("Customer with tax ID: " + ownerTaxID
                        + " does not exist");
            }

            return count;

        } catch (SQLException e) {
            System.err.println(e);
            throw new SQLException("Database error");
        } finally {
            release(results);
            release(prepStatement);
        }
    }

    /**
     * Updates a particular account from the database.
     *
     * @param	phoneNumber	phone number of the account to be updated
     * @param	balance	account's new balance
     * @return {@code true} if account updated successfully, or {@code false} if
     * account to be updated does not exist
     * @throws java.sql.SQLException if a database error occurs
     */
    public static boolean updateAccount(long phoneNumber, Double balance)
            throws SQLException {

        String updateQuery = "UPDATE accounts SET balance=? WHERE phoneNumber=?";

        if (!checkConnection()) {
            throw new SQLException("Connection error to database");
        }

        PreparedStatement prepStatement = null;
        int updateCount = -1;

        String whereList = "";

        try {

            if (!accountExists(phoneNumber)) {
                return false;
            }

            if (balance != null) {
                whereList += "balance=?";
            }

            if (whereList.isEmpty()) {
                throw new IllegalArgumentException("No update criteria defined");
            }

            prepStatement = connection.prepareStatement(updateQuery);

            prepStatement.setDouble(1, balance);
            prepStatement.setLong(2, phoneNumber);

            updateCount = prepStatement.executeUpdate();

            // If the updated account count is 1, then update was successful.
            if (updateCount == 1) {
                return true;
            } else {
                throw new SQLException("More than one accounts with the same phone number");
            }

        } catch (SQLException e) {
            System.err.println(e);

            if (updateCount != -1) {
                throw e;
            }
            throw new SQLException("Database error");
        } finally {
            release(prepStatement);
        }
    }

    /**
     * Recharge a particular account from the database.
     *
     * @param	phoneNumber	phone number of the account to be recharged
     * @param	amount	rechargeAccount amount
     * @return {@code true} if account recharged successfully, or {@code false}
     * if account to be recharged does not exist
     * @throws java.sql.SQLException if a database error occurs
     */
    public static boolean rechargeAccount(long phoneNumber, Double amount)
            throws SQLException {

        String updateQuery = "UPDATE accounts SET balance = balance + ? WHERE phoneNumber=?";

        if (!checkConnection()) {
            throw new SQLException("Connection error to database");
        }

        PreparedStatement prepStatement = null;
        int updateCount = -1;

        String whereList = "";

        try {

            if (!accountExists(phoneNumber)) {
                return false;
            }

            if (amount != null) {
                whereList += "balance=?";
            }

            if (whereList.isEmpty()) {
                throw new IllegalArgumentException("No recharge criteria defined");
            }

            prepStatement = connection.prepareStatement(updateQuery);

            prepStatement.setDouble(1, amount);
            prepStatement.setLong(2, phoneNumber);

            updateCount = prepStatement.executeUpdate();

            // If the updated account count is 1, then update was successful.
            if (updateCount == 1) {
                return true;
            } else {
                throw new SQLException("More than one accounts with the same phone number");
            }

        } catch (SQLException e) {
            System.err.println(e);

            if (updateCount != -1) {
                throw e;
            }
            throw new SQLException("Database error");
        } finally {
            release(prepStatement);
        }
    }

    /**
     * Deletes a particular account from the database.
     *
     * @param	phoneNumber	phone number of the account to be deleted
     * @return {@code true} if account deleted successfully, or {@code false} if
     * account to be deleted does not exist
     * @throws java.sql.SQLException
     */
    public static boolean deleteAccount(long phoneNumber) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;

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
            throw new SQLException("Database error");
        } finally {
            release(prepStatement);
        }
    }

    /**
     * Gets a list of all the manager requests stored in the database.
     *
     * @return	list of manager requests or null if error occurs.
     * @throws java.sql.SQLException if database error occurs
     */
    public static ArrayList<ManagerRequest> getManagerRequests() throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        ArrayList<ManagerRequest> managerRequests = new ArrayList<>();

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT * FROM manager_requests WHERE status='pending'");

            results = prepStatement.executeQuery();

            while (results.next()) {

                ManagerRequest managerRequest = new ManagerRequest();

                managerRequest.setRequestID(results.getLong("requestID"));
                //managerRequest.setConfirmDate(results.getTimestamp("confirmDate").toString());
                managerRequest.setManagerUsername(results.getString("managerUsername"));
                //managerRequest.setRequestTime(results.getTimestamp("requestTime").toString());
                managerRequest.setSalesmanUsername(results.getString("salesmanUsername"));
                managerRequest.setCustomerTaxID(results.getLong("customerTaxID"));
                managerRequest.setRequestedPhoneNumber(results.getLong("requestedPhoneNumber"));
                managerRequest.setPhoneNumberCount(results.getInt("phoneNumberCount"));
                managerRequest.setStatus(results.getString("status"));
                managerRequest.setDescription(results.getString("description"));

                managerRequests.add(managerRequest);
            }

        } catch (SQLException e) {
            System.err.println(e);
            throw new SQLException("Database error");
        } finally {
            release(results);
            release(prepStatement);
        }

        return managerRequests;
    }

    /**
     * Inserts a manager request to database
     *
     * @param managerRequest ManagerRequest to be inserted
     *
     * @throws java.sql.SQLException if a database error occurs
     *
     */
    public static void addManagerRequest(ManagerRequest managerRequest) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;

        try {

            String query = "INSERT INTO manager_requests "
                    + "(salesmanUsername, managerUsername, customerTaxID, "
                    + "requestedPhoneNumber, phoneNumberCount, status, description) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

//			If Date was stored as String in Customer class			
//			DateFormat format = new SimpleDateFormat("YY-MM-DD", Locale.ENGLISH);
//			Date birthDate = format.parse(customer.getBirthDate());
            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, managerRequest.getSalesmanUsername());
            prepStatement.setString(2, managerRequest.getManagerUsername());
            prepStatement.setLong(3, managerRequest.getCustomerTaxID());
            prepStatement.setLong(4, managerRequest.getRequestedPhoneNumber());
            prepStatement.setInt(5, managerRequest.getPhoneNumberCount());
            prepStatement.setString(6, managerRequest.getStatus());
            prepStatement.setString(7, managerRequest.getDescription());
            prepStatement.execute();

        } catch (SQLException e) {
            System.err.println(e);
            throw new SQLException("Error while inserting manager request to database");
        } finally {
            release(prepStatement);
        }
    }

    /**
     * Checks if there is a manager request with the given requestID
     *
     * @param requestID of the manager request to be checked
     *
     * @return	{@code true} if manager request exists, {@code false}, if manager
     * request does not exists or error occurs.
     */
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

    /**
     * Searches a manager request with the given requestID.
     *
     * @param	requestID request ID of the manager request to be searched
     * @return	the {@link models.ManagerRequest} object initialized from data
     * fetched from the database if the manager request exists, or {@code null}
     * if the user does not exist or error occurs.
     */
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

                managerRequest.setRequestID(results.getLong("requestID"));
                managerRequest.setManagerUsername(results.getString("managerUsername"));
                managerRequest.setSalesmanUsername(results.getString("salesmanUsername"));
                managerRequest.setCustomerTaxID(results.getLong("customerTaxID"));
                managerRequest.setRequestedPhoneNumber(results.getLong("requestedPhoneNumber"));
                managerRequest.setPhoneNumberCount(results.getInt("phoneNumberCount"));
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
     * Searches and returns a list of manager requests that satisfy the given
     * criteria. In order to ignore some criteria insert null to the
     * corresponding parameter.
     *
     * @param salesmanUsername	salesman's username
     * @param managerUsername	managers's username
     * @param customerTaxID
     * @param requestedPhoneNumber
     * @param status	status of the request
     * @param phoneNumberCount
     * @param description	description of the request
     * @param strict	designates whether the list of criteria will be connected
     * with AND if true or with OR if false
     *
     * @return	a list of manager requests that satisfy the given criteria
     *
     * @throws	IllegalArgumentException if no search criteria are defined, (i.e
     * null is passed in all the parameters apart from the last).
     *
     * @throws java.sql.SQLException if database error occurs
     */
    public static ArrayList<ManagerRequest> searchManagerRequests(
            String salesmanUsername, String managerUsername, Long customerTaxID,
            Long requestedPhoneNumber, Integer phoneNumberCount, String status,
            String description, boolean strict)
            throws IllegalArgumentException, SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        String operator = strict ? "AND" : "OR";

        String whereList = "";
        ArrayList<ManagerRequest> managerRequests = new ArrayList<>();

        try {

            if (salesmanUsername != null) {
                whereList += " salesmanUsername=? " + operator;
            }
            if (managerUsername != null) {
                whereList += " managerUsername=? " + operator;
            }
            if (customerTaxID != null) {
                whereList += " customerTaxID=? " + operator;
            }
            if (requestedPhoneNumber != null) {
                whereList += " requestedPhoneNumber=? " + operator;
            }
            if (phoneNumberCount != null) {
                whereList += " phoneNumberCount=? " + operator;
            }
            if (status != null) {
                whereList += " status=? " + operator;
            }
            if (description != null) {
                whereList += " description=? " + operator;
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
            String query = "SELECT * FROM manager_requests WHERE" + whereList;

            System.err.println(query);

            prepStatement = connection.prepareStatement(query);

            if (salesmanUsername != null) {
                prepStatement.setString(++fieldCount, salesmanUsername);
            }
            if (managerUsername != null) {
                prepStatement.setString(++fieldCount, managerUsername);
            }
            if (customerTaxID != null) {
                prepStatement.setLong(++fieldCount, customerTaxID);
            }
            if (requestedPhoneNumber != null) {
                prepStatement.setLong(++fieldCount, requestedPhoneNumber);
            }
            if (phoneNumberCount != null) {
                prepStatement.setInt(++fieldCount, phoneNumberCount);
            }
            if (status != null) {
                prepStatement.setString(++fieldCount, status);
            }
            if (description != null) {
                prepStatement.setString(++fieldCount, description);
            }

            results = prepStatement.executeQuery();

            while (results.next()) {

                ManagerRequest managerRequest = new ManagerRequest();

                managerRequest.setRequestID(results.getLong("requestID"));
                managerRequest.setManagerUsername(results.getString("managerUsername"));
                managerRequest.setSalesmanUsername(results.getString("salesmanUsername"));
                managerRequest.setCustomerTaxID(results.getLong("customerTaxID"));
                managerRequest.setRequestedPhoneNumber(results.getLong("requestedPhoneNumber"));
                managerRequest.setPhoneNumberCount(results.getInt("phoneNumberCount"));
                managerRequest.setStatus(results.getString("status"));
                managerRequest.setDescription(results.getString("description"));

                managerRequests.add(managerRequest);
            }

            return managerRequests;

        } catch (SQLException e) {
            System.err.println(e);
            throw new SQLException("Database Error");
        } finally {
            release(results);
            release(prepStatement);
        }
    }

    /**
     * Updates a particular manager request from the database. Pass null to a
     * parameter to leave the corresponding field unchanged.
     *
     * @param requestID	ID of the manager request to be updated
     * @param salesmanUsername	salesman that sent the request
     * @param status	manager request's new status
     * @param customerTaxID	customer's ID who applied for a 7th or greater phone
     * number
     * @param managerUsername	username of the manager that handled the request
     * @param phoneNumberCount	requested phone number count
     * @param requestedPhoneNumber	phone number requested
     * @param description	description of the request
     *
     * @return {@code true} if manager request updated successfully, or
     * {@code false} if manager request to be updated does not exist
     * @throws java.sql.SQLException if a database error occurs
     */
    public static boolean updateManagerRequest(long requestID, String salesmanUsername,
            String managerUsername, Long customerTaxID, Long requestedPhoneNumber,
            Integer phoneNumberCount, String status, String description) throws SQLException, Exception {

        String updateQuery = "UPDATE manager_requests SET ";

        if (!checkConnection()) {
            throw new SQLException("Connection error to database");
        }

        PreparedStatement prepStatement = null;
        int updateCount = -1;

        String whereList = "";

        try {

            if (!managerRequestExists(requestID)) {
                throw new Exception("Request with id:" + requestID + " doesn't exist.");
            }

            if (salesmanUsername != null) {
                whereList += "salesmanUsername=?, ";
            }
            if (managerUsername != null) {
                whereList += "managerUsername=?, ";
            }
            if (customerTaxID != null) {
                whereList += "customerTaxID=?, ";
            }
            if (requestedPhoneNumber != null) {
                whereList += "requestedPhoneNumber=?, ";
            }
            if (phoneNumberCount != null) {
                whereList += "phoneNumberCount=?, ";
            }
            if (status != null) {
                whereList += "status=?, ";
            }
            if (description != null) {
                whereList += "description=?, ";
            }

            if (whereList.isEmpty()) {
                throw new IllegalArgumentException("No update criteria defined");
            }

            // remove the extra comma and space in the end of whereList string
            whereList = whereList.substring(0, whereList.length() - 1 - 1);

            int fieldCount = 0;

            updateQuery += whereList + " WHERE requestID=?";

            prepStatement = connection.prepareStatement(updateQuery);

            if (salesmanUsername != null) {
                prepStatement.setString(++fieldCount, salesmanUsername);
            }
            if (managerUsername != null) {
                prepStatement.setString(++fieldCount, managerUsername);
            }
            if (customerTaxID != null) {
                prepStatement.setLong(++fieldCount, customerTaxID);
            }
            if (requestedPhoneNumber != null) {
                prepStatement.setLong(++fieldCount, requestedPhoneNumber);
            }
            if (phoneNumberCount != null) {
                prepStatement.setInt(++fieldCount, phoneNumberCount);
            }
            if (status != null) {
                prepStatement.setString(++fieldCount, status);
            }
            if (description != null) {
                prepStatement.setString(++fieldCount, description);
            }

            prepStatement.setLong(++fieldCount, requestID);

            updateCount = prepStatement.executeUpdate();

            // If the updated customer count is 1, then update was successful.
            if (updateCount == 1) {
                return true;
            } else {
                throw new SQLException("More than one customers with the same tax ID");
            }

        } catch (SQLException e) {
            System.err.println(e);

            if (updateCount != -1) {
                throw e;
            }
            throw new SQLException("Database error");
        } finally {
            release(prepStatement);
        }
    }

    /**
     * Deletes a particular manager request from the database.
     *
     * @param requestID request ID of the manager request to be deleted
     *
     * @return {@code true} if manager request deleted successfully,
     * {@code false} if manager request to be deleted does not exist or if error
     * occurs.
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

    /**
     * Gets a list of all the transactions stored in the database.
     *
     * @return	list of transactions or null if error occurs.
     */
    public static ArrayList<Transaction> getTransactions() {

        if (!checkConnection()) {
            return null;
        }

        ArrayList<Transaction> transactions = new ArrayList<>();

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

    /**
     * Inserts a transaction to database
     *
     * @param transaction {@link models.Transaction} to be inserted
     *
     * @return {@code true} for successful insertion, {@code false} if error
     * occurred.
     *
     * @throws java.sql.SQLException if database error occurs
     */
    public static void addTransaction(Transaction transaction) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;

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

        } catch (SQLException e) {
            System.err.println(e);
            throw new SQLException("Database error");
        } finally {
            release(prepStatement);
        }
    }

    /**
     * Checks if there is a transaction with the given transaction ID.
     *
     * @param transactionID transaction ID of the transaction to be searched
     *
     * @return	{@code true} if transaction exists, {@code false}, if transaction
     * does not exist or error occurs.
     */
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

    /**
     * Searches a transaction with the given transaction ID.
     *
     * @param	transactionID transaction ID of the transaction to be searched
     * @return	the {@link models.Transaction} object initialized from data
     * fetched from the database if the transaction exists, or {@code null} if
     * the transaction does not exist or error occurs.
     */
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
     * Deletes a particular transaction from the database.
     *
     * @param transactionID	transaction ID of the transaction to be deleted
     *
     * @return {@code true} if transaction deleted successfully, {@code false}
     * if transaction to be deleted does not exist or if error occurs.
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

    /**
     * Gets a list of all the salesmen stored in the users table in the
     * database.
     *
     * @return	a list of salesmen or null if error occurs.
     */
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

    /**
     * Gets a list of all the managers stored in the users table in the
     * database.
     *
     * @return	a list of managers or null if error occurs.
     */
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

    /**
     * Gets a list of all the customers registered online stored in the
     * database.
     *
     * @return	list of {@link models.CustomerOnline} or null if error occurs.
     */
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

    /**
     * Inserts an online customer to database
     *
     * @param customerOnline online customer to be inserted
     *
     * @return {@code true} for successful insertion, {@code false} if error
     * occurred.
     */
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

    /**
     * Checks if there is a online customer with the given username
     *
     * @param username String representing the online customer's username to be
     * searched
     *
     * @return	{@code true} if online customer exist, {@code false}, if online
     * customer does not exists or error occurs.
     */
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

    /**
     * Checks if there is a online customer with the given taxID
     *
     * @param taxID taxID of the online customer to be searched
     *
     * @return	{@code true} if online customer exists, {@code false}, if online
     * customer does not exist or error occurs.
     */
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

    /**
     * Searches a online customer with the given taxID.
     *
     * @param	taxID tax ID of online customer to be searched
     *
     * @return	the online customer object initialized from data fetch from the
     * database if the online customer exists, or {@code null} if the online
     * customer does not exist or error occurs.
     */
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

    /**
     * Searches a online customer with the given username.
     *
     * @param	username username of online customer to be searched
     *
     * @return	the online customer object initialized from data fetch from the
     * database if the online customer exists, or {@code null} if the online
     * customer does not exist
     * @throws java.sql.SQLException	if database error occurs
     *
     */
    public static CustomerOnline searchCustomerOnline(String username) throws SQLException {
        
        
        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "SELECT * FROM customers c "
                    + "JOIN customers_online co "
                    + "ON c.taxID=co.taxID "
                    + "WHERE co.username=?";

            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, username);

            results = prepStatement.executeQuery();

            if (results.next()) {

                CustomerOnline customerOnline = new CustomerOnline();

                customerOnline.setUsername(results.getString("username"));
                customerOnline.setPassword("****");
                //customerOnline.setPassword(results.getString("password"));
                customerOnline.setTaxID(results.getLong("taxID"));
                customerOnline.setRole(results.getString("role"));
                customerOnline.setRegDate(results.getString("regDate"));
                customerOnline.setFirstname(results.getString("firstname"));
                customerOnline.setLastname(results.getString("lastname"));
                customerOnline.setBirthDate(results.getDate("birthDate").toString());
                customerOnline.setGender(results.getString("gender").charAt(0));
                customerOnline.setFamilyStatus(results.getString("familyStatus"));
                customerOnline.setHomeAddress(results.getString("familyStatus"));
                customerOnline.setBankAccountNo(results.getLong("bankAccountNo"));
                customerOnline.setPersonalCode(results.getString("personalCode"));
                customerOnline.setRelateTaxID(results.getLong("relateTaxID"));

                return customerOnline;
            }

        } catch (SQLException e) {
            System.err.println(e);
            throw new SQLException("Database Error");
        } finally {
            release(results);
            release(prepStatement);
        }
        return null;
    }

    /**
     * Deletes a particular online customer from the database.
     *
     * @param taxID tax ID of the online customer to be deleted.
     *
     * @return {@code true} if online customer deleted successfully,
     * {@code false} if online customer to be deleted does not exist or if error
     * occurs.
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
     * Deletes a particular online customer from the database.
     *
     * @param username username of the online customer to be deleted.
     *
     * @return {@code true} if online customer deleted successfully,
     * {@code false} if online customer to be deleted does not exist or if error
     * occurs.
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

    public static boolean AddEmailToNewsletter(String email) throws SQLException {

        if (!checkConnection()) {
            throw new SQLException("Database connection error");
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            String query = "INSERT INTO newsletter (email) VALUES (?)";

            /* Doesn't needed because email is primary key in newsletter table */
            /* if ( emailExists(email) ) return false; */
            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, email);
            prepStatement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println(e);
            throw new SQLException("Database error");
        } finally {
            release(results);
            release(prepStatement);
        }
    }

    public static boolean authenticateCustomer(String username, String password) {

        if (!checkConnection()) {
            return false;
        }

        PreparedStatement prepStatement = null;
        ResultSet results = null;

        try {

            prepStatement = connection.prepareStatement("SELECT username FROM customers_online WHERE username=? AND password=?");
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);

            results = prepStatement.executeQuery();

            while (results.next()) {
                /* requested username is the same with the username of result */
                if (results.getString("username").equals(username)) {
                    return true;
                }
            }

            return false;

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            release(results);
            release(prepStatement);
        }

        return false;
    }

    public static void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Checks the connection to the database and creates a new one if it does
     * not exist.
     *
     * @return	{@link true} connection is established, or {@code false} if error
     * occurred.
     */
    private static boolean checkConnection() {
        Database.connect();
        if (isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Closes a resource
     *
     * @param resource	resource to be closed. Must implement interface:
     * {@link AutoCloseable}
     *
     */
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
