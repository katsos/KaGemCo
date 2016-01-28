/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import enums.Access;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServletRequest;

/**
 * Contains methods for reporting errors or logs to the client or server
 * respectively.
 * 
 * @author pgmank
 */
public class Report {
	
	/** JSON writer object where to write the JSON structured reports */
	private final JsonWriter jsonWriter;
	/** The request object of the servlet that produces the report */
	private final HttpServletRequest request;
	
	/**
	 * Simple constructor that this object's writer and request to the calling 
	 * servlet's writer and request.
	 * objects 
	 * @param jsonWriter
	 * @param request 
	 */
	public Report(JsonWriter jsonWriter, HttpServletRequest request) {
		this.jsonWriter = jsonWriter;
		this.request = request;
	}
	
	/**
	 * Reports access error description as JSON. To do that, it checks if the
	 * given role matches the current role (using Cookies). If not, a report is
	 * output. If roles match, nothing is output.
	 * 
	 * @param role role to be tested
	 * @return 
	 */
	public boolean jsonAccess(String role) {
		Access access = WebUtils.checkAccess(role, request);
		if (access != Access.GRANTED) {
			jsonError(WebUtils.getAccessDescription(access, role));
			return true;
		}
		return false;
	}
	
	/**
	 * Reports the given error message as JSON.
	 * 
	 * @param errorMessage Error message to be output.
	 */
	public void jsonError(String errorMessage) {
		JsonUtils.outputJsonError(errorMessage, jsonWriter);
	}
		
}

/**
 * TODO: Change Report class structure by making constructor receive request
 * and reponse of the servlet and making PrintWriter and JsonWriter by its own.
 * Make this class abstract and create two childs JsonReport and LogReport for
 * the two type of reports.
 * 
 * Tradeoff
 * if this object initializes the PrintWriter or JsonWriter, then the
 * servlet cannot output anything and will depend on the reported object on
 * outputing.
 */