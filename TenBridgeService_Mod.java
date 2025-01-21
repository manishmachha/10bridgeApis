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

	public List<GendersDTO> getGenders(String siteID, String customerName) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		setToken();

		Gender200Response apiResponse = gendersApi.gender(meta);

		return buildResponse(apiResponse.getGenders(), GenderMapper.INSTANCE::GenderToGendersDTO);
	}

	public List<CPTsDTO> getCptCodes(String siteID, String customerName) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		setToken();

		CPT200Response apiResponse = cptsApi.cPT(meta);

		return buildResponse(apiResponse.getCpts(), CptMapper.INSTANCE::CptToCptsDTO);
	}

	public List<ReferralSourcesDTO> getReferralSources(String siteID, String customerName) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		setToken();

		ReferralSource200Response apiResponse = referralSourcesApi.referralSource(meta);

		return buildResponse(apiResponse.getReferralSources(),
				ReferralSourcesMapper.INSTANCE::ReferralSourcesToReferralSourcesDTO);
	}

	public List<CancelReasonsDTO> getCancelReasons(String siteID, String customerName) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		setToken();

		CancellationReason200Response apiResponse = cancelReasonsApi.cancellationReason(meta);

		return buildResponse(apiResponse.getCancelReasons(),
				CancelReasonsMapper.INSTANCE::CancelReasonsToCancelReasonsDTO);
	}

	public List<ChangeReasonsDTO> getChangeReasons(String siteID, String customerName) {
		RequestMetaData meta = createRequestMetaData(siteID, customerName);
		setToken();

		ChangeReason200Response apiResponse = changeReasonsApi.changeReason(meta);

		return buildResponse(apiResponse.getChangeReasons(),
				ChangeReasonsMapper.INSTANCE::ChangeReasonsToChangeReasonsDTO);
	}

	public Object getPatientAlerts(RequestMetaData meta, PatientAlertsRequestBody body) {
		PatientAlertsRequest patientAlertRequest = new PatientAlertsRequest();
		patientAlertRequest.setMeta(meta);
		patientAlertRequest.setBody(body);
		setToken();
		PatientAlert200Response apiResponse = patientAlertsApi.patientAlert(patientAlertRequest);
		return apiResponse;
	}

	public Object getAppointment(RequestMetaData meta, AppointmentSearchRequestData data) {
		AppointmentSearchRequest appointmentSearchRequest = new AppointmentSearchRequest();
		appointmentSearchRequest.setMeta(meta);
		appointmentSearchRequest.setData(data);
		setToken();
		Appointments200Response apiResponse = appointmentsApi.appointments(appointmentSearchRequest);
		return apiResponse;
	}

	public Object getSlots(SlotRequest slotRequest) {
		setToken();
		ProviderSlots200Response apiResponse = slotsApi.providerSlots(slotRequest);
		return apiResponse;
	}

	public Object createPatient(PatientCreateRequest patientCreateRequest) {
		setToken();
		Patient apiResponse = createPatientApi.patient(patientCreateRequest);
		return apiResponse;
	}

	public void setToken() {
		ClientRegistration clientRegistration = oauth.clientRegistration();
		String token = ts.getToken(clientRegistration);
		apiClient.setAccessToken(token);
		System.out.println(">>>>>>>>>>>>" + token);
	}

}
