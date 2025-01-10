package com.ps.tenbridge.datahub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.ps.tenbridge.datahub.dto.ReferralSourcesDTO;
import com.veradigm.ps.tenbridge.client.models.ReferralSourcesResponse;



@Mapper
public interface ReferralSourcesMapper {
	ReferralSourcesMapper INSTANCE = Mappers.getMapper(ReferralSourcesMapper.class);

	@Mapping(source = "referralSourceId", target = "referral_source_id")
	@Mapping(source = "description", target = "description")
	ReferralSourcesDTO ReferralSourcesToReferralSourcesDTO(ReferralSourcesResponse referralSourcesResponse);
}
