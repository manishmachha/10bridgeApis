package com.ps.tenbridge.datahub.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ps.tenbridge.datahub.dto.AppointmentInfoDTO;
import com.ps.tenbridge.datahub.dto.LocationDTO;
import com.ps.tenbridge.datahub.dto.PatientInfoDTO;
import com.ps.tenbridge.datahub.dto.PatientInsuranceInfo;
import com.ps.tenbridge.datahub.dto.ProviderDTO;
import com.ps.tenbridge.datahub.dto.ReferringProviderDTO;
import com.veradigm.ps.tenbridge.client.models.Appointment;
import com.veradigm.ps.tenbridge.client.models.Appointments200Response;
import com.veradigm.ps.tenbridge.client.models.InsurancePolicy;
import com.veradigm.ps.tenbridge.client.models.Patient;
import com.veradigm.ps.tenbridge.client.models.Patients200Response;

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
	AppointmentInfoDTO mapAppointments(Appointment source);

	default List<AppointmentInfoDTO> mapAppointmentsWithAdditionalFields(Appointments200Response apiResponse,
			List<ProviderDTO> allProviders, List<LocationDTO> allLocations) {
		// Extract the list of patients from the API response
		List<Appointment> sourceAppointments = apiResponse.getAppointments();

		// Map each SourcePatient to PatientInfoDTO
		List<AppointmentInfoDTO> response = sourceAppointments.stream().map(sourceAppointment -> {
			// Map basic fields
			AppointmentInfoDTO appointmentInfo = mapAppointments(sourceAppointment);

			return appointmentInfo;
		}).collect(Collectors.toList());

		// Assign additional fields to each PatientInfoDTO
		for (int i = 0; i < response.size(); i++) {
			AppointmentInfoDTO appointment = response.get(i);
			if (i < allProviders.size()) {
				appointment.setDoctorInfo(allProviders.get(i));
			}
			if (i < allLocations.size()) {
				appointment.setLocationInfo(allLocations.get(i));
			}
		}

		return response;
	}

}
