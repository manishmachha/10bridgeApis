package com.ps.tenbridge.datahub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ps.tenbridge.datahub.dto.CPTsDTO;
import com.veradigm.ps.tenbridge.client.models.CPTCode;



@Mapper
public interface CptMapper {
	CptMapper INSTANCE = Mappers.getMapper(CptMapper.class);

	@Mapping(source = "code", target = "code")
	@Mapping(source = "description", target = "description")
	CPTsDTO CptToCptsDTO(CPTCode cptCode);
}
