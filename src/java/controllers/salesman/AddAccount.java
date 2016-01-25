/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.salesman;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.json.Json;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Account;
import utils.JsonUtils;

/**
 * Adds a new account given the phone number, the owner's tax ID and the initial
 * balance. All those parameters are passed via the URL and are obligatory. If
 * at least one is missing, an error report is returned as a response.
 * 
 * @author pgmank
 */
@WebServlet(name = "AddAccount", urlPatterns = {"/AddAccount"})
public class AddAccount extends HttpServlet {

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
		
		String errorMessage = "";
		
		// JSON Output Initialization
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JsonWriter jsonWriter = Json.createWriter(out);
		
		String ownerTaxIDParam = request.getParameter("ownerTaxID");
		Long ownerTaxID = null;
		if (ownerTaxIDParam != null) {
			try {
				ownerTaxID = Long.valueOf(ownerTaxIDParam);
			} catch (NumberFormatException e) {
				errorMessage += "Tax ID must be an integer number" +
					JsonUtils.ERR_DLM;
				ownerTaxID = -1L;	// dummie value to flag invalid ownerTaxID
			}
		}
		
		String phoneNumberParam = request.getParameter("phoneNumber");
		Long phoneNumber = null;
		if (phoneNumberParam != null) {
			try {
				phoneNumber = Long.valueOf(phoneNumberParam);
			} catch (NumberFormatException e) {
				errorMessage += "Phone number must be an integer number" +
					JsonUtils.ERR_DLM;
				phoneNumber = -1L;	// dummie value to flag invalid phoneNumber
			}
		}
		
		String balanceParam = request.getParameter("balance");
		Double balance = null;
		if (balanceParam != null) {
			try {
				balance = Double.valueOf(balanceParam);
			} catch (NumberFormatException e) {
				errorMessage += "Balance must be a decimal number" +
					JsonUtils.ERR_DLM;
				balance = -1D ;	// dummie value to flag invalid balance
			}
		}
		
		// Construct error message describing the undefined parameters
		
		String undefinedParameters = "";
		if (ownerTaxID == null) {
			undefinedParameters += "ownerTaxID, ";
		}
		if (phoneNumber == null) {
			undefinedParameters += "phoneNumber, ";
		}
		if (balance == null) {
			undefinedParameters += "balance, ";
		}
		
		// Check for undefined Parameters
		if (!undefinedParameters.isEmpty()) {
			// Remove extra space and comma from the end of the string
			undefinedParameters = undefinedParameters.substring(0, undefinedParameters.length() -2);
			errorMessage += "Parameters: " + undefinedParameters + " are undefined";
			JsonUtils.outputJsonError(errorMessage, jsonWriter);
			return;
		} else if (!errorMessage.isEmpty()) {
			// remove the extra delimeter from the end of the string
			errorMessage = errorMessage.substring(0, errorMessage.length() -2);
			JsonUtils.outputJsonError(errorMessage, jsonWriter);
			return;
		}
		
		if (balance < 10) {
			JsonUtils.outputJsonError("Initial pre-paid amount must be 10 euros or more", jsonWriter);
			return;
		}
		
		try {
			int count = Database.getCustomerAccountCount(ownerTaxID);
			if (count >= 6) {
				
				/** 
				 * TODO:
				 * Check if request has been sent for 7th or more phone number
				 * Must redesign database to add fields applicantsTaxID and
				 * applicantsPhoneNumber.
				 */ 
				
				JsonUtils.outputJsonError("Customer already has " + count +
					"accounts. In order to add a 7th phone number, please"
					+ "send a request to the manager", jsonWriter);
			}
		} catch (SQLException ex) {
			JsonUtils.outputJsonError(ex.getMessage(), jsonWriter);
		}
		
		try {
			// Check if customer to whom the phone number will be added exists
			if(!Database.customerExists(ownerTaxID)) {
				errorMessage = "Customer with tax ID: " + ownerTaxID + " does not exist";
				JsonUtils.outputJsonError(errorMessage, jsonWriter);
				return;
			}
		} catch (SQLException ex) {
			JsonUtils.outputJsonError(ex.getMessage(), jsonWriter);
		}
		
		Account account = new Account(phoneNumber, ownerTaxID, balance);
		
		boolean success;
		try {
			success = Database.addAccount(account);
		} catch (SQLException ex) {
			JsonUtils.outputJsonError(ex.getMessage(), jsonWriter);
			return;
		}
		
		if (success) {
			jsonWriter.writeObject(Json.createObjectBuilder()
				.add("success", "success").build());
		} else {
			JsonUtils.outputJsonError("Phone number: " + phoneNumber + " already exists", jsonWriter);
		}
		
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
