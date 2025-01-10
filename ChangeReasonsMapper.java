package com.ps.tenbridge.datahub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ps.tenbridge.datahub.dto.ChangeReasonsDTO;
import com.veradigm.ps.tenbridge.client.models.ChangeReasonsResponse;

@Mapper
public interface ChangeReasonsMapper {
	ChangeReasonsMapper INSTANCE = Mappers.getMapper(ChangeReasonsMapper.class);

	@Mapping(source = "changeReasonId", target = "change_reason_id")
	@Mapping(source = "description", target = "description")
	ChangeReasonsDTO ChangeReasonsToChangeReasonsDTO(ChangeReasonsResponse cancelChangeReasonsResponse);
}
