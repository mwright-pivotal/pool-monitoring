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
public class PoolTelemetry implements Serializable {
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
	Double phValue = 0.0;
	@Column(name="tds")
	Double tdsValue = 0.0;
	public Double getTdsValue() {
		return tdsValue;
	}

	public void setTdsValue(Double tdsValue) {
		this.tdsValue = tdsValue;
	}
	@Column(name="time_updated")
	Date timeUpdated;
	
	public PoolTelemetry(String monitorUUID, Double phValue, Double orpValue, Double tdsValue) {
		this.monitorUUID = monitorUUID;
		this.phValue = phValue;
		this.orpValue = orpValue;
		this.tdsValue = tdsValue;
		this.timeUpdated = new Date();
	}
	
	public PoolTelemetry() {
		this.monitorUUID = "not set";
		this.phValue = 0.0;
		this.orpValue = 0.0;
		this.tdsValue = 0.0;
		this.timeUpdated = new Date();
	}
	
	public Date getTimeUpdated() {
		return timeUpdated;
	}
	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
	}
	public Double getPhValue() {
		return phValue;
	}
	public void setPhValue(Double phValue) {
		this.phValue = phValue;
	}
	public Double getOrpValue() {
		return orpValue;
	}
	public void setOrpValue(Double orpValue) {
		this.orpValue = orpValue;
	}
	@Column(name="orp_value")
	Double orpValue = 0.0;
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
		return (new Timestamp(timeUpdated.getTime())) + " : " + monitorUUID + ": " + phValue + " :" + orpValue + " :" + tdsValue;
 	}
}
