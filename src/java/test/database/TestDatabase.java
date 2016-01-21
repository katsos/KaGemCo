/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.database;

import database.Database;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import models.Account;
import models.Customer;
import models.Log;
import models.ManagerRequest;
import models.Transaction;
import models.User;

/**
 *
 * @author pgmank
 */
public class TestDatabase {
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            testAdvancedCustomerSearch();
	}
        
        private static void basicTest() {
            
            // Test getters
		
            System.out.println("Accounts");
            printList(Database.getAccounts());

            System.out.println("\nCustomers");
            printList(Database.getCustomers());

            System.out.println("\nLogs");
            printList(Database.getLogs());

            System.out.println("\nManagerRequests");
            printList(Database.getManagerRequests());

            System.out.println("\nManagers");
            printList(Database.getManagers());

            System.out.println("\nSalesmen");
            printList(Database.getSalesmen());

            System.out.println("\nTransactions");
            printList(Database.getTransactions());

            System.out.println("\nUsers");
            printList(Database.getUsers());

            // User //

            // Search User

            System.out.println("\n\nSearch User");

            System.out.println("Existent user");
            System.out.println(Database.searchUser("admin"));

            System.out.println("Unexistent User");
            System.out.println(Database.searchUser("UnkownSoldier"));

            // Add User

            System.out.println("\n\nAdd User");
            Database.addUser(new User("Neo", "Psari", "newfish", "passwood", "admin"));
            System.out.println(Database.searchUser("newfish") + " added to db.");

            // Delete User

            System.out.println("\n\nDelete User");
            Database.deleteUser("newfish");
            System.out.println("User 'newfish' deleted from db.");

            // Customer //

            // Search Customer

            System.out.println("\n\nSearch Customer");

            System.out.println("Existent Customer");
            System.out.println(Database.searchCustomer(1234123423L));

            System.out.println("Unexistent Customer");
            System.out.println(Database.searchCustomer(1111111111L));

            // Add Customer

            Customer customer = new Customer("George", "Clooney", (new Date(new java.util.Date().getTime()).toString()),
                    'M', "Divorced", "Route 66", 1234123412L, 1000000000L, "pcode", 1234123412L);

            System.out.println("\n\nAdd Customer");
            Database.addCustomer(customer);
            System.out.println(Database.searchCustomer(1234123412L) + " added to db.");

            // Delete Customer

            System.out.println("\n\nDelete User");
            Database.deleteCustomer(1234123412L);
            System.out.println("Customer with tax ID: '1234123412' deleted from db.");

            // Account //

            // Search Account

            System.out.println("\n\nSearch Account");

            System.out.println("Existent Account");
            System.out.println(Database.searchAccount(6969696969L));

            System.out.println("Unexistent Account");
            System.out.println(Database.searchAccount(1111111111L));

            // Add Account

            System.out.println("\n\nAdd Account");
            Database.addAccount(new Account(6901647264L, 1234123423, 20));
            System.out.println(Database.searchAccount(6901647264L) + " added to db.");

            // Delete Account

            System.out.println("\n\nDelete Account");
            Database.deleteAccount(6901647264L);
            System.out.println("Account '6901647264' deleted from db.");

            // Log //

            // Search Log

            System.out.println("\n\nSearch Log");

            System.out.println("Existent Log");
            System.out.println(Database.searchLog(2));

            System.out.println("Unexistent Log");
            System.out.println(Database.searchLog(100000));

            // Add Log

            System.out.println("\n\nAdd Log");
            Database.addLog(new Log("admin", "ksinw ta aftia mou"));
            System.out.println(Database.searchLog(12) + " added to db.");

            // ManagerRequest //

            // Search ManagerRequest

            System.out.println("\n\nSearch ManagerRequest");

            System.out.println("Existent ManagerRequest");
            System.out.println(Database.searchManagerRequest(2));

            System.out.println("Unexistent ManagerRequest");
            System.out.println(Database.searchManagerRequest(111111));

            // Add ManagerRequest

            System.out.println("\n\nAdd ManagerRequest");
            Database.addManagerRequest(new ManagerRequest("nikosS", "manag", "rejected", "I want a promotion"));
            System.out.println(Database.searchManagerRequest(3) + " added to db.");

            // Delete ManagerRequest

            System.out.println("\n\nDelete ManagerRequest");
            Database.deleteManagerRequest(7);
            System.out.println("ManagerRequest '3' deleted from db.");

            // Transaction //

            // Search Transaction

            System.out.println("\n\nSearch Transaction");

            System.out.println("Existent Transaction");
            System.out.println(Database.searchTransaction(2));

            System.out.println("Unexistent Transaction");
            System.out.println(Database.searchTransaction(111111));

            // Add Transaction

            System.out.println("\n\nAdd Transaction");
            Database.addTransaction(new Transaction(6911112222L, 50));
            System.out.println(Database.searchTransaction(4) + " added to db.");

            // Delete Transaction

            System.out.println("\n\nDelete Transaction");
            Database.deleteTransaction(4);
            System.out.println("Transaction '3' deleted from db.");
        }
        
        private static void testAdvancedCustomerSearch() {
            ArrayList<Customer> customers = 
                    Database.searchCustomer("Anna", null, null, 
                            null, null, null, null, null, null, true);
            
            if (customers.isEmpty()) {
                System.out.println("No customers inside");
            }
            
            
            printList(customers);
        }
	
	private static void printList(List list) {
		for (Object item : list) {
			System.out.println(item.toString());
		}
	}
	
}
     