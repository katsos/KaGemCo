/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.internet;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Account;
import models.CustomerOnline;
import utils.JsonUtils;

/**
 *
 * @author dsupport
 */
public class GetCustomer extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
			String errorMessage = null;
		
		// JSON Output Initialization
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JsonWriter jsonWriter = Json.createWriter(out);
		
		// Parameter Initalization //
		
		String username = request.getParameter("username");
		if (username == null) {
			errorMessage = "Customer username undefined";
			JsonUtils.outputJsonError(errorMessage, jsonWriter);
			return;
		}
		
		
		CustomerOnline customerOnline;
		
		try {
			customerOnline = Database.searchCustomerOnline(username);
		} catch (SQLException ex) {
			JsonUtils.outputJsonError(ex.getMessage(), jsonWriter);
			return;
		} 
		
		// Check if customer online exists
		if (customerOnline == null) {
			JsonUtils.outputJsonError("Customer with username: " +
				username + " does not exist", jsonWriter);
			return;
		}
		
		ArrayList<Account> accounts;
		try {
			accounts = Database.searchAccounts(customerOnline.getTaxID());
		} catch (SQLException ex) {
			JsonUtils.outputJsonError(ex.getMessage(), jsonWriter);
			return;
		}
		
		// JSON Build //
		
		JsonObject customerObj = Json.createObjectBuilder()
			.add("username", customerOnline.getUsername())
			.add("role", customerOnline.getRole())
			.add("regDate", customerOnline.getRegDate())
			.add("taxID", customerOnline.getTaxID())
			.add("firstname", customerOnline.getFirstname())
			.add("lastname", customerOnline.getLastname())
			.add("birthDate", customerOnline.getBirthDate())
			.add("gender", customerOnline.getGender())
			.add("familyStatus", customerOnline.getFamilyStatus())
			.add("homeAddress", customerOnline.getHomeAddress())
			.add("bankAccountNo", customerOnline.getBankAccountNo())
			.add("personalCode", customerOnline.getPersonalCode())
			.add("relateTaxID", customerOnline.getRelateTaxID())
			.build();
		
		JsonArrayBuilder accountsBuilder = Json.createArrayBuilder();
		
		for (Account account : accounts) {
			accountsBuilder.add( Json.createObjectBuilder()
                                                .add("phoneNumber", account.getPhoneNumber())
                                                .add("balance", account.getBalance())
			);
		}
		
		JsonArray accountsArray = accountsBuilder.build();
		
		
		JsonObject jsonObj = Json.createObjectBuilder()
			.add("customer", customerObj)
			.add("accounts", accountsArray)
			.build();
		
		
		jsonWriter.writeObject(jsonObj);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
