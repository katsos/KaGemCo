/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;
import utils.JsonUtils;

/**
 *
 * @author pgmank
 */
@WebServlet(name = "GetUser", urlPatterns = {"/GetUser"})
public class GetUser extends HttpServlet {

	/**
	 * Gets a user given his username from the database.
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
		JsonObject userObj;
		String username = request.getParameter("username");
		
		if (username == null) {
			userObj = Json.createObjectBuilder().
				add("error", "username search parameter not defined").build();
			jsonWriter.writeObject(userObj);
			return;
		}
		
		User user = null;
		
		// Search user with the given name. If error occures, write error message to JSON
		try {
			user = Database.searchUser(username);
		} catch (IllegalArgumentException | SQLException e) {
			JsonUtils.outputJsonError(e.getMessage(), jsonWriter);
			return;
		} 
		
		// User found
		if (user != null) {
			userObj = Json.createObjectBuilder()
				.add("username", username)
				.add("firstname", user.getFirstname())
				.add("lastname", user.getLastname())
				.add("role", user.getRole())
				.add("date", user.getDate())
				.build();
		} else {
			userObj = Json.createObjectBuilder().
				add("error", "User with username: " + username + " does not exist").build();
		}
		
		jsonWriter.writeObject(userObj);
		
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
