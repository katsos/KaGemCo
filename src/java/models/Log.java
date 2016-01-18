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
public class Log {

	private static final long serialVersionUID = 1L;
	private Long logID;
	private String actorUsername;
	private String actionTime;
	private String description;

	public Log() {
	}

	public Log(Long logID) {
		this.logID = logID;
	}

	public Log(Long logID, String actorUsername, String actionTime, String description) {
		this.logID = logID;
		this.actorUsername = actorUsername;
		this.actionTime = actionTime;
		this.description = description;
	}

	public Log(String actorUsername, String description) {
		this.actorUsername = actorUsername;
		this.description = description;
	}

	public Long getLogID() {
		return logID;
	}

	public void setLogID(Long logID) {
		this.logID = logID;
	}

	public String getActorUsername() {
		return actorUsername;
	}

	public void setActorUsername(String actorUsername) {
		this.actorUsername = actorUsername;
	}

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Log{" + "logID=" + logID + ", actorUsername=" + actorUsername + ", actionTime=" + actionTime + ", description=" + description + '}';
	}
	
	
	
}
