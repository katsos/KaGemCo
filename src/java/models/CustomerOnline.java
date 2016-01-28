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
public class CustomerOnline extends Customer {
	private String username;
	private String password;
	private long taxID;
	private String role;
	private String regDate;
	
	
	// Empty constructor
	public CustomerOnline() {
		super();
	}

	public CustomerOnline(String username, String password, long taxID, String role, String regDate, String firstname, String lastname, String birthDate, char gender, String familyStatus, String homeAddress, long bankAccountNo, String personalCode, long relatetaxID) {
		super(firstname, lastname, birthDate, gender, familyStatus, homeAddress, taxID, bankAccountNo, personalCode, relatetaxID);
		this.username = username;
		this.password = password;
		this.taxID = taxID;
		this.role = role;
		this.regDate = regDate;
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

	@Override
	public String toString() {
		return "CustomerOnline{" + "username=" + username + ", password=" + password + ", taxID=" + taxID + ", role=" + role + ", regDate=" + regDate + '}' + super.toString();
	}
	
	
	
}
