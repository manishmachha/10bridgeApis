package com.ps.tenbridge.datahub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

import com.ps.tenbridge.datahub.dto.LocationDTO;
import com.ps.tenbridge.datahub.dto.ProviderDTO;
import com.ps.tenbridge.datahub.dto.SearchPatientApi;
import com.ps.tenbridge.datahub.utility.SingleLocationsUtil;
import com.ps.tenbridge.datahub.utility.SinglePractitionersUtil;
import com.veradigm.ps.tenbridge.client.ApiClient;
import com.veradigm.ps.tenbridge.client.api.BookAppointmentApi;
import com.veradigm.ps.tenbridge.client.api.CancelAppointmentApi;
import com.veradigm.ps.tenbridge.client.api.CreatePatientApi;
import com.veradigm.ps.tenbridge.client.api.GetAppointmentTypesApi;
import com.veradigm.ps.tenbridge.client.api.GetAppontmentsApi;
import com.veradigm.ps.tenbridge.client.api.GetAvailableCancelReasonsApi;
import com.veradigm.ps.tenbridge.client.api.GetAvailableChangeReasonsApi;
import com.veradigm.ps.tenbridge.client.api.GetCptValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetEthnicityValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetGenderValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetNotesApi;
import com.veradigm.ps.tenbridge.client.api.GetPatientAlertsApi;
import com.veradigm.ps.tenbridge.client.api.GetPayorGroupsApi;
import com.veradigm.ps.tenbridge.client.api.GetPracticeLocationsApi;
import com.veradigm.ps.tenbridge.client.api.GetProviderScheduleApi;
import com.veradigm.ps.tenbridge.client.api.GetProviderSlotsApi;
import com.veradigm.ps.tenbridge.client.api.GetProvidersApi;
import com.veradigm.ps.tenbridge.client.api.GetRaceValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetReferralSourcesApi;
import com.veradigm.ps.tenbridge.client.api.GetReferringProvidersApi;
import com.veradigm.ps.tenbridge.client.api.GetSingleLocationApi;
import com.veradigm.ps.tenbridge.client.api.GetSinglePractitionerApi;

@Configuration
public class OpenApiConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, LocationDTO> redisLocationTemplate() {
		RedisTemplate<String, LocationDTO> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}

	@Bean
	public RedisTemplate<String, ProviderDTO> redisPractitionerTemplate() {
		RedisTemplate<String, ProviderDTO> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}

	@Bean
	public ApiClient apiClient() {
		ApiClient apiClient = new ApiClient();
		return apiClient;
	}

	@Bean
	public BookAppointmentApi bookAppointmentApi(ApiClient apiClient) {
		return new BookAppointmentApi(apiClient);
	}

	@Bean
	public SingleLocationsUtil singleLocationsUtil(ApiClient apiClient) {
		return new SingleLocationsUtil(apiClient);
	}

	@Bean
	public SinglePractitionersUtil singlePractitionersUtil(ApiClient apiClient) {
		return new SinglePractitionersUtil(apiClient);
	}

	@Bean
	public GetNotesApi getNotesApi(ApiClient apiClient) {

		return new GetNotesApi(apiClient);
	}

	@Bean
	public GetSingleLocationApi singleLocationApi(ApiClient apiClient) {

		return new GetSingleLocationApi(apiClient);
	}

	@Bean
	public GetSinglePractitionerApi singlePractitionerApi(ApiClient apiClient) {

		return new GetSinglePractitionerApi(apiClient);
	}

	@Bean
	public GetProviderScheduleApi getProviderScheduleApi(ApiClient apiClient) {

		return new GetProviderScheduleApi(apiClient);
	}

	@Bean
	public CancelAppointmentApi cancelAppointmentApi(ApiClient apiClient) {

		return new CancelAppointmentApi(apiClient);
	}

	@Bean
	public GetPatientAlertsApi getPatientAlertsApi(ApiClient apiClient) {
		return new GetPatientAlertsApi(apiClient);
	}

	@Bean
	public GetReferringProvidersApi getRefferingProvidersApi(ApiClient apiClient) {
		return new GetReferringProvidersApi(apiClient);
	}

	@Bean
	public GetEthnicityValuesApi getEthnicity(ApiClient apiClient) {
		return new GetEthnicityValuesApi(apiClient);
	}

	@Bean
	public GetRaceValuesApi getRaces(ApiClient apiClient) {
		return new GetRaceValuesApi(apiClient);
	}

	@Bean
	public GetPayorGroupsApi getInsurance(ApiClient apiClient) {
		return new GetPayorGroupsApi(apiClient);
	};

	@Bean
	public SearchPatientApi getPatients(ApiClient apiClient) {
		return new SearchPatientApi(apiClient);
	}

	@Bean
	public GetProvidersApi GetProviders(ApiClient apiClient) {
		return new GetProvidersApi(apiClient);
	}

	@Bean
	public GetPracticeLocationsApi getLocations(ApiClient apiClient) {
		return new GetPracticeLocationsApi(apiClient);
	}

	@Bean
	public GetProviderSlotsApi getSlots(ApiClient apiClient) {
		return new GetProviderSlotsApi(apiClient);
	}

	@Bean
	public GetAvailableCancelReasonsApi getCancelReasons(ApiClient apiClient) {
		return new GetAvailableCancelReasonsApi(apiClient);
	}

	@Bean
	public GetAvailableChangeReasonsApi getChangeReasons(ApiClient apiClient) {
		return new GetAvailableChangeReasonsApi(apiClient);
	}

	@Bean
	public GetReferralSourcesApi getReferralSources(ApiClient apiClient) {
		return new GetReferralSourcesApi(apiClient);
	}

	@Bean
	public CreatePatientApi createPatientApi(ApiClient apiClient) {
		return new CreatePatientApi(apiClient);
	}

	@Bean
	public GetAppontmentsApi getAppontmentsApi(ApiClient apiClient) {
		return new GetAppontmentsApi(apiClient);
	}

	@Bean
	public GetGenderValuesApi getGenders(ApiClient apiClient) {
		return new GetGenderValuesApi(apiClient);
	}

	@Bean
	public GetCptValuesApi getCptsApi(ApiClient apiClient) {
		return new GetCptValuesApi(apiClient);
	}

	@Bean
	public GetAppointmentTypesApi getAppointmentTypesApi(ApiClient apiClient) {
		return new GetAppointmentTypesApi(apiClient);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
