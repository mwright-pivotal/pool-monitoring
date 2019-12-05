package io.wrightcode.pool.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Status implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String monitorUUID = "";
	String textStatus = "";
	String phValue = "";
	String tdsValue = "";
	public String getTdsValue() {
		return tdsValue;
	}

	public void setTdsValue(String tdsValue) {
		this.tdsValue = tdsValue;
	}
	Date timeUpdated;
	
	public Status(String monitorUUID, String phValue, String orpValue) {
		this.monitorUUID = monitorUUID;
		this.phValue = phValue;
		this.timeUpdated = new Date();
	}
	
	public Date getTimeUpdated() {
		return timeUpdated;
	}
	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
	}
	public String getPhValue() {
		return phValue;
	}
	public void setPhValue(String phValue) {
		this.phValue = phValue;
	}
	public String getOrpValue() {
		return orpValue;
	}
	public void setOrpValue(String orpValue) {
		this.orpValue = orpValue;
	}
	String orpValue = "";
	public String getMonitorUUID() {
		return monitorUUID;
	}
	public void setMonitorUUID(String monitorUUID) {
		this.monitorUUID = monitorUUID;
	}
	public String getTextStatus() {
		return textStatus;
	}
	public void setTextStatus(String textStatus) {
		this.textStatus = textStatus;
	}
	public String toString() {
		return (new Timestamp(timeUpdated.getTime())) + " : " + monitorUUID + ": " + phValue + " : " + orpValue + " : " + tdsValue;
 	}
}
