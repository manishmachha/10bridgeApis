package com.ps.tenbridge.datahub.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ps.tenbridge.datahub.controllerImpl.TenBridgeService;
import com.ps.tenbridge.datahub.dto.AppointmentInfoDTO;
import com.ps.tenbridge.datahub.dto.AppointmentLocationInfoDTO;
import com.ps.tenbridge.datahub.dto.AppointmentPractitionerInfoDTO;
import com.veradigm.ps.tenbridge.client.models.Appointment;
import com.veradigm.ps.tenbridge.client.models.Appointments200Response;
import com.veradigm.ps.tenbridge.client.models.Location;
import com.veradigm.ps.tenbridge.client.models.Practitioner;

@Mapper
public interface PatientAppointmentsMapper {
	PatientAppointmentsMapper INSTANCE = Mappers.getMapper(PatientAppointmentsMapper.class);

	@Mapping(source = "appointmentId", target = "appointmentId")
	@Mapping(source = "appointmentBookingDate", target = "scheduledDate")
	@Mapping(source = "appointmentStartTime", target = "startDateTime")
	@Mapping(source = "appointmentEndTime", target = "endDateTime")
	@Mapping(source = "appointmentType", target = "appointmentName")
	@Mapping(source = "notesOrComments", target = "comments")
	@Mapping(source = "appointmentStatus", target = "status")
	@Mapping(source = "scheduledLocationId", target = "locationId")
	@Mapping(source = "scheduledProviderId", target = "practitionerId")
	AppointmentInfoDTO mapAppointments(Appointment source);

	@Mapping(source = "locationId", target = "locationId")
	@Mapping(source = "abbreviation", target = "abbreviation")
	@Mapping(source = "abbreviation", target = "locationAbbreviation")
	@Mapping(source = "locationName", target = "listName")
	@Mapping(source = "addressLine1", target = "address1")
	@Mapping(source = "addressLine2", target = "address2")
	@Mapping(source = "city", target = "city")
	@Mapping(source = "state", target = "state")
	@Mapping(source = "zip", target = "zip")
	AppointmentLocationInfoDTO mapLocation(Location source);

	@Mapping(source = "firstName", target = "firstName")
	@Mapping(source = "lastName", target = "lastName")
	@Mapping(source = "middleName", target = "middle")
	@Mapping(source = "abbreviation", target = "abbreviation")
	@Mapping(source = "speciality", target = "degree")
	@Mapping(source = "practitionerId", target = "doctorId")
	@Mapping(source = "fullName", target = "listName")
	AppointmentPractitionerInfoDTO mapDoctor(Practitioner source);

	default List<AppointmentInfoDTO> mapAppointmentsWithAdditionalFields(Appointments200Response apiResponse,
			Location location, Practitioner practitioner) {

		// Extract the list of Appointments from the API response
		List<Appointment> sourceAppointments = apiResponse.getAppointments();

		// Map each SourceAppointment to AppointmentInfoDTO
		List<AppointmentInfoDTO> response = sourceAppointments.stream().map(sourceAppointment -> {
			// Map basic fields
			AppointmentInfoDTO appointmentInfo = mapAppointments(sourceAppointment);

			appointmentInfo.setLocationInfo(location);
			appointmentInfo.setDoctorInfo(practitioner);

			return appointmentInfo;
		}).collect(Collectors.toList());

		return response;
	}

}
