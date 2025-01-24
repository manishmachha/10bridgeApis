package com.ps.tenbridge.datahub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ps.tenbridge.datahub.dto.GendersDTO;
import com.veradigm.ps.tenbridge.client.models.Gender;



@Mapper
public interface GenderMapper {
	GenderMapper INSTANCE = Mappers.getMapper(GenderMapper.class);

	@Mapping(source = "genderId", target = "genderId")
	@Mapping(source = "gender", target = "genderName")
	GendersDTO GenderToGendersDTO(Gender genders);
}
