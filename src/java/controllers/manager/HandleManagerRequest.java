/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.manager;

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
 * Changes the status of the manager request to either accepted or rejected,
 * given two obligatory URL parameters: requestID and status. If at least one
 * of those parameters are not passed, then an error report is returned as a
 * response, describing the undefined parameters.
 * 
 * @author pgmank
 */
@WebServlet(name = "HandleManagerRequest", urlPatterns = {"/HandleManagerRequest"})
public class HandleManagerRequest extends HttpServlet {

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
		
		String requestIDParam = request.getParameter("requestID");
		Long requestID = null;
		if (requestIDParam != null) {
			try {
				requestID = Long.valueOf(requestIDParam);
			} catch (NumberFormatException e) {
				errorMessage += "Manager request ID must be an integer number" +
					JsonUtils.ERR_DLM;
				requestID = -1L;	// dummie value to flag invalid relateTaxID
			}
		}
		
		String status = request.getParameter("status");
		
		// Undefined Parameters initialization
		String undefinedParameters = "";
		if (requestID == null) {
			undefinedParameters += "requestID, ";
		}
		if (status == null) {
			undefinedParameters += "status, ";
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
			success = Database.updateManagerRequest(requestID, status);
		} catch (IllegalArgumentException | SQLException e) {
			JsonUtils.outputJsonError(e.getMessage(), jsonWriter);
			return;
		} 
		
		
		JsonObject successObj;
		
		if (success) {
			successObj = Json.createObjectBuilder().add("success", "success").build();
		} else {
			successObj = Json.createObjectBuilder().
				add("error", "Manager request with ID: " + requestID + " does not exist").build();
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
