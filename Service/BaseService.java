package com.ps.tenbridge.datahub.utility;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.slf4j.LoggerFactory;

import com.veradigm.ps.tenbridge.client.models.AppointmentNotesRequest;
import com.veradigm.ps.tenbridge.client.models.AppointmentNotesRequestData;
import com.veradigm.ps.tenbridge.client.models.CancelAppointmentRequest;
import com.veradigm.ps.tenbridge.client.models.GetLocationByIdRequest;
import com.veradigm.ps.tenbridge.client.models.GetLocationByIdRequestData;
import com.veradigm.ps.tenbridge.client.models.GetProviderByIdRequest;
import com.veradigm.ps.tenbridge.client.models.GetProviderByIdRequestData;
import com.veradigm.ps.tenbridge.client.models.NewAppointment;
import com.veradigm.ps.tenbridge.client.models.NewAppointmentRequest;
import com.veradigm.ps.tenbridge.client.models.NewPatient;
import com.veradigm.ps.tenbridge.client.models.PatientCreateRequest;
import com.veradigm.ps.tenbridge.client.models.PatientRequest;
import com.veradigm.ps.tenbridge.client.models.PatientRequestData;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;
import com.veradigm.ps.tenbridge.client.models.ScheduleRequest;
import com.veradigm.ps.tenbridge.client.models.ScheduleRequestData;

public abstract class BaseService {
	protected RequestMetaData createRequestMetaData(String siteID, String customerName) {
		RequestMetaData meta = new RequestMetaData();
		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		return meta;
	}

	protected PatientRequest createPatientRequest(String siteID, String customerName, String first_name,
			String last_name, String date_of_birth) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		PatientRequestData patientRequestData = new PatientRequestData();
		patientRequestData.setFirstName(first_name);
		patientRequestData.setLastName(last_name);
		patientRequestData.setDateOfBirth(date_of_birth);
		PatientRequest patientRequest = new PatientRequest();
		patientRequest.setMeta(meta);
		patientRequest.setData(patientRequestData);
		return patientRequest;
	}

	protected <T, U> List<U> buildResponse(List<T> sourceList, Function<T, U> mapper) {
		if (sourceList == null || sourceList.isEmpty()) {
			LoggerFactory.getLogger(this.getClass()).warn("API returned empty list");
			return Collections.emptyList();
		}
		return sourceList.stream().map(mapper).toList();
	}

	protected GetProviderByIdRequest createGetProviderByIdRequest(String siteID, String customerName,
			String practitionerId) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		GetProviderByIdRequestData practitionerRequestData = new GetProviderByIdRequestData();
		practitionerRequestData.setPractitionerId(practitionerId);
		GetProviderByIdRequest practitionerRequest = new GetProviderByIdRequest();
		practitionerRequest.setMeta(meta);
		practitionerRequest.setData(practitionerRequestData);
		return practitionerRequest;
	}

	protected GetLocationByIdRequest createGetLoactionByIdRequest(String siteID, String customerName,
			String practitionerId) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		GetLocationByIdRequestData practitionerRequestData = new GetLocationByIdRequestData();
		practitionerRequestData.setLocationId(practitionerId);
		GetLocationByIdRequest practitionerRequest = new GetLocationByIdRequest();
		practitionerRequest.setMeta(meta);
		practitionerRequest.setData(practitionerRequestData);
		return practitionerRequest;
	}

	protected PatientCreateRequest cratePatientCreateRequest(String siteID, String customerName, String first_name,
			String last_name, String middle_name, String date_of_birth, String gender, String phone,
			String address_line_1, String address_line_2, String state, String city, String zip, String email) {
		RequestMetaData meta = new RequestMetaData();
		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		NewPatient data = new NewPatient();
		data.setFirstName(first_name);
		data.setLastName(last_name);
		data.setMiddleName(middle_name);
		data.setDateOfBirth(date_of_birth);
		data.setGender(gender);
		data.setPhone(phone);
		data.setAddressLine1(address_line_1);
		data.setAddressLine2(address_line_2);
		data.setState(state);
		data.setCity(city);
		data.setZip(zip);
		data.setEmail(email);
		PatientCreateRequest patientCreateRequest = new PatientCreateRequest();
		patientCreateRequest.setMeta(meta);
		patientCreateRequest.setData(data);

		return patientCreateRequest;
	}

	protected AppointmentNotesRequest createAppointmentNotesRequest(String siteID, String customerName,
			String patientProfileId, String appointmentId) {
		RequestMetaData meta = new RequestMetaData();
		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		AppointmentNotesRequestData appointmentNotesRequestData = new AppointmentNotesRequestData();
		appointmentNotesRequestData.setAppointmentId(appointmentId);
		appointmentNotesRequestData.setPatientProfileId(patientProfileId);
		AppointmentNotesRequest appointmentNotesRequest = new AppointmentNotesRequest();
		appointmentNotesRequest.setMeta(meta);
		appointmentNotesRequest.setData(appointmentNotesRequestData);
		return appointmentNotesRequest;
	}

	protected CancelAppointmentRequest createCancelAppointmentRequest(String siteID, String customerName,
			String requested_appointment_id) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		NewAppointment appointment = new NewAppointment();
		appointment.setRequestedAppointmentId(requested_appointment_id);
		CancelAppointmentRequest cancelAppointmentRequest = new CancelAppointmentRequest();
		cancelAppointmentRequest.setMeta(meta);
		cancelAppointmentRequest.setAppointments(appointment);
		return cancelAppointmentRequest;
	}

	protected ScheduleRequest createScheduleRequest(String siteID, String customerName, String patientProfileId,
			String start_date) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		ScheduleRequest scheduleRequest = new ScheduleRequest();
		scheduleRequest.setMeta(meta);
		ScheduleRequestData scheduleRequestData = new ScheduleRequestData();
		scheduleRequestData.setPatientProfileId(patientProfileId);
		scheduleRequestData.setStartDate(start_date);
		scheduleRequest.setData(scheduleRequestData);
		return scheduleRequest;
	}

	protected NewAppointmentRequest createBookAppointmentRequest(String siteID, String customerName,
			String appointmentDate, Integer appointmentDuration, String appointmentDateTime, String appointmentType,
			String appointmentStatus, String appointmentCreatedDate, String appointmentBookingDate,
			String appointmentBookedBy, String coverageType, String visitType, String appointmentStartTime,
			String appointmentEndTime, String scheduledLocationId, String scheduledProviderId,
			String scheduledDepartment, String referringProviderId, String requestedAppointmentId,
			String patientIdentifier, String notesOrComments) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		NewAppointment bookAppointmentRequestData = new NewAppointment();
		bookAppointmentRequestData.setAppointmentDate(appointmentDate);
		bookAppointmentRequestData.setAppointmentDuration(appointmentDuration);
		bookAppointmentRequestData.setAppointmentDateTime(appointmentDateTime);
		bookAppointmentRequestData.setAppointmentType(appointmentType);
		bookAppointmentRequestData.setAppointmentStatus(appointmentStatus);
		bookAppointmentRequestData.setAppointmentCreatedDate(appointmentCreatedDate);
		bookAppointmentRequestData.setAppointmentBookingDate(appointmentBookingDate);
		bookAppointmentRequestData.setAppointmentBookedBy(appointmentBookedBy);
		bookAppointmentRequestData.setCoverageType(coverageType);
		bookAppointmentRequestData.setVisitType(visitType);
		bookAppointmentRequestData.setAppointmentStartTime(appointmentStartTime);
		bookAppointmentRequestData.setAppointmentEndTime(appointmentEndTime);
		bookAppointmentRequestData.setScheduledLocationId(scheduledLocationId);
		bookAppointmentRequestData.setScheduledProviderId(scheduledProviderId);
		bookAppointmentRequestData.setScheduledDepartment(scheduledDepartment);
		bookAppointmentRequestData.setReferringProviderId(referringProviderId);
		bookAppointmentRequestData.setRequestedAppointmentId(requestedAppointmentId);
		bookAppointmentRequestData.setPatientIdentifier(patientIdentifier);
		bookAppointmentRequestData.setNotesOrComments(notesOrComments);
		NewAppointmentRequest bookAppointmentRequest = new NewAppointmentRequest();
		bookAppointmentRequest.setMeta(meta);
		bookAppointmentRequest.setAppointments(bookAppointmentRequestData);
		return bookAppointmentRequest;
	}

}
