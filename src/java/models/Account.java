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
public class Account {
    
	private long phoneNumber;
	private long ownerTaxID;
	private double balance;

	public Account() {
	}

	public Account(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public Account(long phoneNumber, long ownerTaxID) {
		this.phoneNumber = phoneNumber;
		this.ownerTaxID = ownerTaxID;
	}
	
	public Account(long phoneNumber, long ownerTaxID, double balance) {
		this.phoneNumber = phoneNumber;
		this.ownerTaxID = ownerTaxID;
		this.balance = balance;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getOwnerTaxID() {
		return ownerTaxID;
	}

	public void setOwnerTaxID(long ownerTaxID) {
		this.ownerTaxID = ownerTaxID;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Account{" + "phoneNumber=" + phoneNumber + ", ownerTaxID=" + ownerTaxID + ", balance=" + balance + '}';
	}

	
	
	
}
