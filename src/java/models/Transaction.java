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
public class Transaction {
	
	private long transactionID;
	private long phoneNumber;
	private double merit;
	private String time;
	
	public Transaction() {
	}
	
	public Transaction(long transactionID) {
		this.transactionID = transactionID;
	}

	public Transaction(long transactionID, long phoneNumber) {
		this.transactionID = transactionID;
		this.phoneNumber = phoneNumber;
	}
	
	public Transaction(long phoneNumber, double merit) {
		this.phoneNumber = phoneNumber;
		this.merit = merit;
	}

	public Transaction(long transactionID, long phoneNumber, double merit) {
		this.transactionID = transactionID;
		this.phoneNumber = phoneNumber;
		this.merit = merit;
	}

	public Transaction(long transactionID, long phoneNumber, double merit, String time) {
		this.transactionID = transactionID;
		this.phoneNumber = phoneNumber;
		this.merit = merit;
		this.time = time;
	}
	
	
	
	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public double getMerit() {
		return merit;
	}

	public void setMerit(double merit) {
		this.merit = merit;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Transaction{" + "transactionID=" + transactionID + ", phoneNumber=" + phoneNumber + ", merit=" + merit + ", time=" + time + '}';
	}
	
	
	
	
}
