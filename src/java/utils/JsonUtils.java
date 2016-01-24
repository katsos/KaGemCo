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
	
	/**
	 * Outputs an error JSON object with the given message to the given writer.
	 * The assumption is that the error object has only one field: "error", that
	 * contains the error message.
	 * 
	 * @param errorMessage	Error message to be output
	 * @param jsonWriter	JsonWriter object to write JSON object.
	 */
	public static void outputJsonError(String errorMessage, JsonWriter jsonWriter) {

		JsonObject errorObj = Json.createObjectBuilder().
			add("error", errorMessage).build();

		// Write json contents to web page
		jsonWriter.writeObject(errorObj);
	}
}
