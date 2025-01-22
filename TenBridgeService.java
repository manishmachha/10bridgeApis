package com.ps.tenbridge.datahub.controllerImpl;

import java.util.List;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;

import com.ps.tenbridge.datahub.config.OAuth2Config;
import com.ps.tenbridge.datahub.dto.EthnicityDTO;
import com.ps.tenbridge.datahub.dto.InsuranceDTO;
import com.ps.tenbridge.datahub.dto.LocationDTO;
import com.ps.tenbridge.datahub.dto.ProviderDTO;
import com.ps.tenbridge.datahub.dto.RacesDTO;
import com.ps.tenbridge.datahub.dto.ReferralSourcesDTO;
import com.ps.tenbridge.datahub.dto.ReferringProviderDTO;
import com.ps.tenbridge.datahub.mapper.CarrierMapper;
import com.ps.tenbridge.datahub.mapper.EthnicityMapper;
import com.ps.tenbridge.datahub.mapper.LocationMapper;
import com.ps.tenbridge.datahub.mapper.PractitionerMapper;
import com.ps.tenbridge.datahub.mapper.RaceMapper;
import com.ps.tenbridge.datahub.mapper.ReferralSourcesMapper;
import com.ps.tenbridge.datahub.mapper.ReferringProviderMapper;
import com.ps.tenbridge.datahub.services.authentication.TokenService;
import com.ps.tenbridge.datahub.utility.BaseService;
import com.veradigm.ps.tenbridge.client.ApiClient;
import com.veradigm.ps.tenbridge.client.api.GetEthnicityValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetPatientAlertsApi;
import com.veradigm.ps.tenbridge.client.api.GetPayorGroupsApi;
import com.veradigm.ps.tenbridge.client.api.GetPracticeLocationsApi;
import com.veradigm.ps.tenbridge.client.api.GetProvidersApi;
import com.veradigm.ps.tenbridge.client.api.GetRaceValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetReferralSourcesApi;
import com.veradigm.ps.tenbridge.client.api.GetReferringProvidersApi;
import com.veradigm.ps.tenbridge.client.models.Ethnicity200Response;
import com.veradigm.ps.tenbridge.client.models.PatientAlert200Response;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequest;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequestBody;
import com.veradigm.ps.tenbridge.client.models.PayorGroups200Response;
import com.veradigm.ps.tenbridge.client.models.PracticeLocation200Response;
import com.veradigm.ps.tenbridge.client.models.Providers200Response;
import com.veradigm.ps.tenbridge.client.models.Race200Response;
import com.veradigm.ps.tenbridge.client.models.ReferralSource200Response;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;

@Service
public class TenBridgeService extends BaseService {

	private static final Logger logger = Logger.getLogger(TenBridgeService.class.getName());
	private final TokenService tokenService;

	private final ApiClient apiClient;
	@Autowired
	private GetProvidersApi providersApi;
	@Autowired
	private GetPracticeLocationsApi locationsApi;
	@Autowired
	private GetPayorGroupsApi payerGroupApi;
	@Autowired
	private GetReferringProvidersApi refferingProviderApi;
	@Autowired
	private GetEthnicityValuesApi ethnicityApi;
	@Autowired
	private GetRaceValuesApi racesApi;
	@Autowired
	private GetReferralSourcesApi referralSourcesApi;
	@Autowired
	private GetPatientAlertsApi patientAlertsApi;

	private final OAuth2Config oauth;

	public TenBridgeService(ApiClient apiClient, TokenService ts, OAuth2Config oauth, GetProvidersApi providersApi,
			GetPracticeLocationsApi locationsApi, GetPayorGroupsApi payorGroupsApi, GetReferringProvidersApi referringProvidersApi) {
		this.apiClient = apiClient;
		this.tokenService = ts;
		this.oauth = oauth;
		this.providersApi = providersApi;
		this.locationsApi = locationsApi;
		this.payerGroupApi = payorGroupsApi;
		this.refferingProviderApi = referringProvidersApi;
	}

	public List<ProviderDTO> getProviders(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();
			Providers200Response apiResponse = providersApi.providers(meta);
			if (apiResponse == null || apiResponse.getProviders() == null) {
				logger.severe("Invalid data received: Providers list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getProviders().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving providers: Empty provider list");
			}
			return buildResponse(apiResponse.getProviders(), PractitionerMapper.INSTANCE::practitionerToProviderDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving providers: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving providers: " + e.getMessage(), e);
		}
	}

	public List<LocationDTO> getLocations(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			PracticeLocation200Response apiResponse = locationsApi.practiceLocation(meta);

			if (apiResponse == null || apiResponse.getLocations() == null) {
				logger.severe("Invalid data received: Providers list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getLocations().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving locations: Empty location list");
			}
			return buildResponse(apiResponse.getLocations(), LocationMapper.INSTANCE::LocationToLocationDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving locations: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving locations: " + e.getMessage(), e);
		}
	}

	public List<InsuranceDTO> getInsurances(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			PayorGroups200Response apiResponse = payerGroupApi.payorGroups(meta);

			if (apiResponse == null || apiResponse.getCarriers() == null) {
				logger.severe("Invalid data received: Insurances list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getCarriers().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving insurances: Empty insurance list");
			}
			return buildResponse(apiResponse.getCarriers(), CarrierMapper.INSTANCE::InsuranceCarrierToInsuranceDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving insurances: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving insurances: " + e.getMessage(), e);
		}

	}

	public List<ReferringProviderDTO> getReferringProviders(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			Providers200Response apiResponse = refferingProviderApi.referringProviders(meta);

			if (apiResponse == null || apiResponse.getProviders() == null) {
				logger.severe("Invalid data received: Referring providers list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getProviders().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving referring providers: Empty Referring provider list");
			}
			return buildResponse(apiResponse.getProviders(),
					ReferringProviderMapper.INSTANCE::practitionerToReferringProviderDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving referring providers: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving referring providers: " + e.getMessage(), e);
		}

	}

	public List<EthnicityDTO> getEthnicities(String siteID, String customerName) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		setToken();

		Ethnicity200Response apiResponse = ethnicityApi.ethnicity(meta);

		return buildResponse(apiResponse.getEthnicities(), EthnicityMapper.INSTANCE::EthnicityToEthnicityDTO);
	}

	public List<RacesDTO> getRaces(String siteID, String customerName) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		setToken();

		Race200Response apiResponse = racesApi.race(meta);

		return buildResponse(apiResponse.getRaces(), RaceMapper.INSTANCE::RaceToRacesDTO);
	}

	public List<ReferralSourcesDTO> getReferralSources(String siteID, String customerName) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		setToken();

		ReferralSource200Response apiResponse = referralSourcesApi.referralSource(meta);

		return buildResponse(apiResponse.getReferralSources(),
				ReferralSourcesMapper.INSTANCE::ReferralSourcesToReferralSourcesDTO);
	}

	public Object getPatientAlerts(RequestMetaData meta, PatientAlertsRequestBody body) {
		PatientAlertsRequest patientAlertRequest = new PatientAlertsRequest();
		patientAlertRequest.setMeta(meta);
		patientAlertRequest.setBody(body);
		setToken();
		PatientAlert200Response apiResponse = patientAlertsApi.patientAlert(patientAlertRequest);
		return apiResponse;
	}

//	public void setToken() {
//		ClientRegistration clientRegistration = getOauth().clientRegistration();
//		String token = ts.getToken(clientRegistration);
//		apiClient.setAccessToken(token);
////		System.out.println(">>>>>>>>>>>>" + token);
//	}

	public OAuth2Config getOauth() {
		return oauth;
	}

	public void setToken() {
		if (oauth == null) {
			throw new IllegalStateException("OAuth2Config is not initialized");
		}

		ClientRegistration clientRegistration = oauth.clientRegistration();
		if (clientRegistration == null) {
			throw new IllegalStateException("ClientRegistration is not available");
		}

		String token = tokenService.getToken(clientRegistration);
		apiClient.setAccessToken(token);
	}

}
