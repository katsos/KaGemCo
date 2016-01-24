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
import models.ManagerRequest;
import utils.JsonUtils;

/**
 *
 * @author pgmank
 */
@WebServlet(name = "RegisterCustomer", urlPatterns = {"/RegisterCustomer"})
public class RegisterCustomer extends HttpServlet {

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
		
		// Parameter Initalization //
		
		String taxIDParam = request.getParameter("taxID");
		Long taxID = null;
		if (taxIDParam != null) {
			try {
				taxID = Long.valueOf(taxIDParam);
			} catch (NumberFormatException e) {
				errorMessage += "Tax ID must be an integer number::";
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
				errorMessage += "Bank Account number must be made of numbers::";
			}
		}
		String personalCode = request.getParameter("personalCode");
		Long relateTaxID = null;
		String relateTaxIDParam = request.getParameter("relateTaxID");
		if (relateTaxIDParam != null) {
			try {
				relateTaxID = Long.valueOf(relateTaxIDParam);
			} catch (NumberFormatException e) {
				errorMessage += "Relative Tax ID must be an integer number::";
			}
			
		}
		
		// Construct error message so that it includes the undefined parameters
		if (taxID == null) {
			errorMessage += "taxID, ";
		}
		if (firstname == null) {
			errorMessage += "firstname, ";
		}
		if (lastname == null) {
			errorMessage += "lastname, ";
		}
		if (birthDate == null) {
			errorMessage += "birthDate, ";
		}
		if (gender == null) {
			errorMessage += "gender, ";
		}
		if (familyStatus == null) {
			errorMessage += "familyStatus, ";
		}
		if (homeAddress == null) {
			errorMessage += "homeAddress, ";
		}
		if (bankAccountNo == null) {
			errorMessage += "bankAccountNo, ";
		}
		if (personalCode == null) {
			errorMessage += "personalCode, ";
		}
		
		// Output error message as JSON
		if (!errorMessage.isEmpty()) {
			// Remove the extra comma and space from the end of the string
			errorMessage = errorMessage.substring(0, errorMessage.length() -2);
			errorMessage = "Parameters: " + errorMessage + " are undefined";
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
