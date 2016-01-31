/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin;

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
@WebServlet(name = "UpdateUser", urlPatterns = {"/UpdateUser"})
public class UpdateUser extends HttpServlet {

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
		
		String username = request.getParameter("username");
		if (username == null) {
			errorMessage += "Username of the user to be updated must be defined"
				+ JsonUtils.ERR_DLM;
		}
		String password = request.getParameter("password");
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String role = request.getParameter("role");
		
		if (role != null) {
			if ( !role.equals("salesman") && !role.equals("manager")) {
				errorMessage += "Role must either be 'salesman' or 'manager'" 
					+ JsonUtils.ERR_DLM;
			}
		}
		
		
		
		// Output error message as JSON
		if (!errorMessage.isEmpty()) {
			errorMessage = errorMessage.substring(0, errorMessage.length() -2);
			JsonUtils.outputJsonError(errorMessage, jsonWriter);
			return;
		}
		
		try {
			Database.updateUser(username, password, firstname, lastname, role);
		} catch (IllegalArgumentException | SQLException e) {
			JsonUtils.outputJsonError(e.getMessage(), jsonWriter);
			return;
		} 
		
		
		JsonObject successObj = 
			Json.createObjectBuilder().add("success", "success").build();
		
		
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
