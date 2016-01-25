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
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Customer;
import utils.JsonUtils;

/**
 * Adds a new customer given all the Customer entity fields as URL parameters.
 * For a successful registration, all fields must be passed. Otherwise an error
 * report will be output in the JSON describing the undefined parameters.
 * @author pgmank
 */
@WebServlet(name = "RegisterCustomer", urlPatterns = {"/RegisterCustomer"})
public class RegisterCustomer extends HttpServlet {

	/**
	 * Adds a customer to the database given the information taken from the URL
	 * parameters. In order for a customer to be registered, all fields must be
	 * passed as URL parameter fields. Otherwise, an error is returned which 
	 * message describes the undefined parameters.
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
		
		// Parameter Initalization //
		
		String taxIDParam = request.getParameter("taxID");
		Long taxID = null;
		if (taxIDParam != null) {
			try {
				taxID = Long.valueOf(taxIDParam);
			} catch (NumberFormatException e) {
				errorMessage += "Tax ID must be an integer number" + 
					JsonUtils.ERR_DLM;
				taxID = -1L;	// dummie value to flag invalid taxID
			}
		}
		
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String birthDate = request.getParameter("birthDate");
		Character gender = null;
			String genderParam = request.getParameter("gender");
			if(genderParam != null) {
				gender = genderParam.charAt(0);
			}
		String familyStatus = request.getParameter("familyStatus");
		String homeAddress = request.getParameter("homeAddress");
		String bankAccountNoParam = request.getParameter("bankAccountNo");
		Long bankAccountNo = null;
		if(bankAccountNoParam != null) {
			try {
				bankAccountNo = Long.valueOf(bankAccountNoParam);
			} catch (NumberFormatException e) {
				errorMessage += "Bank Account number must be made of numbers" +
					JsonUtils.ERR_DLM ;
				bankAccountNo = -1L;	// dummie value to flag invalid bankAccountNo
			}
		}
		String personalCode = request.getParameter("personalCode");
		Long relateTaxID = null;
		String relateTaxIDParam = request.getParameter("relateTaxID");
		if (relateTaxIDParam != null) {
			try {
				relateTaxID = Long.valueOf(relateTaxIDParam);
			} catch (NumberFormatException e) {
				errorMessage += "Relative Tax ID must be an integer number" +
					JsonUtils.ERR_DLM;
				relateTaxID = -1L;	// dummie value to flag invalid relateTaxID
			}
			
		}
		
		String undefinedParameters = "";
		
		// Construct error message so that it includes the undefined parameters
		if (taxID == null) {
			undefinedParameters += "taxID, ";
		}
		if (firstname == null) {
			undefinedParameters += "firstname, ";
		}
		if (lastname == null) {
			undefinedParameters += "lastname, ";
		}
		if (birthDate == null) {
			undefinedParameters += "birthDate, ";
		}
		if (gender == null) {
			undefinedParameters += "gender, ";
		}
		if (familyStatus == null) {
			undefinedParameters += "familyStatus, ";
		}
		if (homeAddress == null) {
			undefinedParameters += "homeAddress, ";
		}
		if (bankAccountNo == null) {
			undefinedParameters += "bankAccountNo, ";
		}
		if (personalCode == null) {
			undefinedParameters += "personalCode, ";
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
		
		boolean success;
		
		try {
			success = Database.addCustomer(new Customer(firstname, lastname, birthDate, gender, familyStatus, homeAddress, taxID, bankAccountNo, personalCode, relateTaxID));
		} catch (SQLException e) {
			JsonUtils.outputJsonError(e.getMessage(), jsonWriter);
			return;
		} 
		
		
		JsonObject successObj;
		
		if (success) {
			successObj = Json.createObjectBuilder().add("success", "success").build();
		} else {
			successObj = Json.createObjectBuilder().
				add("error", "Customer with tax ID: " + taxID + " already exists").build();
		}
		
		jsonWriter.writeObject(successObj);
		
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
