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
public class ManagerRequest {
	
	private long requestID;
	
	private String salesmanUsername;
	
	private String managerUsername;

	private String status;
	
	private String description;
	
	//private String requestTime;
	
	//private String confirmDate;

	public ManagerRequest() {
	}
	
	public ManagerRequest(String salesmanUsername, String managerUsername, String status, String description) {
		this(-1, salesmanUsername,  managerUsername,  status,  description);
	}
	
	public ManagerRequest(long requestID, String salesmanUsername, String managerUsername, String status, String description) {
		this.requestID = requestID;
		this.salesmanUsername = salesmanUsername;
		this.managerUsername = managerUsername;
		this.status = status;
		this.description = description;
	}
	
	
	
	
	/**
	 * Set the value of salesmanUsername
	 *
	 * @param salesmanUsername new value of salesmanUsername
	 */
	public void setSalesmanUsername(String salesmanUsername) {
		this.salesmanUsername = salesmanUsername;
	}	
	
	/**
	 * Get the value of salesmanUsername
	 *
	 * @return the value of salesmanUsername
	 */
	public String getSalesmanUsername() {
		return salesmanUsername;
	}
		
	/**
	 * Get the value of managerUsername
	 *
	 * @return the value of managerUsername
	 */
	public String getManagerUsername() {
		return managerUsername;
	}

	/**
	 * Set the value of managerUsername
	 *
	 * @param managerUsername new value of managerUsername
	 */
	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	}
	
	/**
	 * Get the value of status
	 *
	 * @return the value of status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Set the value of status
	 *
	 * @param status new value of status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
		
//	/**
//	 * Get the value of requestTime
//	 *
//	 * @return the value of requestTime
//	 */
//	public String getRequestTime() {
//		return requestTime;
//	}
//
//	/**
//	 * Set the value of requestTime
//	 *
//	 * @param requestTime new value of requestTime
//	 */
//	public void setRequestTime(String requestTime) {
//		this.requestTime = requestTime;
//	}
//		
//	/**
//	 * Get the value of confirmDate
//	 *
//	 * @return the value of confirmDate
//	 */
//	public String getConfirmDate() {
//		return confirmDate;
//	}
//
//	/**
//	 * Set the value of confirmDate
//	 *
//	 * @param confirmDate new value of confirmDate
//	 */
//	public void setConfirmDate(String confirmDate) {
//		this.confirmDate = confirmDate;
//	}

	public long getRequestID() {
		return requestID;
	}

	public void setRequestID(long requestID) {
		this.requestID = requestID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

	@Override
	public String toString() {
		return "ManagerRequest{" + "requestID=" + requestID + "salesmanUsername=" + salesmanUsername + ", managerUsername=" + managerUsername + ", status=" + status + ", description=" + description + '}';
	}
	
	

}
