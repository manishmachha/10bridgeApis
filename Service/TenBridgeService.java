package com.ps.tenbridge.datahub.controllerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;

import com.ps.tenbridge.datahub.config.OAuth2Config;
import com.ps.tenbridge.datahub.dto.AppointmentInfoDTO;
import com.ps.tenbridge.datahub.dto.CPTsDTO;
import com.ps.tenbridge.datahub.dto.CancelReasonsDTO;
import com.ps.tenbridge.datahub.dto.ChangeReasonsDTO;
import com.ps.tenbridge.datahub.dto.EthnicityDTO;
import com.ps.tenbridge.datahub.dto.GendersDTO;
import com.ps.tenbridge.datahub.dto.InsuranceDTO;
import com.ps.tenbridge.datahub.dto.LocationDTO;
import com.ps.tenbridge.datahub.dto.PatientInfoDTO;
import com.ps.tenbridge.datahub.dto.Patients200Response;
import com.ps.tenbridge.datahub.dto.ProviderDTO;
import com.ps.tenbridge.datahub.dto.RacesDTO;
import com.ps.tenbridge.datahub.dto.ReferralSourcesDTO;
import com.ps.tenbridge.datahub.dto.ReferringProviderDTO;
import com.ps.tenbridge.datahub.dto.SearchPatientApi;
import com.ps.tenbridge.datahub.dto.SlotsInfo;
import com.ps.tenbridge.datahub.dto.patientAlerts;
import com.ps.tenbridge.datahub.mapper.CancelReasonsMapper;
import com.ps.tenbridge.datahub.mapper.CarrierMapper;
import com.ps.tenbridge.datahub.mapper.ChangeReasonsMapper;
import com.ps.tenbridge.datahub.mapper.CptMapper;
import com.ps.tenbridge.datahub.mapper.EthnicityMapper;
import com.ps.tenbridge.datahub.mapper.GenderMapper;
import com.ps.tenbridge.datahub.mapper.LocationMapper;
import com.ps.tenbridge.datahub.mapper.PatientAlertsMapper;
import com.ps.tenbridge.datahub.mapper.PatientAppointmentsMapper;
import com.ps.tenbridge.datahub.mapper.PatientSearchMapper;
import com.ps.tenbridge.datahub.mapper.PractitionerMapper;
import com.ps.tenbridge.datahub.mapper.RaceMapper;
import com.ps.tenbridge.datahub.mapper.ReferralSourcesMapper;
import com.ps.tenbridge.datahub.mapper.ReferringProviderMapper;
import com.ps.tenbridge.datahub.mapper.SlotMapper;
import com.ps.tenbridge.datahub.services.authentication.TokenService;
import com.ps.tenbridge.datahub.utility.BaseService;
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
import com.veradigm.ps.tenbridge.client.api.GetLocationByIdApi;
import com.veradigm.ps.tenbridge.client.api.GetNotesApi;
import com.veradigm.ps.tenbridge.client.api.GetPatientAlertsApi;
import com.veradigm.ps.tenbridge.client.api.GetPayorGroupsApi;
import com.veradigm.ps.tenbridge.client.api.GetPracticeLocationsApi;
import com.veradigm.ps.tenbridge.client.api.GetProviderByIdApi;
import com.veradigm.ps.tenbridge.client.api.GetProviderScheduleApi;
import com.veradigm.ps.tenbridge.client.api.GetProviderSlotsApi;
import com.veradigm.ps.tenbridge.client.api.GetProvidersApi;
import com.veradigm.ps.tenbridge.client.api.GetRaceValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetReferralSourcesApi;
import com.veradigm.ps.tenbridge.client.api.GetReferringProvidersApi;
import com.veradigm.ps.tenbridge.client.models.Appointment;
import com.veradigm.ps.tenbridge.client.models.AppointmentNotes;
import com.veradigm.ps.tenbridge.client.models.AppointmentNotes200Response;
import com.veradigm.ps.tenbridge.client.models.AppointmentNotesRequest;
import com.veradigm.ps.tenbridge.client.models.AppointmentResponse;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequest;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequestData;
import com.veradigm.ps.tenbridge.client.models.AppointmentType;
import com.veradigm.ps.tenbridge.client.models.AppointmentTypes200Response;
import com.veradigm.ps.tenbridge.client.models.Appointments200Response;
import com.veradigm.ps.tenbridge.client.models.CPT200Response;
import com.veradigm.ps.tenbridge.client.models.CancelAppointmentRequest;
import com.veradigm.ps.tenbridge.client.models.CancelAppointmentResponse;
import com.veradigm.ps.tenbridge.client.models.CancellationReason200Response;
import com.veradigm.ps.tenbridge.client.models.ChangeReason200Response;
import com.veradigm.ps.tenbridge.client.models.Ethnicity200Response;
import com.veradigm.ps.tenbridge.client.models.Gender200Response;
import com.veradigm.ps.tenbridge.client.models.GetLocationById200Response;
import com.veradigm.ps.tenbridge.client.models.GetLocationByIdRequest;
import com.veradigm.ps.tenbridge.client.models.GetProviderById200Response;
import com.veradigm.ps.tenbridge.client.models.GetProviderByIdRequest;
import com.veradigm.ps.tenbridge.client.models.InsuranceCarrier;
import com.veradigm.ps.tenbridge.client.models.Location;
import com.veradigm.ps.tenbridge.client.models.NewAppointmentRequest;
import com.veradigm.ps.tenbridge.client.models.Patient;
import com.veradigm.ps.tenbridge.client.models.PatientAlert200Response;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequest;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequestBody;
import com.veradigm.ps.tenbridge.client.models.PatientCreateRequest;
import com.veradigm.ps.tenbridge.client.models.PatientRequest;
import com.veradigm.ps.tenbridge.client.models.PayorGroups200Response;
import com.veradigm.ps.tenbridge.client.models.PracticeLocation200Response;
import com.veradigm.ps.tenbridge.client.models.Practitioner;
import com.veradigm.ps.tenbridge.client.models.ProviderSlots200Response;
import com.veradigm.ps.tenbridge.client.models.Providers200Response;
import com.veradigm.ps.tenbridge.client.models.Race200Response;
import com.veradigm.ps.tenbridge.client.models.ReferralSource200Response;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;
import com.veradigm.ps.tenbridge.client.models.Schedule200Response;
import com.veradigm.ps.tenbridge.client.models.ScheduleRequest;
import com.veradigm.ps.tenbridge.client.models.SlotRequest;
import com.veradigm.ps.tenbridge.client.models.SlotRequestData;

@Service
public class TenBridgeService extends BaseService {

	private static final Logger logger = Logger.getLogger(TenBridgeService.class.getName());
	private final TokenService tokenService;

	private final ApiClient apiClient;
	@Autowired
	private final GetProvidersApi providersApi;
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
	private SearchPatientApi searchPatient;
	@Autowired
	private GetPatientAlertsApi patientAlertsApi;
	@Autowired
	private GetProviderSlotsApi slotsApi;
	@Autowired
	private GetAvailableCancelReasonsApi cancelReasonsApi;
	@Autowired
	private GetAvailableChangeReasonsApi changeReasonsApi;
	@Autowired
	private GetReferralSourcesApi referralSourcesApi;
	@Autowired
	private GetProviderByIdApi getProviderByIdApi;
	@Autowired
	private GetLocationByIdApi getLocationByIdApi;
	@Autowired
	private GetAppontmentsApi appointmentsApi;
	@Autowired
	private GetGenderValuesApi genderValuesApi;
	@Autowired
	private GetCptValuesApi cptsApi;
	@Autowired
	private CreatePatientApi createPatientApi;
	@Autowired
	private GetAppointmentTypesApi appointmentTypesApi;
	@Autowired
	private GetNotesApi appointmentNotesApi;
	@Autowired
	private CancelAppointmentApi cancelAppointmentApi;
	@Autowired
	private GetProviderScheduleApi providerScheduleApi;
	@Autowired
	private BookAppointmentApi bookAppointmentApi;
	@Autowired
	private CacheManager cacheManager;

	private final OAuth2Config oauth;

	public TenBridgeService(ApiClient apiClient, TokenService ts, OAuth2Config oauth, GetProvidersApi providersApi,
			GetPracticeLocationsApi locationsApi, GetPayorGroupsApi payorGroupsApi,
			GetReferringProvidersApi referringProvidersApi, GetEthnicityValuesApi getEthnicityValuesApi,
			GetRaceValuesApi raceValuesApi, SearchPatientApi searchPatient, GetPatientAlertsApi patientAlertsApi,
			GetReferralSourcesApi referralSourcesApi, GetAvailableCancelReasonsApi cancelReasonsApi,
			GetAvailableChangeReasonsApi changeReasonsApi, GetProviderByIdApi singlePractitionerApi,
			CacheManager cacheManager, GetProviderByIdApi providerByIdApi, GetLocationByIdApi locationByIdApi,
			GetAppontmentsApi getAppontmentsApi, GetGenderValuesApi genderValuesApi, GetProviderSlotsApi slotsApi,
			CreatePatientApi createPatientApi, BookAppointmentApi bookAppointmentApi, GetCptValuesApi cptValuesApi,
			CancelAppointmentApi cancelAppointmentApi) {

		this.apiClient = apiClient;
		this.tokenService = ts;
		this.oauth = oauth;
		this.providersApi = providersApi;
		this.locationsApi = locationsApi;
		this.payerGroupApi = payorGroupsApi;
		this.refferingProviderApi = referringProvidersApi;
		this.ethnicityApi = getEthnicityValuesApi;
		this.racesApi = raceValuesApi;
		this.searchPatient = searchPatient;
		this.patientAlertsApi = patientAlertsApi;
		this.referralSourcesApi = referralSourcesApi;
		this.cancelReasonsApi = cancelReasonsApi;
		this.changeReasonsApi = changeReasonsApi;
		this.getProviderByIdApi = singlePractitionerApi;
		this.cacheManager = cacheManager;
		this.getProviderByIdApi = providerByIdApi;
		this.getLocationByIdApi = locationByIdApi;
		this.appointmentsApi = getAppontmentsApi;
		this.genderValuesApi = genderValuesApi;
		this.slotsApi = slotsApi;
		this.createPatientApi = createPatientApi;
		this.bookAppointmentApi = bookAppointmentApi;
		this.cptsApi = cptValuesApi;
		this.cancelAppointmentApi = cancelAppointmentApi;
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
				logger.severe("Invalid data received: location list is null");
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
				logger.severe("Invalid data received: insurances list is null");
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
				logger.severe("Invalid data received: referring providers list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getProviders().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException(
						"Error occurred while retrieving referring providers: Empty referring provider list");
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
				throw new RuntimeException("Error occurred while retrieving ethnicities: Empty ethnicities list");
			}
			return buildResponse(apiResponse.getEthnicities(), EthnicityMapper.INSTANCE::EthnicityToEthnicityDTO);

		} catch (Exception e) {
			logger.severe("Error occurred while retrieving ethnicities: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving ethnicities: " + e.getMessage(), e);
		}
	}

	public List<RacesDTO> getRaces(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			Race200Response apiResponse = racesApi.race(meta);
			if (apiResponse == null || apiResponse.getRaces() == null) {
				logger.severe("Invalid data received: races list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getRaces().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving races: Empty races list");
			}
			return buildResponse(apiResponse.getRaces(), RaceMapper.INSTANCE::RaceToRacesDTO);

		} catch (Exception e) {
			logger.severe("Error occurred while retrieving races: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving races: " + e.getMessage(), e);
		}
	}

	public List<PatientInfoDTO> getPatients(String siteID, String customerName, String first_name, String last_name,
			String date_of_birth, String patientProfileId, String patientNumber, String practiceId) {
		try {
			PatientRequest patientRequest = createPatientRequest(siteID, customerName, first_name, last_name,
					date_of_birth);
			setToken();

			Patients200Response apiResponse = searchPatient.patients(patientRequest);

			if (apiResponse == null || apiResponse.getPatients() == null) {
				logger.severe("Invalid data received: Patients list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getPatients().isEmpty()) {
				logger.severe("API returned empty list");
				return new ArrayList<PatientInfoDTO>();
			}

			PatientInfoDTO patientInfo = new PatientInfoDTO();

			patientInfo.setPatientProfileId(patientProfileId);
			patientInfo.setPatientId(patientNumber);
			List<PatientInfoDTO> response = PatientSearchMapper.INSTANCE.mapPatientsWithAdditionalFields(apiResponse);
			response.forEach(patientInfo1 -> {
				String practitionerId = patientInfo1.getPractitionerId(); // Get practitionerId from patientInfo1

				ProviderDTO provider = new ProviderDTO(); // Initialize provider with all null values
				ReferringProviderDTO ReferringProvider = new ReferringProviderDTO();

				if (practitionerId != null && !practitionerId.isEmpty()) {
					// Fetch the practitioner details
					GetProviderById200Response providerDetailsById = getProviderDetailsById(siteID, customerName,
							practitionerId);

					if (providerDetailsById != null && providerDetailsById.getProviders() != null
							&& !providerDetailsById.getProviders().isEmpty()) {

						Practitioner providerDetails = providerDetailsById.getProviders().get(0);

						// Use MapStruct to map Practitioner to ProviderDTO
						provider = PractitionerMapper.INSTANCE.practitionerToProviderDTO(providerDetails);
					}
				}

				patientInfo1.setPrefDoc(provider);
				// Fetch practitioner details if practitionerId is not null or empty
				patientInfo1.setRefDoc(ReferringProvider);
			});
			return response;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Patients: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Patients: " + e.getMessage(), e);
		}
	}

	public patientAlerts getPatientAlerts(String siteID, String customerName, String patientProfileId) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			PatientAlertsRequest patientAlertRequest = new PatientAlertsRequest();
			patientAlertRequest.setMeta(meta);
			PatientAlertsRequestBody body = new PatientAlertsRequestBody();
			body.setPatientProfileId(patientProfileId);
			patientAlertRequest.setBody(body);
			setToken();
			PatientAlert200Response apiResponse = patientAlertsApi.patientAlert(patientAlertRequest);

			if (apiResponse == null || apiResponse.getPatientAlerts() == null) {
				logger.severe("Invalid data received: Patient Alerts list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getPatientAlerts().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving Patient Alerts: Empty Patient Alerts list");
			}
			patientAlerts response = PatientAlertsMapper.INSTANCE.toTargetDTO(apiResponse.getPatientAlerts());
			return response;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving PatientAlerts: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving PatientAlerts: " + e.getMessage(), e);
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

	public GetProviderById200Response getProviderDetailsById(String siteID, String customerName,
			String practitionerId) {
		Cache cache = cacheManager.getCache("providerCache");
		GetProviderById200Response cachedResponse = cache.get(practitionerId, GetProviderById200Response.class);

		if (cachedResponse != null) {
			logger.info("Returning provider details from cache for ID: " + practitionerId);
			return cachedResponse;
		}

		try {
			setToken();
			GetProviderByIdRequest getProviderByIdRequest = createGetProviderByIdRequest(siteID, customerName,
					practitionerId);
			GetProviderById200Response apiResponse = getProviderByIdApi.getProviderById(getProviderByIdRequest);

			if (apiResponse == null || apiResponse.getProviders() == null) {
				logger.severe("Invalid data received: Practitioner is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getProviders().isEmpty()) {
				logger.severe("API returned empty data");
				return new GetProviderById200Response();
			}

			logger.info("Fetching provider details from API for ID: " + practitionerId);
			cache.put(practitionerId, apiResponse); // Store in cache
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Practitioner: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Practitioner: " + e.getMessage(), e);
		}
	}

	public GetLocationById200Response getLocationById(String siteID, String customerName, String locationId) {
		Cache cache = cacheManager.getCache("locationCache");
		GetLocationById200Response cachedResponse = cache.get(locationId, GetLocationById200Response.class);

		if (cachedResponse != null) {
			logger.info("Returning location details from cache for ID: " + locationId);
			return cachedResponse;
		}

		try {
			setToken();
			GetLocationByIdRequest getLocationByIdRequest = createGetLoactionByIdRequest(siteID, customerName,
					locationId);
			GetLocationById200Response apiResponse = getLocationByIdApi.getLocationById(getLocationByIdRequest);

			if (apiResponse == null || apiResponse.getLocations() == null) {
				logger.severe("Invalid data received: Location is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getLocations().isEmpty()) {
				logger.severe("API returned empty data");
				return new GetLocationById200Response();
			}

			logger.info("Fetching location details from API for ID: " + locationId);
			cache.put(locationId, apiResponse); // Store in cache
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving location: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving location: " + e.getMessage(), e);
		}
	}

	public List<AppointmentInfoDTO> getAppointment(String siteID, String customerName, String patient_id) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			AppointmentSearchRequestData data = new AppointmentSearchRequestData();
			data.setPatientId(patient_id);
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

			List<AppointmentInfoDTO> response = PatientAppointmentsMapper.INSTANCE
					.mapAppointmentsWithAdditionalFields(apiResponse);

			// Collecting unique locationIds and practitionerIds
			Set<String> uniqueLocationIds = new HashSet<>();
			Set<String> uniquePractitionerIds = new HashSet<>();

			for (AppointmentInfoDTO appointment : response) {
				if (appointment.getLocationId() != null) {
					uniqueLocationIds.add(appointment.getLocationId());
				}
				if (appointment.getPractitionerId() != null) {
					uniquePractitionerIds.add(appointment.getPractitionerId());
				}
			}

			// Fetching provider and location details once and store in maps
			Map<String, ProviderDTO> providerMap = new HashMap<>();
			for (String practitionerId : uniquePractitionerIds) {
				GetProviderById200Response providerResponse = getProviderDetailsById(siteID, customerName,
						practitionerId);
				if (providerResponse != null && providerResponse.getProviders() != null
						&& !providerResponse.getProviders().isEmpty()) {
					providerMap.put(practitionerId, PractitionerMapper.INSTANCE
							.practitionerToProviderDTO(providerResponse.getProviders().get(0)));
				}
			}

			Map<String, LocationDTO> locationMap = new HashMap<>();
			for (String locationId : uniqueLocationIds) {
				GetLocationById200Response locationResponse = getLocationById(siteID, customerName, locationId);
				if (locationResponse != null && locationResponse.getLocations() != null
						&& !locationResponse.getLocations().isEmpty()) {
					locationMap.put(locationId,
							LocationMapper.INSTANCE.LocationToLocationDTO(locationResponse.getLocations().get(0)));
				}
			}

			// Map the provider and location details to each appointment
			for (AppointmentInfoDTO appointment : response) {
				appointment.setDoctorInfo(providerMap.getOrDefault(appointment.getPractitionerId(), new ProviderDTO()));
				appointment.setLocationInfo(locationMap.getOrDefault(appointment.getLocationId(), new LocationDTO()));
			}

			return response;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Appointments: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Appointments: " + e.getMessage(), e);
		}
	}

	public List<SlotsInfo> getSlots(String siteID, String customerName, String appointmentType, String startDate) {
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
				return new ArrayList<SlotsInfo>();
			}
			return buildResponse(apiResponse.getSlots(), SlotMapper.INSTANCE::mapToTarget);
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Error occurred while retrieving Slots: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Slots: " + e.getMessage(), e);
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

	public Object createPatient(String siteID, String customerName, String first_name, String last_name,
			String middle_name, String date_of_birth, String gender, String phone, String address_line_1,
			String address_line_2, String state, String city, String zip, String email) {
		PatientCreateRequest patientCreateRequest = cratePatientCreateRequest(siteID, customerName, first_name,
				last_name, middle_name, date_of_birth, gender, phone, address_line_1, address_line_2, state, city, zip,
				email);
		try {
			setToken();
			Patient apiResponse = createPatientApi.patient(patientCreateRequest);
			if (apiResponse == null || apiResponse.getFirstName() == null) {
				logger.severe("Invalid data received: Patient is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			return apiResponse;
		} catch (Exception e) {
			e.printStackTrace();
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

	public List<AppointmentNotes> getAppointmentNotes(String siteID, String customerName, String patientProfileId,
			String appointmentId) {
		AppointmentNotesRequest appointmentNotesRequest = createAppointmentNotesRequest(siteID, customerName,
				patientProfileId, appointmentId);
		try {
			setToken();
			AppointmentNotes200Response apiResponse = appointmentNotesApi.appointmentNotes(appointmentNotesRequest);
			if (apiResponse == null || apiResponse.getNotes() == null) {
				logger.severe("Invalid data received: AppointmentNotes is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getNotes() == null) {
				logger.severe("API returned empty list");
				throw new RuntimeException(
						"Error occurred while retrieving AppointmentNotes: Empty AppointmentTypes data");
			}
			return apiResponse.getNotes();
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving AppointmentNotes: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving AppointmentNotes: " + e.getMessage(), e);
		}
	}

	public List<InsuranceCarrier> getInsuranceCarriers(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			PayorGroups200Response apiResponse = payerGroupApi.payorGroups(meta);

			if (apiResponse == null || apiResponse.getCarriers() == null) {
				logger.severe("Invalid data received: Insurance carriers list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getCarriers().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException(
						"Error occurred while retrieving insurance carriers: Empty insurance carriers list");
			}
			return apiResponse.getCarriers();
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving insurance carriers: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving insurance carriers: " + e.getMessage(), e);
		}

	}

	public CancelAppointmentResponse cancelAppointment(String siteID, String customerName,
			String requested_appointment_id) {
		try {
			CancelAppointmentRequest cancelAppointmentRequest = createCancelAppointmentRequest(siteID, customerName,
					requested_appointment_id);
			setToken();
			CancelAppointmentResponse apiResponse = cancelAppointmentApi.cancelAppointment(cancelAppointmentRequest);
			apiResponse.setAppointmentId(requested_appointment_id);
			if (apiResponse == null || apiResponse.getAppointmentId() == null) {
				logger.severe("Invalid data received: appointment is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while cancelling appointment: " + e.getMessage());
			throw new RuntimeException("Error occurred while cancelling appointment: " + e.getMessage(), e);
		}
	}

	public Object scheduleSearch(String siteID, String customerName, String patientProfileId, String start_date) {
		try {
			setToken();
			ScheduleRequest scheduleRequest = createScheduleRequest(siteID, customerName, patientProfileId, start_date);
			Schedule200Response apiResponse = providerScheduleApi.schedule(scheduleRequest);
			if (apiResponse == null || apiResponse.getSchedule().isEmpty()) {
				logger.severe("Invalid data received: schedule is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getSchedule().isEmpty()) {
				logger.severe("API returned empty data");
				throw new RuntimeException("Error occurred while retrieving schedule: Empty schedule");
			}
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving schedule: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving schedule: " + e.getMessage(), e);
		}
	}

	public List<AppointmentInfoDTO> bookAppointment(String siteID, String customerName, String appointmentDate,
			int appointmentDuration, String appointmentDateTime, String appointmentType, String appointmentStatus,
			String appointmentCreatedDate, String appointmentBookingDate, String appointmentBookedBy,
			String coverageType, String visitType, String appointmentStartTime, String appointmentEndTime,
			String scheduledLocationId, String scheduledProviderId, String scheduledDepartment,
			String referringProviderId, String requestedAppointmentId, String patientIdentifier,
			String notesOrComments) {
		try {
			setToken();
			NewAppointmentRequest bookAppointmentRequest = createBookAppointmentRequest(siteID, customerName,
					appointmentDate, appointmentDuration, appointmentDateTime, appointmentType, appointmentStatus,
					appointmentCreatedDate, appointmentBookingDate, appointmentBookedBy, coverageType, visitType,
					appointmentStartTime, appointmentEndTime, scheduledLocationId, scheduledProviderId,
					scheduledDepartment, referringProviderId, requestedAppointmentId, patientIdentifier,
					notesOrComments);

			Appointment apiResponse = bookAppointmentApi.bookAppointment(bookAppointmentRequest);
			Appointments200Response apiResponse2 = new Appointments200Response();
			apiResponse2.setAppointments(List.of(apiResponse));
			if (apiResponse == null || apiResponse.getAppointmentId() == null) {
				logger.severe("Invalid data received: Appointments list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getAppointmentId().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while creating appointment: Empty Appointments list");
			}

			List<AppointmentInfoDTO> response = PatientAppointmentsMapper.INSTANCE
					.mapAppointmentsWithAdditionalFields(apiResponse2);

			response.forEach(appointment -> {
				String LocationId = appointment.getLocationId(); // get locationID from appointment
				LocationDTO location = new LocationDTO(); // Initialize location with all null values
				if (LocationId != null) {
					// Fetch the Location details
					GetLocationById200Response singleLocation = getLocationById(siteID, customerName, LocationId);
					if (singleLocation != null && singleLocation.getLocations() != null
							&& !singleLocation.getLocations().isEmpty()) {
						Location locationDetails = singleLocation.getLocations().get(0);

						// Use MapStruct to map Practitioner to ProviderDTO
						location = LocationMapper.INSTANCE.LocationToLocationDTO(locationDetails);
					}
				}

				String practitionerId = appointment.getPractitionerId(); // get practitionerId from appointment
				ProviderDTO practitioner = new ProviderDTO(); // Initialize practitioner with all null values
				if (practitionerId != null) {
					// Fetch the practitioner details
					GetProviderById200Response singlePractitioner = getProviderDetailsById(siteID, customerName,
							practitionerId);

					if (singlePractitioner != null && singlePractitioner.getProviders() != null
							&& !singlePractitioner.getProviders().isEmpty()) {

						Practitioner providerDetails = singlePractitioner.getProviders().get(0);

						// Use MapStruct to map Practitioner to ProviderDTO
						practitioner = PractitionerMapper.INSTANCE.practitionerToProviderDTO(providerDetails);
					}
				}
				appointment.setDoctorInfo(practitioner);
				appointment.setLocationInfo(location);
			});

			return response;
		} catch (Exception e) {
			logger.severe("Error occurred while creating Appointment: " + e.getMessage());
			throw new RuntimeException("Error occurred while creating Appointment: " + e.getMessage(), e);
		}
	}

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
