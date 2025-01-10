package com.ps.tenbridge.datahub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ps.tenbridge.datahub.dto.CancelReasonsDTO;
import com.veradigm.ps.tenbridge.client.models.CancelReasonsResponse;

@Mapper
public interface CancelReasonsMapper {
	CancelReasonsMapper INSTANCE = Mappers.getMapper(CancelReasonsMapper.class);

	@Mapping(source = "cancellationReasonId", target = "cancellation_reason_id")
	@Mapping(source = "description", target = "description")
	CancelReasonsDTO CancelReasonsToCancelReasonsDTO(CancelReasonsResponse cancelReasonsResponse);
}
