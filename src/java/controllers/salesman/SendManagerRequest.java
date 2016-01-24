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
import models.ManagerRequest;
import utils.JsonUtils;

/**
 *
 * @author pgmank
 */
@WebServlet(name = "sendManagerRequest", urlPatterns = {"/sendManagerRequest"})
public class SendManagerRequest extends HttpServlet {

	/**
	 * Constructs a ManagerRequest from the given URL parameters and adds it to
	 * manager_requests table in the database. Parameters salesmanUsername,
	 * managerUsername and description are obligatory. If at least one of those
	 * parameters are not defined, error occurs. In addition, the status field 
	 * value is set to pending by default.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		String errorMessage = "";
		
		response.setContentType("application/json;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		JsonWriter jsonWriter = Json.createWriter(out);
	
		String salesmanUsername = request.getParameter("salesmanUsername");
		String managerUsername = request.getParameter("managerUsername");
		String description = request.getParameter("description");
		
		if (salesmanUsername == null) {
			errorMessage += "salesmanUsername, ";
		}
		if (managerUsername == null) {
			errorMessage += "managerUsername, ";
		}
		if (description == null) {
			errorMessage += "description, ";
		}
		
		// Obligatory parameters where ignored. Send error message informing
		// which parameters where not defined
		if (!errorMessage.isEmpty()) {
			
			// Remove extra comma and space from the end of the string
			errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
			errorMessage = "Parameters: " + errorMessage + " are undefined.";
			JsonUtils.outputJsonError(errorMessage, jsonWriter);
			return;
		}
		
		ManagerRequest managerRequest = new ManagerRequest(salesmanUsername, managerUsername, "pending", description);
		
		try {
			Database.addManagerRequest(managerRequest);
		} catch (IllegalArgumentException | SQLException e) {
			JsonUtils.outputJsonError(e.getMessage(), jsonWriter);
			return;
		}
		
		JsonObject successObj;
		
		successObj = Json.createObjectBuilder().add("success", "success").build();
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
