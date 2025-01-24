package com.ps.tenbridge.datahub.controllerImpl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;

import com.ps.tenbridge.datahub.config.OAuth2Config;
import com.ps.tenbridge.datahub.dto.CPTsDTO;
import com.ps.tenbridge.datahub.dto.CancelReasonsDTO;
import com.ps.tenbridge.datahub.dto.ChangeReasonsDTO;
import com.ps.tenbridge.datahub.dto.GendersDTO;
import com.ps.tenbridge.datahub.dto.ReferralSourcesDTO;
import com.ps.tenbridge.datahub.mapper.CancelReasonsMapper;
import com.ps.tenbridge.datahub.mapper.ChangeReasonsMapper;
import com.ps.tenbridge.datahub.mapper.CptMapper;
import com.ps.tenbridge.datahub.mapper.GenderMapper;
import com.ps.tenbridge.datahub.mapper.ReferralSourcesMapper;
import com.ps.tenbridge.datahub.services.authentication.TokenService;
import com.ps.tenbridge.datahub.utility.BaseService;
import com.veradigm.ps.tenbridge.client.ApiClient;
import com.veradigm.ps.tenbridge.client.api.CreatePatientApi;
import com.veradigm.ps.tenbridge.client.api.GetAppontmentsApi;
import com.veradigm.ps.tenbridge.client.api.GetAvailableCancelReasonsApi;
import com.veradigm.ps.tenbridge.client.api.GetAvailableChangeReasonsApi;
import com.veradigm.ps.tenbridge.client.api.GetCptValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetGenderValuesApi;
import com.veradigm.ps.tenbridge.client.api.GetPatientAlertsApi;
import com.veradigm.ps.tenbridge.client.api.GetProviderSlotsApi;
import com.veradigm.ps.tenbridge.client.api.GetReferralSourcesApi;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequest;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequestData;
import com.veradigm.ps.tenbridge.client.models.Appointments200Response;
import com.veradigm.ps.tenbridge.client.models.CPT200Response;
import com.veradigm.ps.tenbridge.client.models.CancellationReason200Response;
import com.veradigm.ps.tenbridge.client.models.ChangeReason200Response;
import com.veradigm.ps.tenbridge.client.models.Gender200Response;
import com.veradigm.ps.tenbridge.client.models.Patient;
import com.veradigm.ps.tenbridge.client.models.PatientAlert200Response;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequest;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequestBody;
import com.veradigm.ps.tenbridge.client.models.PatientCreateRequest;
import com.veradigm.ps.tenbridge.client.models.ProviderSlots200Response;
import com.veradigm.ps.tenbridge.client.models.ReferralSource200Response;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;
import com.veradigm.ps.tenbridge.client.models.Slot;
import com.veradigm.ps.tenbridge.client.models.SlotRequest;

@Service
public class TenBridgeService_Mod extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(TenBridgeService_Mod.class);
	TokenService ts;
	ApiClient apiClient;

	@Autowired
	private GetGenderValuesApi gendersApi;
	@Autowired
	private GetCptValuesApi cptsApi;
	@Autowired
	private GetReferralSourcesApi referralSourcesApi;
	@Autowired
	private GetPatientAlertsApi patientAlertsApi;
	@Autowired
	private GetAppontmentsApi appointmentsApi;
	@Autowired
	private GetProviderSlotsApi slotsApi;
	@Autowired
	private CreatePatientApi createPatientApi;
	@Autowired
	private GetAvailableCancelReasonsApi cancelReasonsApi;
	@Autowired
	private GetAvailableChangeReasonsApi changeReasonsApi;
	@Autowired
	private OAuth2Config oauth;

	public TenBridgeService_Mod(ApiClient apiClient, TokenService ts) {
		this.apiClient = apiClient;
		this.ts = ts;
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

	public List<GendersDTO> getGenders(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			Gender200Response apiResponse = gendersApi.gender(meta);
			if (apiResponse == null || apiResponse.getGenders() == null) {
				logger.severe("Invalid data received: Genders list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getGenders().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving genders: Empty gender list");
			}
			return buildResponse(apiResponse.getGenders(), GenderMapper.INSTANCE::GenderToGendersDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving genders: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving genders: " + e.getMessage(), e);
		}
	}

	public List<CPTsDTO> getCptCodes(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			CPT200Response apiResponse = cptsApi.cPT(meta);
			if (apiResponse == null || apiResponse.getCpts() == null) {
				logger.severe("Invalid data received: CPTs list is null");
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

	public List<ReferralSourcesDTO> getReferralSources(String siteID, String customerName) {
		try {
			RequestMetaData meta = createRequestMetaData(siteID, customerName);
			setToken();

			ReferralSource200Response apiResponse = referralSourcesApi.referralSource(meta);
			if (apiResponse == null || apiResponse.getReferralSources() == null) {
				logger.severe("Invalid data received: Referral sources list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getReferralSources().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException(
						"Error occurred while retrieving referral sources: Empty referral sources list");
			}
			return buildResponse(apiResponse.getReferralSources(),
					ReferralSourcesMapper.INSTANCE::ReferralSourcesToReferralSourcesDTO);
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving referral sources: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving referral sources: " + e.getMessage(), e);
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

	public Object getPatientAlerts(RequestMetaData meta, PatientAlertsRequestBody body) {
		try {
			PatientAlertsRequest patientAlertRequest = new PatientAlertsRequest();
			patientAlertRequest.setMeta(meta);
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
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Patient Alerts: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Patient Alerts: " + e.getMessage(), e);
		}
	}

	public Object getAppointment(RequestMetaData meta, AppointmentSearchRequestData data) {
		try {
			AppointmentSearchRequest appointmentSearchRequest = new AppointmentSearchRequest();
			appointmentSearchRequest.setMeta(meta);
			appointmentSearchRequest.setData(data);
			setToken();
			Appointments200Response apiResponse = appointmentsApi.appointments(appointmentSearchRequest);
			if (apiResponse == null || apiResponse.getAppointment() == null) {
				logger.severe("Invalid data received: Appointments list is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.getAppointment().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while retrieving Appointments: Empty Appointments list");
			}
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while retrieving Appointments: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Appointments: " + e.getMessage(), e);
		}
	}

	public Object getSlots(SlotRequest slotRequest) {
		try {
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
			logger.severe("Error occurred while retrieving Slots: " + e.getMessage());
			throw new RuntimeException("Error occurred while retrieving Slots: " + e.getMessage(), e);
		}
	}

	public Object createPatient(PatientCreateRequest patientCreateRequest) {
		try {
			setToken();
			Patient apiResponse = createPatientApi.patient(patientCreateRequest);
			if (apiResponse == null || apiResponse.patient() == null) {
				logger.severe("Invalid data received: Patient is null");
				throw new RuntimeException("Error occurred while building response: Invalid data received");
			}
			if (apiResponse.patient().isEmpty()) {
				logger.severe("API returned empty list");
				throw new RuntimeException("Error occurred while creating Patient: Empty Patient data");
			}
			return apiResponse;
		} catch (Exception e) {
			logger.severe("Error occurred while creating Patient: " + e.getMessage());
			throw new RuntimeException("Error occurred while creating Patient: " + e.getMessage(), e);
		}
	}

	public void setToken() {
		ClientRegistration clientRegistration = oauth.clientRegistration();
		String token = ts.getToken(clientRegistration);
		apiClient.setAccessToken(token);
		System.out.println(">>>>>>>>>>>>" + token);
	}

}
