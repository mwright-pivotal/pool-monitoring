package io.wrightcode.pool.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Status implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="recording_id")
    private Long id;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="device_id")
	String monitorUUID = "";
	String textStatus = "";
	@Column(name="ph")
	String phValue = "";
	@Column(name="tds")
	String tdsValue = "";
	public String getTdsValue() {
		return tdsValue;
	}

	public void setTdsValue(String tdsValue) {
		this.tdsValue = tdsValue;
	}
	@Column(name="time_updated")
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
	@Column(name="orp_value")
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
