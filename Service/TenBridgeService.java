package com.ps.tenbridge.datahub.controllerImpl;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;

import com.ps.tenbridge.datahub.config.OAuth2Config;
import com.ps.tenbridge.datahub.dto.CPTsDTO;
import com.ps.tenbridge.datahub.dto.CancelReasonsDTO;
import com.ps.tenbridge.datahub.dto.ChangeReasonsDTO;
import com.ps.tenbridge.datahub.dto.EthnicityDTO;
import com.ps.tenbridge.datahub.dto.GendersDTO;
import com.ps.tenbridge.datahub.dto.InsuranceDTO;
import com.ps.tenbridge.datahub.dto.LocationDTO;
import com.ps.tenbridge.datahub.dto.PatientAlertsDTO;
import com.ps.tenbridge.datahub.dto.PatientInfoDTO;
import com.ps.tenbridge.datahub.dto.ProviderDTO;
import com.ps.tenbridge.datahub.dto.RacesDTO;
import com.ps.tenbridge.datahub.dto.ReferralSourcesDTO;
import com.ps.tenbridge.datahub.dto.ReferringProviderDTO;
import com.ps.tenbridge.datahub.mapper.CancelReasonsMapper;
import com.ps.tenbridge.datahub.mapper.CarrierMapper;
import com.ps.tenbridge.datahub.mapper.ChangeReasonsMapper;
import com.ps.tenbridge.datahub.mapper.CptMapper;
import com.ps.tenbridge.datahub.mapper.EthnicityMapper;
import com.ps.tenbridge.datahub.mapper.GenderMapper;
import com.ps.tenbridge.datahub.mapper.LocationMapper;
import com.ps.tenbridge.datahub.mapper.PatientAlertsMapper;
import com.ps.tenbridge.datahub.mapper.PatientSearchMapper;
import com.ps.tenbridge.datahub.mapper.PractitionerMapper;
import com.ps.tenbridge.datahub.mapper.RaceMapper;
import com.ps.tenbridge.datahub.mapper.ReferralSourcesMapper;
import com.ps.tenbridge.datahub.mapper.ReferringProviderMapper;
import com.ps.tenbridge.datahub.services.authentication.TokenService;
import com.ps.tenbridge.datahub.utility.BaseService;
import com.veradigm.ps.tenbridge.client.ApiClient;
import com.veradigm.ps.tenbridge.client.api.CreatePatientApi;
import com.veradigm.ps.tenbridge.client.api.GetAppointmentTypesApi;
import com.veradigm.ps.tenbridge.client.api.GetAppontmentsApi;
import com.veradigm.ps.tenbridge.client.api.GetAvailableCancelReasonsApi;
import com.veradigm.ps.tenbridge.client.api.GetAvailableChangeReasonsApi;
import com.veradigm.ps.tenbridge.client.api.GetCptValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetEthnicityValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetGenderValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetPatientAlertsApi;
import com.veradigm.ps.tenbridge.client.api.GetPayorGroupsApi;
import com.veradigm.ps.tenbridge.client.api.GetPracticeLocationsApi;
import com.veradigm.ps.tenbridge.client.api.GetProviderSlotsApi;
import com.veradigm.ps.tenbridge.client.api.GetProvidersApi;
import com.veradigm.ps.tenbridge.client.api.GetRaceValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetReferralSourcesApi;
import com.veradigm.ps.tenbridge.client.api.GetReferringProvidersApi;
import com.veradigm.ps.tenbridge.client.api.SearchPatientApi;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequest;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequestData;
import com.veradigm.ps.tenbridge.client.models.AppointmentType;
import com.veradigm.ps.tenbridge.client.models.AppointmentTypes200Response;
import com.veradigm.ps.tenbridge.client.models.Appointments200Response;
import com.veradigm.ps.tenbridge.client.models.CPT200Response;
import com.veradigm.ps.tenbridge.client.models.CancellationReason200Response;
import com.veradigm.ps.tenbridge.client.models.ChangeReason200Response;
import com.veradigm.ps.tenbridge.client.models.Ethnicity200Response;
import com.veradigm.ps.tenbridge.client.models.Gender200Response;
import com.veradigm.ps.tenbridge.client.models.Patient;
import com.veradigm.ps.tenbridge.client.models.PatientAlert200Response;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequest;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequestBody;
import com.veradigm.ps.tenbridge.client.models.PatientCreateRequest;
import com.veradigm.ps.tenbridge.client.models.PatientRequest;
import com.veradigm.ps.tenbridge.client.models.Patients200Response;
import com.veradigm.ps.tenbridge.client.models.PayorGroups200Response;
import com.veradigm.ps.tenbridge.client.models.PracticeLocation200Response;
import com.veradigm.ps.tenbridge.client.models.ProviderSlots200Response;
import com.veradigm.ps.tenbridge.client.models.Providers200Response;
import com.veradigm.ps.tenbridge.client.models.Race200Response;
import com.veradigm.ps.tenbridge.client.models.ReferralSource200Response;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;
import com.veradigm.ps.tenbridge.client.models.SlotRequest;
import com.veradigm.ps.tenbridge.client.models.SlotRequestData;

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
	@Autowired
	private GetGenderValuesApi genderValuesApi;
	@Autowired
	private GetAppontmentsApi appointmentsApi;
	@Autowired
	private GetProviderSlotsApi slotsApi;
	@Autowired
	private SearchPatientApi searchPatient;
	@Autowired
	private GetCptValuesApi cptsApi;
	@Autowired
	private GetAvailableCancelReasonsApi cancelReasonsApi;
	@Autowired
	private GetAvailableChangeReasonsApi changeReasonsApi;
	@Autowired
	private CreatePatientApi createPatientApi;
	@Autowired
	private GetAppointmentTypesApi appointmentTypesApi;

	private final OAuth2Config oauth;

	public TenBridgeService(ApiClient apiClient, TokenService ts, OAuth2Config oauth, GetProvidersApi providersApi,
			GetPracticeLocationsApi locationsApi, GetPayorGroupsApi payorGroupsApi,
			GetReferringProvidersApi referringProvidersApi, GetEthnicityValuesApi getEthnicityValuesApi,
			GetRaceValuesApi raceValuesApi, GetReferralSourcesApi referralSourcesApi,
			GetPatientAlertsApi patientAlertsApi, GetGenderValuesApi genderValuesApi, GetCptValuesApi cptsApi,
			GetAvailableCancelReasonsApi cancelReasonsApi, GetAvailableChangeReasonsApi changeReasonsApi,
			GetAppontmentsApi getAppontmentsApi, GetProviderSlotsApi providerSlotsApi, CreatePatientApi patientApi,
			SearchPatientApi searchPatientApi) {
		this.apiClient = apiClient;
		this.tokenService = ts;
		this.oauth = oauth;
		this.providersApi = providersApi;
		this.locationsApi = locationsApi;
		this.payerGroupApi = payorGroupsApi;
		this.refferingProviderApi = referringProvidersApi;
		this.ethnicityApi = getEthnicityValuesApi;
		this.racesApi = raceValuesApi;
		this.referralSourcesApi = referralSourcesApi;
		this.patientAlertsApi = patientAlertsApi;
		this.genderValuesApi = genderValuesApi;
		this.cptsApi = cptsApi;
		this.changeReasonsApi = changeReasonsApi;
		this.cancelReasonsApi = cancelReasonsApi;
		this.appointmentsApi = getAppontmentsApi;
		this.slotsApi = providerSlotsApi;
		this.createPatientApi = patientApi;
		this.searchPatient = searchPatientApi;
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
				throw new RuntimeException(
						"Error occurred while retrieving referring providers: Empty Referring provider list");
			}
			return buildResponse(apiResponse.getProviders(),
					ReferringProviderMapper.INSTANCE::practitionerToReferringProviderDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving referring providers: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving referring providers: " + e.getMessage(), e);
		}

	}

	public List<EthnicityDTO> getEthnicities(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			Ethnicity200Response apiResponse = ethnicityApi.ethnicity(meta);
			if (apiResponse == null || apiResponse.getEthnicities() == null) {
				logger.severe("Invalid data received: Ethnicities list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getEthnicities().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException(
						"Error occurred while retrieving referring providers: Empty Ethnicities list");
			}
			return buildResponse(apiResponse.getEthnicities(), EthnicityMapper.INSTANCE::EthnicityToEthnicityDTO);

		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Ethnicities: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Ethnicities: " + e.getMessage(), e);
		}
	}

	public List<PatientInfoDTO> getPatients(String siteID, String customerName, String first_name, String last_name,
			String date_of_birth, String patientProfileId, String patientNumber) {
		try {
			PatientRequest patientRequest = createPatientRequest(siteID, customerName, first_name, last_name,
					date_of_birth);
			setToken();
			List<ProviderDTO> allProviders = new ArrayList<ProviderDTO>();
			allProviders = getProviders(siteID, customerName);

			List<ReferringProviderDTO> allReferringProviders = new ArrayList<ReferringProviderDTO>();
			allReferringProviders = getReferringProviders(siteID, customerName);

			List<LocationDTO> allLocations = new ArrayList<LocationDTO>();
			allLocations = getLocations(siteID, customerName);

			Patients200Response apiResponse = searchPatient.patients(patientRequest);

			PatientInfoDTO patientInfo = new PatientInfoDTO();
			patientInfo.setPrefDoc(allProviders.get(0));
			patientInfo.setPrefLoc(allLocations.get(0));
			patientInfo.setRefDoc(allReferringProviders.get(0));
			patientInfo.setPatientProfileId(date_of_birth);
			patientInfo.setPatientId(date_of_birth);
			List<PatientInfoDTO> response = PatientSearchMapper.INSTANCE.mapPatientsWithAdditionalFields(apiResponse,
					allProviders, // List of providers
					allLocations, // List of locations
					allReferringProviders // List of referring providers
			);
			return response;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Ethnicities: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Ethnicities: " + e.getMessage(), e);
		}

	}

	public Object getSlots(String siteID, String customerName, String appointmentType, String startDate) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			SlotRequestData data = new SlotRequestData();
			data.setAppointmentType(appointmentType);
			data.setStartDate(startDate);
			SlotRequest slotRequest = new SlotRequest();
			slotRequest.setData(data);
			slotRequest.setMeta(meta);
			setToken();
			ProviderSlots200Response apiResponse = slotsApi.providerSlots(slotRequest);
			if (apiResponse == null || apiResponse.getSlots() == null) {
				logger.severe("Invalid data received: Slots list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getSlots().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving Slots: Empty Slots list");
			}
			return apiResponse;
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Error occurred while retrieving Slots: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Slots: " + e.getMessage(), e);
		}
	}

	public List<RacesDTO> getRaces(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			Race200Response apiResponse = racesApi.race(meta);
			if (apiResponse == null || apiResponse.getRaces() == null) {
				logger.severe("Invalid data received: Races list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getRaces().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving referring providers: Empty Races list");
			}
			return buildResponse(apiResponse.getRaces(), RaceMapper.INSTANCE::RaceToRacesDTO);

		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Races: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Races: " + e.getMessage(), e);
		}
	}

	public List<ReferralSourcesDTO> getReferralSources(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			ReferralSource200Response apiResponse = referralSourcesApi.referralSource(meta);
			if (apiResponse == null || apiResponse.getReferralSources() == null) {
				logger.severe("Invalid data received: ReferralSources list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getReferralSources().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException(
						"Error occurred while retrieving referring providers: Empty ReferralSources list");
			}
			return buildResponse(apiResponse.getReferralSources(),
					ReferralSourcesMapper.INSTANCE::ReferralSourcesToReferralSourcesDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving ReferralSources: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving ReferralSources: " + e.getMessage(), e);
		}

	}

	public List<PatientAlertsDTO> getPatientAlerts(String siteID, String customerName, String patientProfileID) {
		try {
			PatientAlertsRequest patientAlertRequest = new PatientAlertsRequest();
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			patientAlertRequest.setMeta(meta);
			PatientAlertsRequestBody body = new PatientAlertsRequestBody();
			body.setPatientProfileId(patientProfileID);
			patientAlertRequest.setBody(body);
			setToken();
			PatientAlert200Response apiResponse = patientAlertsApi.patientAlert(patientAlertRequest);
			if (apiResponse == null || apiResponse.getPatientAlerts() == null) {
				logger.severe("Invalid data received: PatientAlerts list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getPatientAlerts().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving PatientAlerts: Empty PatientAlerts list");
			}
			return buildResponse(apiResponse.getPatientAlerts(),
					PatientAlertsMapper.INSTANCE::patientAlertsResponseToPatientAlertsDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving ReferralSources: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving PatientAlerts: " + e.getMessage(), e);
		}
	}

	public List<GendersDTO> getGenders(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			Gender200Response apiResponse = genderValuesApi.gender(meta);
			if (apiResponse == null || apiResponse.getGenders() == null) {
				logger.severe("Invalid data received: Genders list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getGenders().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving Genders: Empty Genders list");
			}
			return buildResponse(apiResponse.getGenders(), GenderMapper.INSTANCE::GenderToGendersDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Genders: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Genders: " + e.getMessage(), e);
		}
	}

	public List<CPTsDTO> getCptCodes(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			CPT200Response apiResponse = cptsApi.cPT(meta);
			if (apiResponse == null || apiResponse.getCpts() == null) {
				logger.severe("Invalid data received: cpt list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getCpts().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving cpts: Empty cpt list");
			}
			return buildResponse(apiResponse.getCpts(), CptMapper.INSTANCE::CptToCptsDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving cpts: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving cpts: " + e.getMessage(), e);
		}
	}

	public List<CancelReasonsDTO> getCancelReasons(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			CancellationReason200Response apiResponse = cancelReasonsApi.cancellationReason(meta);
			if (apiResponse == null || apiResponse.getCancelReasons() == null) {
				logger.severe("Invalid data received: Cancel Reasons list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getCancelReasons().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving Cancel Reasons: Empty Cancel Reasons list");
			}
			return buildResponse(apiResponse.getCancelReasons(),
					CancelReasonsMapper.INSTANCE::CancelReasonsToCancelReasonsDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Cancel Reasons: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Cancel Reasons: " + e.getMessage(), e);
		}
	}

	public List<ChangeReasonsDTO> getChangeReasons(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			ChangeReason200Response apiResponse = changeReasonsApi.changeReason(meta);
			if (apiResponse == null || apiResponse.getChangeReasons() == null) {
				logger.severe("Invalid data received: Change Reasons list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getChangeReasons().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving Change Reasons: Empty Change Reasons list");
			}
			return buildResponse(apiResponse.getChangeReasons(),
					ChangeReasonsMapper.INSTANCE::ChangeReasonsToChangeReasonsDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Change Reasons: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Change Reasons: " + e.getMessage(), e);
		}
	}

	public Object getAppointment(RequestMetaData meta, AppointmentSearchRequestData data) {
		try {
			AppointmentSearchRequest appointmentSearchRequest = new AppointmentSearchRequest();
			appointmentSearchRequest.setMeta(meta);
			appointmentSearchRequest.setData(data);
			setToken();
			Appointments200Response apiResponse = appointmentsApi.appointments(appointmentSearchRequest);
			if (apiResponse == null || apiResponse.getAppointments() == null) {
				logger.severe("Invalid data received: Appointments list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getAppointments().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving Appointments: Empty Appointments list");
			}
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Appointments: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Appointments: " + e.getMessage(), e);
		}
	}

	public Object createPatient(PatientCreateRequest patientCreateRequest) {
		try {
			setToken();
			Patient apiResponse = createPatientApi.patient(patientCreateRequest);
			if (apiResponse == null || apiResponse.getPatientProfileID().isEmpty()) {
				logger.severe("Invalid data received: Patient is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getPatientProfileID().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while creating Patient: Empty Patient data");
			}
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while creating Patient: " + e.getMessage());
			throw new RuntimeException("Error occurred while creating Patient: " + e.getMessage(), e);
		}
	}

	public List<AppointmentType> getAppointmentTypes(RequestMetaData meta) {
		try {
			setToken();
			AppointmentTypes200Response apiResponse = appointmentTypesApi.appointmentTypes(meta);
			if (apiResponse == null || apiResponse.getAppointmentTypes().isEmpty()) {
				logger.severe("Invalid data received: AppointmentTypes is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getAppointmentTypes().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException(
						"Error occurred while retrieving AppointmentTypes: Empty AppointmentTypes data");
			}
			return apiResponse.getAppointmentTypes();
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving AppointmentTypes: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving AppointmentTypes: " + e.getMessage(), e);
		}
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
