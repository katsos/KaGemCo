/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 * Utility methods for better management of JSON related functions.
 * 
 * @author pgmank
 */
public class JsonUtils {
	
	/** Delimeter for separating error messages inside the JSON error field */
	public static final String ERR_DLM = "::";
	public static final String ERR_MSG = "error";
	public static final String INF_MSG = "info";
	
	/**
	 * Outputs an error JSON object with the given message to the given writer.
	 * The assumption is that the error object has only one field: "error", that
	 * contains the error message.
	 * 
	 * @param errorMessage	Error message to be output
	 * @param jsonWriter	JsonWriter object to write JSON object.
	 */
	public static void outputJsonError(String errorMessage, JsonWriter jsonWriter) {
		outputJsonMessage(ERR_MSG, errorMessage, jsonWriter);
	}
	
	/**
	 * Outputs an info JSON object with the given message to the given writer.
	 * The assumption is that the info object has only one field: "info", that
	 * contains the info message.
	 * 
	 * @param infoMessage	Error message to be output
	 * @param jsonWriter	JsonWriter object to write JSON object.
	 */
	public static void outputJsonInfo(String infoMessage, JsonWriter jsonWriter) {
		outputJsonMessage(INF_MSG, infoMessage, jsonWriter);
	}
	
	/**
	 * Outputs a one field JSON object with the given title and message to 
	 * the given writer. The title is the object's only field and the message
	 * is its value.
	 * 
	 * @param title
	 * @param message	Error message to be output
	 * @param jsonWriter	JsonWriter object to write JSON object.
	 */
	public static void outputJsonMessage(String title, String message, JsonWriter jsonWriter) {

		JsonObject errorObj = Json.createObjectBuilder().
			add(title, message).build();

		// Write json contents to web page
		jsonWriter.writeObject(errorObj);
	}
}
