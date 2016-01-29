/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import enums.Access;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author pgmank
 */
public class WebUtils {
	/**
	 * Searches for a particular cookie given its name from the browser's cookies.
	 * 
	 * @param cookieName	name of the cookie to be searched
	 * @param request		request object where to get the cookies from
	 * @return the cookie found or null of the cookie was not found
	 * @throws IllegalStateException if no cookies are set
	 * if the request object is not set properly or cookies are not set in browser.
	 */
	public static Cookie getCookie(String cookieName, HttpServletRequest request)
		throws IllegalStateException {
		Cookie[] cookies;
		
		try {
			cookies = request.getCookies();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			throw new IllegalStateException("request object not initilized");
		}
		
		Cookie foundCookie = null;
		
		if (cookies == null) {
			throw new IllegalStateException("No cookies found");
		}
		
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				foundCookie = cookie;
				break;
			}
		}
		return foundCookie;
	}
	
	/**
	 * Checks if the current role has access to the permissions that the given
	 * role has.
	 * 
	 * @param role		Role to compare permissions with
	 * @param request	current {@link HttpServletRequest} object
	 * @return			The corresponding Permission enum value
	 */
	public static Access checkAccess(String role, HttpServletRequest request) {
		String currentRole;
		
		try {
			currentRole = getCookie("role", request).getValue();
		} catch (IllegalStateException ex) {
			currentRole = null;
		}
		
		if (currentRole == null) {
			return Access.NOT_A_MEMBER;
		} else if (!currentRole.equals(role)) {
			return Access.DENIED;
		} else {
			return Access.GRANTED;
		}
	}
	
	/**
	 * Gets a descriptive message of the access type.
	 * 
	 * @param access	access type to describe
	 * @param role		specifies the role to display in an access denied message
	 * @return			the descriptive message of the access enum or null if
	 *					given enum value does have a correspondence or is null
	 */
//	public static String getAccessDescription(Access access, String role) {
//		
//		String message;
//		
//		switch (access) {
//			case NOT_A_MEMBER:
//				message = "Only authenticated users can access this service";
//				break;
//			
//			case DENIED:
//				message = "Only a " + role + " can access this service";
//				break;
//				
//			case GRANTED:
//				message = "Access Granted !";
//				break;
//			default:
//				message = null;
//				break;
//		}
//		
//		return message;
//		
//	}
}