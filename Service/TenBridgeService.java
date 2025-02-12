package com.ps.tenbridge.datahub.controllerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.veradigm.ps.tenbridge.client.ApiClient;
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
				throw new RuntimeException("Error occurred while retrieving ethnicities: Empty Ethnicities list");
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

			Patients200Response apiResponse = searchPatient.patients(patientRequest);

			if (apiResponse == null || apiResponse.getPatients() == null) {
				logger.severe("Invalid data received: Patients list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getPatients().isEmpty()) {
				logger.severe("API returned empty list");
				return new ArrayList<PatientInfoDTO>();
			}
			List<ProviderDTO> allProviders = getProviders(siteID, customerName);
			List<ReferringProviderDTO> allReferringProviders = getReferringProviders(siteID, customerName);
			List<LocationDTO> allLocations = getLocations(siteID, customerName);

			PatientInfoDTO patientInfo = new PatientInfoDTO();
			patientInfo.setPrefDoc(allProviders.get(0));
			patientInfo.setPrefLoc(allLocations.get(0));
			patientInfo.setRefDoc(allReferringProviders.get(0));
			patientInfo.setPatientProfileId(patientProfileId);
			patientInfo.setPatientId(patientNumber);
			List<PatientInfoDTO> response = PatientSearchMapper.INSTANCE.mapPatientsWithAdditionalFields(apiResponse,
					allProviders, allLocations, allReferringProviders);
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

			Location location = new Location();
			Practitioner practitioner = new Practitioner();

			List<AppointmentInfoDTO> response = PatientAppointmentsMapper.INSTANCE
					.mapAppointmentsWithAdditionalFields(apiResponse, location, practitioner);

			response.forEach(appointment -> {
				String LocationId = appointment.getLocationId();
				String practitionerId = appointment.getPractitionerId();
				System.out.println("LLLLLL: " + LocationId);
				System.out.println("PPPPP: " + practitionerId);
				Object singleLocation = getSingleLocation(siteID, customerName, LocationId);
				appointment.setLocationInfo(singleLocation);
				Object singlePractitioner = getSinglePractitioner(siteID, customerName, practitionerId);
				appointment.setDoctorInfo(singlePractitioner);
			});

			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Error occurred while retrieving Appointments: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Appointments: " + e.getMessage(), e);
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

	public Object getSingleLocation(String siteID, String customerName, String locationId) {
		try {
			setToken();
			SingleLocationRequest singleLocationRequest = createSingleLocationRequest(siteID, customerName, locationId);
			SingleLocation200Response apiResponse = singleLocationApi.singleLocation(singleLocationRequest);
			if (apiResponse == null || apiResponse.getLocations().isEmpty()) {
				logger.severe("Invalid data received: location is null");
				return new Location();
//				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getLocations().isEmpty()) {
				logger.severe("API returned empty data");
				return new Location();
//				throw new RuntimeException("Error occurred while retrieving location: Empty location");
			}
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving location: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving location: " + e.getMessage(), e);
		}
	}

	public Object getSinglePractitioner(String siteID, String customerName, String practitionerId) {
		try {
			setToken();
			SinglePractitionerRequest singlePractitionerRequest = createSinglePractitionerRequest(siteID, customerName,
					practitionerId);
			SinglePractitioner200Response apiResponse = singlePractitionerApi
					.singlePractitioner(singlePractitionerRequest);
			if (apiResponse == null || apiResponse.getProviders().isEmpty()) {
				logger.severe("Invalid data received: Practitioner is null");
				return new Practitioner();
//				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getProviders().isEmpty()) {
				logger.severe("API returned empty data");
				return new Practitioner();
//				throw new RuntimeException("Error occurred while retrieving Practitioner: Empty Practitioner");
			}
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Practitioner: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Practitioner: " + e.getMessage(), e);
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