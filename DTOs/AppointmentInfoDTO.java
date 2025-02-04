package com.ps.tenbridge.datahub.dto;

public class AppointmentInfoDTO {
	private String appointmentId;
	private String scheduledDate;
	private String startDateTime;
	private String endDateTime;
	private ProviderDTO doctorInfo;
	private LocationDTO locationInfo;
	private int appointmentTypeId;
	private String appointmentName;
	private String comments;
	private String chiefComplaintText;
	private String chiefComplaintCommentsInfo;
	private String status;
	private boolean pastAppointment;

	// Getters and Setters
	public String getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public ProviderDTO getDoctorInfo() {
		return doctorInfo;
	}

	public void setDoctorInfo(ProviderDTO doctorInfo) {
		this.doctorInfo = doctorInfo;
	}

	public LocationDTO getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(LocationDTO locationInfo) {
		this.locationInfo = locationInfo;
	}

	public int getAppointmentTypeId() {
		return appointmentTypeId;
	}

	public void setAppointmentTypeId(int appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}

	public String getAppointmentName() {
		return appointmentName;
	}

	public void setAppointmentName(String appointmentName) {
		this.appointmentName = appointmentName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getChiefComplaintText() {
		return chiefComplaintText;
	}

	public void setChiefComplaintText(String chiefComplaintText) {
		this.chiefComplaintText = chiefComplaintText;
	}

	public String getChiefComplaintCommentsInfo() {
		return chiefComplaintCommentsInfo;
	}

	public void setChiefComplaintCommentsInfo(String chiefComplaintCommentsInfo) {
		this.chiefComplaintCommentsInfo = chiefComplaintCommentsInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPastAppointment() {
		return pastAppointment;
	}

	public void setPastAppointment(boolean pastAppointment) {
		this.pastAppointment = pastAppointment;
	}
}
