package com.ps.tenbridge.datahub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ps.tenbridge.datahub.dto.PatientAlertsDTO;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsResponse;

@Mapper
public interface PatientAlertsMapper {

	PatientAlertsMapper INSTANCE = Mappers.getMapper(PatientAlertsMapper.class);

	@Mapping(source = "patient", target = "patient")
	@Mapping(source = "description", target = "description")
	@Mapping(source = "category", target = "category")
	@Mapping(source = "preventBooking", target = "preventBooking")
	PatientAlertsDTO patientAlertsResponseToPatientAlertsDTO(PatientAlertsResponse patientAlertsResponse);

}
