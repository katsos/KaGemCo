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
import utils.JsonUtils;

/**
 *
 * @author pgmank
 */
@WebServlet(name = "modifyCustomer", urlPatterns = {"/modifyCustomer"})
public class ModifyCustomer extends HttpServlet {

	/**
	 * Updates a particular customer given his taxID and the information to be
	 * updated as URL parameters. Feedback is given to the client as JSON, where
	 * if error field is defined means that update failed and if success field
	 * is defined means that update succeeded. The error field stores the error
	 * message.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		String errorMessage = null;
		
		response.setContentType("application/json;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		JsonWriter jsonWriter = Json.createWriter(out);
		
		String taxIDParam = request.getParameter("taxID");
		if (taxIDParam == null) {
			errorMessage = "Tax ID of customer to be updated must be defined";
			JsonUtils.outputJsonError(errorMessage, jsonWriter);
			return;
		}
		
		Long taxID;
		try {
			taxID = Long.valueOf(taxIDParam);
		} catch (NumberFormatException e) {
			errorMessage = "Tax ID must be an integer number";
			JsonUtils.outputJsonError(errorMessage, jsonWriter);
			return;
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
				errorMessage = "Bank Account number must be made of numbers";
				JsonUtils.outputJsonError(errorMessage, jsonWriter);
				return;
			}
		}
		String personalCode = request.getParameter("personalCode");
		Long relateTaxID = null;
		String relateTaxIDParam = request.getParameter("relateTaxIDParam");
		if (relateTaxIDParam != null) {
			try {
				relateTaxID = Long.valueOf(relateTaxIDParam);
			} catch (NumberFormatException e) {
				errorMessage = "Relative Tax ID must be an integer number";
				JsonUtils.outputJsonError(errorMessage, jsonWriter);
				return;
			}
			
		}
		
		boolean success;
		
		try {
			success = Database.updateCustomer(taxID, firstname, lastname, birthDate, gender, familyStatus, homeAddress, bankAccountNo, personalCode, relateTaxID);
		} catch (IllegalArgumentException | SQLException e) {
			JsonUtils.outputJsonError(e.getMessage(), jsonWriter);
			return;
		} 
		
		
		JsonObject successObj;
		
		if (success) {
			successObj = Json.createObjectBuilder().add("success", "success").build();
		} else {
			successObj = Json.createObjectBuilder().
				add("error", "Customer with tax ID: " + taxID + " does not exist").build();
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
