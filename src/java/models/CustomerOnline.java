/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author pgmank
 */
public class CustomerOnline {
	private String username;
	private String password;
	private long taxID;
	private String role;
	private String regDate;
	
	
	// Empty constructor
	public CustomerOnline() {
	}
	
	
	
	public CustomerOnline(String username, String password, long taxID, String role, String regDate) {
		this.username = username;
		this.password = password;
		this.taxID = taxID;
		this.role = role;
		this.regDate = regDate;
	}
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getTaxID() {
		return taxID;
	}

	public void setTaxID(long taxID) {
		this.taxID = taxID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
}
