package com.ps.tenbridge.datahub.dto;

import java.io.Serializable;

public class PatientAlertsDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7533458432563189416L;
	private String patient;
	private String description;
	private String category;
	private String preventBooking;

	public PatientAlertsDTO(String patient, String description, String category, String preventBooking) {
		super();
		this.patient = patient;
		this.description = description;
		this.category = category;
		this.preventBooking = preventBooking;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPreventBooking() {
		return preventBooking;
	}

	public void setPreventBooking(String preventBooking) {
		this.preventBooking = preventBooking;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PatientAlertsDTO [patient=" + patient + ", description=" + description + ", category=" + category
				+ ", preventBooking=" + preventBooking + "]";
	}

}
