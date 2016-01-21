/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.ManagerRequest;

public class GetManagerRequests extends HttpServlet {

    /**
     * Return a JSON file containing an array of manager requests. Example:
	 * 
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		ArrayList<ManagerRequest> managerRequests;
        managerRequests = Database.getManagerRequests();
        
        response.setContentType("application/json;charset=UTF-8");
        
		PrintWriter out = response.getWriter();
			
		JsonArrayBuilder rootArray = Json.createArrayBuilder();

		for (ManagerRequest managerRequest : managerRequests) {

			// Object that holds one manager request's information
			JsonObjectBuilder mrequestObj = Json.createObjectBuilder();

			// Fill object with manager request data
			mrequestObj
				.add("requestID", managerRequest.getRequestID())
				.add("salesmanUsername", managerRequest.getSalesmanUsername())
				.add("managerUsername", managerRequest.getManagerUsername())
				.add("status", managerRequest.getStatus())
				.add("description", managerRequest.getDescription());

			// Add manager request json object to root array
			rootArray.add(mrequestObj);
		}

		JsonArray jsonArray = rootArray.build();

		// Attach jsonWriter to out PrintWriter
		JsonWriter jsonWriter = Json.createWriter(out);
		
		// Write json contents to web page
		jsonWriter.writeArray(jsonArray);
        
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
