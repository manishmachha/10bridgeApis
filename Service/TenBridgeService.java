package com.ps.tenbridge.datahub.controllerImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

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
import com.ps.tenbridge.datahub.services.authentication.TokenService;
import com.ps.tenbridge.datahub.utility.BaseService;
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
import com.veradigm.ps.tenbridge.client.models.CancellationReason200Response;
import com.veradigm.ps.tenbridge.client.models.ChangeReason200Response;
import com.veradigm.ps.tenbridge.client.models.Ethnicity200Response;
import com.veradigm.ps.tenbridge.client.models.Gender200Response;
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
import com.veradigm.ps.tenbridge.client.models.SingleLocation200Response;
import com.veradigm.ps.tenbridge.client.models.SingleLocationRequest;
import com.veradigm.ps.tenbridge.client.models.SinglePractitioner200Response;
import com.veradigm.ps.tenbridge.client.models.SinglePractitionerRequest;
import com.veradigm.ps.tenbridge.client.models.SlotRequest;
import com.veradigm.ps.tenbridge.client.models.SlotRequestData;

@Service
@EnableAsync
@EnableCaching
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
	@Autowired
	private GetNotesApi appointmentNotesApi;
	@Autowired
	private CancelAppointmentApi cancelAppointmentApi;
	@Autowired
	private GetProviderScheduleApi providerScheduleApi;
	@Autowired
	private GetSingleLocationApi singleLocationApi;
	@Autowired
	private GetSinglePractitionerApi singlePractitionerApi;
	@Autowired
	private BookAppointmentApi bookAppointmentApi;
	@Autowired
	private SingleLocationsUtil singleLocationBulkApi;
	@Autowired
	private SinglePractitionersUtil singlePractitionerBulkApi;
	@Autowired
	private CacheManager cacheManager;
	private final OAuth2Config oauth;

	public TenBridgeService(ApiClient apiClient, TokenService ts, OAuth2Config oauth, GetProvidersApi providersApi,
			GetPracticeLocationsApi locationsApi, GetPayorGroupsApi payorGroupsApi,
			GetReferringProvidersApi referringProvidersApi, GetEthnicityValuesApi getEthnicityValuesApi,
			GetRaceValuesApi raceValuesApi, GetReferralSourcesApi referralSourcesApi,
			GetPatientAlertsApi patientAlertsApi, GetGenderValuesApi genderValuesApi, GetCptValuesApi cptsApi,
			GetAvailableCancelReasonsApi cancelReasonsApi, GetAvailableChangeReasonsApi changeReasonsApi,
			GetAppontmentsApi getAppontmentsApi, GetProviderSlotsApi providerSlotsApi, CreatePatientApi patientApi,
			SearchPatientApi searchPatientApi, GetSinglePractitionerApi singlePractitionerApi,
			GetSingleLocationApi singleLocationApi, BookAppointmentApi bookAppointmentApi) {
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
		this.singlePractitionerApi = singlePractitionerApi;
		this.singleLocationApi = singleLocationApi;
		this.bookAppointmentApi = bookAppointmentApi;
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
				throw new RuntimeException("Error occurred while retrieving ethnicities: Empty Ethnicities list");
			}
			return buildResponse(apiResponse.getEthnicities(), EthnicityMapper.INSTANCE::EthnicityToEthnicityDTO);

		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Ethnicities: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Ethnicities: " + e.getMessage(), e);
		}
	}

//	public List<PatientInfoDTO> getPatients(String siteID, String customerName, String first_name, String last_name,
//			String date_of_birth, String patientProfileId, String patientNumber, String practiceId) {
//		try {
//			PatientRequest patientRequest = createPatientRequest(siteID, customerName, first_name, last_name,
//					date_of_birth);
//			setToken();
//
//			Patients200Response apiResponse = searchPatient.patients(patientRequest);
//
//			if (apiResponse == null || apiResponse.getPatients() == null) {
//				logger.severe("Invalid data received: Patients list is null");
//				throw new RuntimeException("Error occurred while building response: Invalid data received");
//			}
//			if (apiResponse.getPatients().isEmpty()) {
//				logger.severe("API returned empty list");
//				return new ArrayList<PatientInfoDTO>();
//			}
//			ProviderDTO Provider = new ProviderDTO();
//			ReferringProviderDTO ReferringProvider = new ReferringProviderDTO();
//
//			PatientInfoDTO patientInfo = new PatientInfoDTO();
//
//			patientInfo.setPatientProfileId(patientProfileId);
//			patientInfo.setPatientId(patientNumber);
//			List<PatientInfoDTO> response = PatientSearchMapper.INSTANCE.mapPatientsWithAdditionalFields(apiResponse,
//					Provider, ReferringProvider);
//			response.forEach(patientInfo1 -> {
//				String practitionerId = patientInfo1.getPractitionerId(); // Get practitionerId from patientInfo1
//
//				ProviderDTO provider = new ProviderDTO(); // Initialize provider with all null values
//
//				if (practitionerId != null && !practitionerId.isEmpty()) {
//					// Fetch the practitioner details
//					SinglePractitioner200Response singlePractitioner = getSinglePractitioner(siteID, customerName,
//							practitionerId);
//
//					if (singlePractitioner != null && singlePractitioner.getProviders() != null
//							&& !singlePractitioner.getProviders().isEmpty()) {
//
//						Practitioner providerDetails = singlePractitioner.getProviders().get(0);
//
//						// Use MapStruct to map Practitioner to ProviderDTO
//						provider = PractitionerMapper.INSTANCE.practitionerToProviderDTO(providerDetails);
//					}
//				}
//
//				patientInfo1.setPrefDoc(provider);
//			});
//			return response;
//		} catch (Exception e) {
//			logger.severe("Error occurred while retrieving Patients: " + e.getMessage());
//			throw new RuntimeException("Error occurred while retrieving Patients: " + e.getMessage(), e);
//		}
//	}

//	public List<AppointmentInfoDTO> getAppointment(String siteID, String customerName, String patient_id) {
//		try {
//			RequestMetaData meta = createRequestMetaData(siteID, customerName);
//			AppointmentSearchRequestData data = new AppointmentSearchRequestData();
//			data.setPatientId(patient_id);
//			AppointmentSearchRequest appointmentSearchRequest = new AppointmentSearchRequest();
//			appointmentSearchRequest.setMeta(meta);
//			appointmentSearchRequest.setData(data);
//			setToken();
//
//			Appointments200Response apiResponse = appointmentsApi.appointments(appointmentSearchRequest);
//			if (apiResponse == null || apiResponse.getAppointments() == null) {
//				logger.severe("Invalid data received: Appointments list is null");
//				throw new RuntimeException("Error occurred while building response: Invalid data received");
//			}
//			if (apiResponse.getAppointments().isEmpty()) {
//				logger.severe("API returned empty list");
//				throw new RuntimeException("Error occurred while retrieving Appointments: Empty Appointments list");
//			}
//
//			List<AppointmentInfoDTO> response = PatientAppointmentsMapper.INSTANCE
//					.mapAppointmentsWithAdditionalFields(apiResponse);
//
//			response.forEach(appointment -> {
//				String LocationId = appointment.getLocationId(); // get locationID from appointment
//				LocationDTO location = new LocationDTO(); // Initialize location with all null values
//				if (LocationId != null) {
//					// Fetch the Location details
//					SingleLocation200Response singleLocation = getSingleLocation(siteID, customerName, LocationId);
//					if (singleLocation != null && singleLocation.getLocations() != null
//							&& !singleLocation.getLocations().isEmpty()) {
//						Location locationDetails = singleLocation.getLocations().get(0);
//
//						// Use MapStruct to map Practitioner to ProviderDTO
//						location = LocationMapper.INSTANCE.LocationToLocationDTO(locationDetails);
//					}
//				}
//
//				String practitionerId = appointment.getPractitionerId(); // get practitionerId from appointment
//				ProviderDTO practitioner = new ProviderDTO(); // Initialize practitioner with all null values
//				if (practitionerId != null) {
//					// Fetch the practitioner details
//					SinglePractitioner200Response singlePractitioner = getSinglePractitioner(siteID, customerName,
//							practitionerId);
//
//					if (singlePractitioner != null && singlePractitioner.getProviders() != null
//							&& !singlePractitioner.getProviders().isEmpty()) {
//
//						Practitioner providerDetails = singlePractitioner.getProviders().get(0);
//
//						// Use MapStruct to map Practitioner to ProviderDTO
//						practitioner = PractitionerMapper.INSTANCE.practitionerToProviderDTO(providerDetails);
//					}
//				}
//				appointment.setDoctorInfo(practitioner);
//				appointment.setLocationInfo(location);
//			});
//
//			return response;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.severe("Error occurred while retrieving Appointments: " + e.getMessage());
//			throw new RuntimeException("Error occurred while retrieving Appointments: " + e.getMessage(), e);
//		}
//	}

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
				throw new RuntimeException("Error occurred while retrieving Patients: Empty Patients list");
			}

			List<PatientInfoDTO> response = PatientSearchMapper.INSTANCE.mapPatientsWithAdditionalFields(apiResponse,
					new ProviderDTO(), new ReferringProviderDTO());

			Set<String> practitionerIds = response.stream().map(PatientInfoDTO::getPractitionerId)
					.filter(Objects::nonNull).collect(Collectors.toSet());

			// Map<String, ProviderDTO> practitionerMap = getPractitionersInBulk(siteID,
			// customerName, practitionerIds).get();
			Map<String, ProviderDTO> practitionerMap = getPractitionersInCache(siteID, customerName, practitionerIds);

			response.forEach(patientInfo -> {
				patientInfo.setPrefDoc(practitionerMap.get(patientInfo.getPractitionerId()));
			});

			return response;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Patients: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Patients: " + e.getMessage(), e);
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

			// Map basic fields using MapStruct
			List<AppointmentInfoDTO> response = PatientAppointmentsMapper.INSTANCE
					.mapAppointmentsWithAdditionalFields(apiResponse);

			// Collect unique LocationIds and PractitionerIds
			Set<String> locationIds = response.stream().map(AppointmentInfoDTO::getLocationId).filter(Objects::nonNull)
					.collect(Collectors.toSet());

			Set<String> practitionerIds = response.stream().map(AppointmentInfoDTO::getPractitionerId)
					.filter(Objects::nonNull).collect(Collectors.toSet());

			// Fetch all locations and practitioners in bulk

			// Map<String, LocationDTO> locationMap = getLocationsInBulk(siteID,
			// customerName, locationIds).get();
			Map<String, LocationDTO> locationMap = getLocationsInCache(siteID, customerName, locationIds);

			// Map<String, ProviderDTO> practitionerMap = getPractitionersInBulk(siteID,
			// customerName, practitionerIds).get();
			Map<String, ProviderDTO> practitionerMap = getPractitionersInCache(siteID, customerName, practitionerIds);

			// Assign fetched data to appointments
			response.forEach(appointment -> {
				if (locationMap.containsKey(appointment.getLocationId())) {
					appointment.setLocationInfo(locationMap.get(appointment.getLocationId()));
				} else {
					appointment.setLocationInfo(new LocationDTO());
				}
				if (practitionerMap.containsKey(appointment.getPractitionerId())) {
					appointment.setDoctorInfo(practitionerMap.get(appointment.getPractitionerId()));
				} else {
					appointment.setDoctorInfo(new ProviderDTO());
				}
			});

			return response;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Appointments: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Appointments: " + e.getMessage(), e);
		}
	}

	@Autowired
	private RedisTemplate<String, LocationDTO> redisLocationTemplate;
//
//	@Autowired
//	private RedisTemplate<String, ProviderDTO> redisPractitionerTemplate;

//	public ProviderDTO fetchPractitioner(String siteID, String customerName, String practitionerId) {
//		ValueOperations<String, ProviderDTO> ops = redisPractitionerTemplate.opsForValue();
//		String redisKey = "practitioner:" + practitionerId;
//		logger.info("REDIS PRACTITIONER KEY: " + redisKey);
//		ProviderDTO cachedPractitioner = ops.get(redisKey);
//		logger.info("CACHED DATA: " + cachedPractitioner);
//
//		if (cachedPractitioner != null) {
//			logger.info("CACHE HIT for practitionerId: " + practitionerId);
//			return cachedPractitioner;
//		}
//
//		logger.warning("CACHE MISS for practitionerId: " + practitionerId);
//		try {
//			logger.info("HITTING API for practitionerId: " + practitionerId);
//			List<SinglePractitioner200Response> practitionerResponses = singlePractitionerBulkApi
//					.singlePractitioners(siteID, customerName, new ArrayList<>(List.of(practitionerId)));
//			if (practitionerResponses != null && !practitionerResponses.isEmpty()
//					&& practitionerResponses.get(0).getProviders() != null
//					&& !practitionerResponses.get(0).getProviders().isEmpty()) {
//				ProviderDTO providerDTO = PractitionerMapper.INSTANCE
//						.practitionerToProviderDTO(practitionerResponses.get(0).getProviders().get(0));
//				ops.set("practitioner:" + practitionerId, providerDTO);
//				System.out.println("Stored in cache with key: " + practitionerId + ", value: " + providerDTO);
//				return providerDTO;
//			}
//		} catch (RestClientResponseException e) {
//			logger.severe("Error fetching practitioner for ID " + practitionerId + ": " + e.getMessage());
//		}
//		return null;
//	}
//
//	public Map<String, ProviderDTO> getPractitionersInBulk(String siteID, String customerName,
//			Set<String> practitionerIds) {
//		setToken();
//
//		if (practitionerIds.isEmpty()) {
//			return Collections.emptyMap();
//		}
//
//		Map<String, ProviderDTO> practitionersMap = new HashMap<>();
//		System.out.println("Processing practitioners in bulk");
//
//		for (String practitionerId : practitionerIds) {
//			ProviderDTO providerDTO = fetchPractitioner(siteID, customerName, practitionerId);
//			if (providerDTO != null) {
//				practitionersMap.put(providerDTO.getProviderId(), providerDTO);
//			}
//		}
//
//		System.out.println("Completed processing practitioners in bulk");
//		return practitionersMap;
//	}

	@Cacheable(value = "practitioners", key = "'practitioner:' + #practitionerId") // Ensures that API-fetched data is
																					// stored in cache
	public ProviderDTO fetchPractitioner(String siteID, String customerName, String practitionerId) {
		Cache cache = cacheManager.getCache("practitioners");

		// Check if data is present in cache
		if (cache != null) {
			Cache.ValueWrapper cachedValue = cache.get("practitioner:" + practitionerId);
			if (cachedValue != null) {
				logger.info("Cache HIT: Returning practitionerId " + practitionerId + " from cache.");
				return (ProviderDTO) cachedValue.get();
			}
		}

		// If not found in cache, fetch from API
		logger.info("Cache MISS: HITTING API for practitionerId: " + practitionerId);
		try {
			List<SinglePractitioner200Response> practitionerResponses = singlePractitionerBulkApi
					.singlePractitioners(siteID, customerName, new ArrayList<>(List.of(practitionerId)));

			if (practitionerResponses != null && !practitionerResponses.isEmpty()
					&& practitionerResponses.get(0).getProviders() != null
					&& !practitionerResponses.get(0).getProviders().isEmpty()) {

				ProviderDTO providerDTO = PractitionerMapper.INSTANCE
						.practitionerToProviderDTO(practitionerResponses.get(0).getProviders().get(0));

				// Store result in cache
				if (cache != null) {
					cache.put("practitioner:" + practitionerId, providerDTO);
					logger.info("Stored practitionerId " + practitionerId + " in cache.");
				}

				return providerDTO;
			}
		} catch (RestClientResponseException e) {
			logger.severe("Error fetching practitioner for ID " + practitionerId + ": " + e.getMessage());
		}

		return null;
	}

	public Map<String, ProviderDTO> getPractitionersInCache(String siteID, String customerName,
			Set<String> practitionerIds) {
		setToken();
		if (practitionerIds.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, ProviderDTO> practitionersMap = new HashMap<>();
		for (String practitionerId : practitionerIds) {
			ProviderDTO providerDTO = fetchPractitioner(siteID, customerName, practitionerId);
			if (providerDTO != null) {
				practitionersMap.put(providerDTO.getProviderId(), providerDTO);
			}
		}
		return practitionersMap;
	}

//	@Async
//	public CompletableFuture<ProviderDTO> fetchPractitionerAsync(String siteID, String customerName,
//			String practitionerId) {
//		ValueOperations<String, ProviderDTO> ops = redisPractitionerTemplate.opsForValue();
//		String redisKey = "practitioner:" + practitionerId;
//		logger.info("REDIS PRACTITIONER KEY: " + redisKey);
//		ProviderDTO cachedPractitioner = ops.get(redisKey);
//		logger.info("CACHED DATA: " + cachedPractitioner);
//
//		if (cachedPractitioner != null) {
//			logger.info("CACHE HIT for practitionerId: " + practitionerId);
//			return CompletableFuture.completedFuture(cachedPractitioner);
//		}
//
//		logger.warning("CACHE MISS for practitionerId: " + practitionerId);
//		return CompletableFuture.supplyAsync(() -> {
//			try {
//				logger.info("HITTING API for practitionerId: " + practitionerId);
//				List<SinglePractitioner200Response> practitionerResponses = singlePractitionerBulkApi
//						.singlePractitioners(siteID, customerName, new ArrayList<>(List.of(practitionerId)));
//				if (practitionerResponses != null && !practitionerResponses.isEmpty()
//						&& practitionerResponses.get(0).getProviders() != null
//						&& !practitionerResponses.get(0).getProviders().isEmpty()) {
//					ProviderDTO providerDTO = PractitionerMapper.INSTANCE
//							.practitionerToProviderDTO(practitionerResponses.get(0).getProviders().get(0));
//					ops.set("practitioner:" + practitionerId, providerDTO);
//					System.out.println("Stored in cache with key: " + practitionerId + ", value: " + providerDTO);
//					return providerDTO;
//				}
//			} catch (RestClientResponseException e) {
//				logger.severe("Error fetching practitioner for ID " + practitionerId + ": " + e.getMessage());
//			}
//			return null;
//		});
//	}
//
//	public CompletableFuture<Map<String, ProviderDTO>> getPractitionersInBulkAsync(String siteID, String customerName,
//			Set<String> practitionerIds) {
//		setToken();
//
//		if (practitionerIds.isEmpty()) {
//			return CompletableFuture.completedFuture(Collections.emptyMap());
//		}
//
//		List<CompletableFuture<ProviderDTO>> futures = practitionerIds.stream()
//				.map(practitionerId -> fetchPractitionerAsync(siteID, customerName, practitionerId))
//				.collect(Collectors.toList());
//
//		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
//				.thenApply(voidResult -> futures.stream().map(CompletableFuture::join).filter(Objects::nonNull)
//						.collect(Collectors.toMap(ProviderDTO::getProviderId, provider -> provider)));
//	}

//	public LocationDTO fetchLocation(String siteID, String customerName, String locationId) {
//		ValueOperations<String, LocationDTO> ops = redisLocationTemplate.opsForValue();
//		String redisKey = "location:" + locationId;
//		logger.info("REDIS LOCATION KEY: " + redisKey);
//		LocationDTO cachedLocation = ops.get(redisKey);
//		logger.info("CACHED DATA: " + cachedLocation);
//
//		if (cachedLocation != null) {
//			logger.info("CACHE HIT for locationId: " + locationId);
//			return cachedLocation;
//		}
//
//		logger.warning("CACHE MISS for locationId: " + locationId);
//		try {
//			System.out.println("HITTING API for locationId: " + locationId);
//			List<SingleLocation200Response> locationResponses = singleLocationBulkApi.singleLocations(siteID,
//					customerName, new ArrayList<>(List.of(locationId)));
//			if (locationResponses != null && !locationResponses.isEmpty()
//					&& locationResponses.get(0).getLocations() != null
//					&& !locationResponses.get(0).getLocations().isEmpty()) {
//				LocationDTO locationDTO = LocationMapper.INSTANCE
//						.LocationToLocationDTO(locationResponses.get(0).getLocations().get(0));
//				ops.set("location:" + locationId, locationDTO);
//				return locationDTO;
//			}
//		} catch (RestClientResponseException e) {
//			logger.severe("Error fetching location for ID " + locationId + ": " + e.getMessage());
//		}
//		return null;
//	}
//
//	public Map<String, LocationDTO> getLocationsInBulk(String siteID, String customerName, Set<String> locationIds) {
//		setToken();
//
//		if (locationIds.isEmpty()) {
//			return Collections.emptyMap();
//		}
//
//		Map<String, LocationDTO> locationsMap = new HashMap<>();
//
//		for (String locationId : locationIds) {
//			LocationDTO locationDTO = fetchLocation(siteID, customerName, locationId);
//			if (locationDTO != null) {
//				locationsMap.put(locationDTO.getLocationId(), locationDTO);
//			}
//		}
//
//		return locationsMap;
//	}

//	@Async
//	public CompletableFuture<LocationDTO> fetchLocationAsync(String siteID, String customerName, String locationId) {
//		ValueOperations<String, LocationDTO> ops = redisLocationTemplate.opsForValue();
//		String redisKey = "location:" + locationId;
//		logger.info("REDIS LOCATION KEY: " + redisKey);
//		LocationDTO cachedLocation = ops.get(redisKey);
//		logger.info("CACHED DATA: " + cachedLocation);
//
//		if (cachedLocation != null) {
//			logger.info("CACHE HIT for locationId: " + locationId);
//			return CompletableFuture.completedFuture(cachedLocation);
//		}
//
//		logger.warning("CACHE MISS for locationId: " + locationId);
//		return CompletableFuture.supplyAsync(() -> {
//			try {
//				System.out.println("HITTING API for locationId: " + locationId);
//				List<SingleLocation200Response> locationResponses = singleLocationBulkApi.singleLocations(siteID,
//						customerName, new ArrayList<>(List.of(locationId)));
//				if (locationResponses != null && !locationResponses.isEmpty()
//						&& locationResponses.get(0).getLocations() != null
//						&& !locationResponses.get(0).getLocations().isEmpty()) {
//					LocationDTO locationDTO = LocationMapper.INSTANCE
//							.LocationToLocationDTO(locationResponses.get(0).getLocations().get(0));
//					ops.set("location:" + locationId, locationDTO);
//					return locationDTO;
//				}
//			} catch (RestClientResponseException e) {
//				logger.severe("Error fetching location for ID " + locationId + ": " + e.getMessage());
//			}
//			return null;
//		});
//	}
//
//	public CompletableFuture<Map<String, LocationDTO>> getLocationsInBulkAsync(String siteID, String customerName,
//			Set<String> locationIds) {
//		setToken();
//
//		if (locationIds.isEmpty()) {
//			return CompletableFuture.completedFuture(Collections.emptyMap());
//		}
//
//		List<CompletableFuture<LocationDTO>> futures = locationIds.stream()
//				.map(locationId -> fetchLocationAsync(siteID, customerName, locationId)).collect(Collectors.toList());
//
//		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
//				.thenApply(voidResult -> futures.stream().map(CompletableFuture::join).filter(Objects::nonNull)
//						.collect(Collectors.toMap(LocationDTO::getLocationId, location -> location)));
//	}

	@Cacheable(value = "locations", key = "'location:' + #locationId") // Ensures that API-fetched data is stored in
																		// cache
	public LocationDTO fetchLocation(String siteID, String customerName, String locationId) {
		Cache cache = cacheManager.getCache("locations");

		// Check if data is present in cache
		if (cache != null) {
			Cache.ValueWrapper cachedValue = cache.get("location:" + locationId);
			if (cachedValue != null) {
				logger.info("Cache HIT: Returning locationId " + locationId + " from cache.");
				return (LocationDTO) cachedValue.get();
			}
		}

		// If not found in cache, fetch from API
		logger.info("Cache MISS: HITTING API for locationId: " + locationId);
		try {
			List<SingleLocation200Response> locationResponses = singleLocationBulkApi.singleLocations(siteID,
					customerName, new ArrayList<>(List.of(locationId)));

			if (locationResponses != null && !locationResponses.isEmpty()
					&& locationResponses.get(0).getLocations() != null
					&& !locationResponses.get(0).getLocations().isEmpty()) {

				LocationDTO locationDTO = LocationMapper.INSTANCE
						.LocationToLocationDTO(locationResponses.get(0).getLocations().get(0));

				// Store result in cache
				if (cache != null) {
					cache.put("location:" + locationId, locationDTO);
					logger.info("Stored locationId " + locationId + " in cache.");
				}

				return locationDTO;
			}
		} catch (RestClientResponseException e) {
			logger.severe("Error fetching location for ID " + locationId + ": " + e.getMessage());
		}

		return null;
	}

	public Map<String, LocationDTO> getLocationsInCache(String siteID, String customerName, Set<String> locationIds) {
		setToken();
		if (locationIds.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, LocationDTO> locationsMap = new HashMap<>();
		for (String locationId : locationIds) {
			LocationDTO locationDTO = fetchLocation(siteID, customerName, locationId);
			if (locationDTO != null) {
				locationsMap.put(locationDTO.getLocationId(), locationDTO);
			}
		}
		return locationsMap;
	}

	public LocationDTO getLocationFromRedis(String redisKey) {
		ValueOperations<String, LocationDTO> ops = redisLocationTemplate.opsForValue();
		System.out.println("REDIS LOCATION KEY: " + redisKey);
		LocationDTO cachedLocation = ops.get(redisKey);
		System.out.println("CACHED DATA: " + cachedLocation);

		if (cachedLocation != null) {
			System.out.println("CACHE HIT for Redis key: " + redisKey);
			return cachedLocation;
		}

		System.out.println("CACHE MISS for Redis key: " + redisKey);
		return null;
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
				throw new RuntimeException("Error occurred while retrieving PatientAlerts: Empty Patient Alerts list");
			}
			patientAlerts response = PatientAlertsMapper.INSTANCE.toTargetDTO(apiResponse.getPatientAlerts());
			return response;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving PatientAlerts: " + e.getMessage());
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

	public Object cancelAppointment(String siteID, String customerName, String requested_appointment_id) {
		try {
			CancelAppointmentRequest cancelAppointmentRequest = createCancelAppointmentRequest(siteID, customerName,
					requested_appointment_id);
			setToken();
			AppointmentResponse apiResponse = cancelAppointmentApi.cancelAppointment(cancelAppointmentRequest);
			if (apiResponse == null || apiResponse.getAppointmentId() == null) {
				logger.severe("Invalid data received: appointment is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getAppointmentId() == null) {
				logger.severe("API returned empty data");
				throw new RuntimeException("Error occurred while cancelling appointment: Empty appointment");
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

	public SingleLocation200Response getSingleLocation(String siteID, String customerName, String locationId) {
		try {
			setToken();
			SingleLocationRequest singleLocationRequest = createSingleLocationRequest(siteID, customerName, locationId);
			SingleLocation200Response apiResponse = singleLocationApi.singleLocation(singleLocationRequest);
			if (apiResponse == null || apiResponse.getLocations() == null) {
				logger.severe("Invalid data received: Location is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getLocations().isEmpty()) {
				logger.severe("API returned empty data");
				return new SingleLocation200Response();
			}
			System.out.println(apiResponse);
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving location: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving location: " + e.getMessage(), e);
		}
	}

	public SinglePractitioner200Response getSinglePractitioner(String siteID, String customerName,
			String practitionerId) {
		try {
			setToken();
			SinglePractitionerRequest singlePractitionerRequest = createSinglePractitionerRequest(siteID, customerName,
					practitionerId);
			SinglePractitioner200Response apiResponse = singlePractitionerApi
					.singlePractitioner(singlePractitionerRequest);
			if (apiResponse == null || apiResponse.getProviders().isEmpty()) {
				logger.severe("Invalid data received: Practitioner is null");
				return new SinglePractitioner200Response();
//				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getProviders().isEmpty()) {
				logger.severe("API returned empty data");
				return new SinglePractitioner200Response();
//				throw new RuntimeException("Error occurred while retrieving Practitioner: Empty Practitioner");
			}
			System.out.println(apiResponse);
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Practitioner: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Practitioner: " + e.getMessage(), e);
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
				throw new RuntimeException("Error occurred while retrieving Appointments: Empty Appointments list");
			}

			List<AppointmentInfoDTO> response = PatientAppointmentsMapper.INSTANCE
					.mapAppointmentsWithAdditionalFields(apiResponse2);

			response.forEach(appointment -> {
				String LocationId = appointment.getLocationId(); // get locationID from appointment
				LocationDTO location = new LocationDTO(); // Initialize location with all null values
				if (LocationId != null) {
					// Fetch the Location details
					SingleLocation200Response singleLocation = getSingleLocation(siteID, customerName, LocationId);
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
					SinglePractitioner200Response singlePractitioner = getSinglePractitioner(siteID, customerName,
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

//	public void setToken() {
//		ClientRegistration clientRegistration = getOauth().clientRegistration();
//		String token = ts.getToken(clientRegistration);
//		apiClient.setAccessToken(token);
//		System.out.println(">>>>>>>>>>>>" + token);
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

/*
 * { "appointmentId": 3265, "scheduledDate": null, "startDateTime":
 * "2022-04-25 10:15:00.000", "endDateTime": null, "doctorInfo": { "firstName":
 * "Thomas", "lastName": "Livingston", "middle":"", "abbreviation": "LIVINGST",
 * "degree": "NA", ------ "doctorId": 2, "careProviderId": 123, ------
 * "listName": "Livingston, Thomas", ------ "categoryId": null ------ },
 * "locationInfo": { "locationId": 1, "abbreviation": "NAS OFF",
 * "locationAbbreviation": "NAS OFF ", "listName": "Nashua Office", ------
 * "address1": "NA", "address2": "", "city": "NA", "state": "NA", "zip": "NA" },
 * "appointmentTypeId": 1, "appointmentName": "ACUTE", "comments":
 * "Sched by 69_0000_kumar_kiran_2022-04-23 /Acute Visit /",
 * "chiefComplaintText": null, ------ "chiefComplaintCommentsInfo": null, ------
 * "status": "S", "pastAppointment": true ------- }
 * 
 */