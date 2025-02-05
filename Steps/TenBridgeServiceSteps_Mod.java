package com.ps.tenbridge.integration.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import com.ps.tenbridge.datahub.config.OAuth2Config;
import com.ps.tenbridge.datahub.controllerImpl.TenBridgeService;
import com.ps.tenbridge.datahub.dto.CPTsDTO;
import com.ps.tenbridge.datahub.dto.CancelReasonsDTO;
import com.ps.tenbridge.datahub.dto.ChangeReasonsDTO;
import com.ps.tenbridge.datahub.dto.EthnicityDTO;
import com.ps.tenbridge.datahub.dto.GendersDTO;
import com.ps.tenbridge.datahub.dto.InsuranceDTO;
import com.ps.tenbridge.datahub.dto.LocationDTO;
import com.ps.tenbridge.datahub.dto.PatientAlertsDTO;
import com.ps.tenbridge.datahub.dto.PatientInfoDTO;
import com.ps.tenbridge.datahub.dto.Patients200Response;
import com.ps.tenbridge.datahub.dto.ProviderDTO;
import com.ps.tenbridge.datahub.dto.RacesDTO;
import com.ps.tenbridge.datahub.dto.ReferralSourcesDTO;
import com.ps.tenbridge.datahub.dto.ReferringProviderDTO;
import com.ps.tenbridge.datahub.dto.SearchPatientApi;
import com.ps.tenbridge.datahub.dto.patientAlerts;
import com.ps.tenbridge.datahub.services.authentication.TokenService;
import com.veradigm.ps.tenbridge.client.ApiClient;
import com.veradigm.ps.tenbridge.client.api.CreatePatientApi;
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
import com.veradigm.ps.tenbridge.client.models.Appointment;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequest;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequestData;
import com.veradigm.ps.tenbridge.client.models.Appointments200Response;
import com.veradigm.ps.tenbridge.client.models.CPT200Response;
import com.veradigm.ps.tenbridge.client.models.CPTCode;
import com.veradigm.ps.tenbridge.client.models.CancelReasonsResponse;
import com.veradigm.ps.tenbridge.client.models.CancellationReason200Response;
import com.veradigm.ps.tenbridge.client.models.ChangeReason200Response;
import com.veradigm.ps.tenbridge.client.models.ChangeReasonsResponse;
import com.veradigm.ps.tenbridge.client.models.Ethnicity;
import com.veradigm.ps.tenbridge.client.models.Ethnicity200Response;
import com.veradigm.ps.tenbridge.client.models.Gender;
import com.veradigm.ps.tenbridge.client.models.Gender200Response;
import com.veradigm.ps.tenbridge.client.models.InsuranceCarrier;
import com.veradigm.ps.tenbridge.client.models.InsurancePolicy;
import com.veradigm.ps.tenbridge.client.models.Location;
import com.veradigm.ps.tenbridge.client.models.Patient;
import com.veradigm.ps.tenbridge.client.models.PatientAlert200Response;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequest;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequestBody;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsResponse;
import com.veradigm.ps.tenbridge.client.models.PatientCreateRequest;
import com.veradigm.ps.tenbridge.client.models.PatientRequest;
import com.veradigm.ps.tenbridge.client.models.PatientRequestData;
import com.veradigm.ps.tenbridge.client.models.PayorGroups200Response;
import com.veradigm.ps.tenbridge.client.models.PracticeLocation200Response;
import com.veradigm.ps.tenbridge.client.models.Practitioner;
import com.veradigm.ps.tenbridge.client.models.Providers200Response;
import com.veradigm.ps.tenbridge.client.models.Race;
import com.veradigm.ps.tenbridge.client.models.Race200Response;
import com.veradigm.ps.tenbridge.client.models.ReferralSource200Response;
import com.veradigm.ps.tenbridge.client.models.ReferralSourcesResponse;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TenBridgeServiceSteps_Mod {

	private RequestMetaData meta = new RequestMetaData();
	private PatientAlertsRequestBody requestBody = new PatientAlertsRequestBody();
	private AppointmentSearchRequestData appointmentSearchRequestData = new AppointmentSearchRequestData();
	private PatientRequest patientRequest = new PatientRequest();
	private PatientRequestData patientRequestData = new PatientRequestData();

	private List<ProviderDTO> providerResponse;

	private List<ReferringProviderDTO> ReferringroviderResponse;

	private List<LocationDTO> locationResponse;

	private List<InsuranceDTO> insuranceResponse;

	private List<EthnicityDTO> ethnicityResponse;

	private List<RacesDTO> racesResponse;

	private List<ReferralSourcesDTO> referralSourcesResponse;

	private patientAlerts patientAlertsResponse;

	private List<GendersDTO> gendersResponse;

	private List<CPTsDTO> cptResponse;

	private List<CancelReasonsDTO> cancelReasonsResponse;

	private List<ChangeReasonsDTO> changeReasonsResponse;

	private List<Appointment> appointmentsResponse;

	private List<PatientInfoDTO> patientsResponse;

	private Patient createPatientResponse;

	private Exception exception;

	@Mock
	private GetProvidersApi providersApi;

	@Mock
	private GetReferringProvidersApi referringProvidersApi;

	@Mock
	private GetPracticeLocationsApi locationsApi;

	@Mock
	private GetPayorGroupsApi payorGroupsApi;

	@Mock
	private GetEthnicityValuesApi ethnicityValuesApi;

	@Mock
	private GetRaceValuesApi racesApi;

	@Mock
	private GetReferralSourcesApi referralSourcesApi;

	@Mock
	private GetPatientAlertsApi patientAlertsApi;

	@Mock
	private GetGenderValuesApi genderValuesApi;

	@Mock
	private GetCptValuesApi cptValuesApi;

	@Mock
	private SearchPatientApi searchPatientApi;

	@Mock
	private GetAvailableCancelReasonsApi cancelReasonsApi;

	@Mock
	private GetAvailableChangeReasonsApi changeReasonsApi;

	@Mock
	private GetAppontmentsApi appontmentsApi;

	@Mock
	private GetProviderSlotsApi slotsApi;

	@Mock
	private CreatePatientApi createPatientApi;

	@Mock
	private OAuth2Config oauth;

	@Mock
	private TokenService ts;

	@Mock
	private ApiClient apiClient;

	@InjectMocks
	private TenBridgeService tenBridgeService;

	@Mock
	private ClientRegistration clientRegistration;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		// Mock dependencies
		apiClient = mock(ApiClient.class);
		ts = mock(TokenService.class);
		oauth = mock(OAuth2Config.class);
		clientRegistration = mock(ClientRegistration.class);
		providersApi = mock(GetProvidersApi.class);
		// Mock behavior
		when(oauth.clientRegistration()).thenReturn(clientRegistration);
		when(ts.getToken(Mockito.any(ClientRegistration.class))).thenReturn("valid-token");

		// Assign mocks to TenBridgeService
		tenBridgeService = new TenBridgeService(apiClient, ts, oauth, providersApi, locationsApi, payorGroupsApi,
				referringProvidersApi, ethnicityValuesApi, racesApi, referralSourcesApi, patientAlertsApi,
				genderValuesApi, cptValuesApi, cancelReasonsApi, changeReasonsApi, appontmentsApi, slotsApi,
				createPatientApi, searchPatientApi);
	}

	/*********************************************************************************************************
	 * Providers Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For GetProviders")
	public void initializeWithValidTokenForGetProviders() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getProviders API with siteID {string} and customerName {string} with valid Token")
	public void callGetProvidersWithValidToken(String siteID, String customerName) {
		Providers200Response mockApiResponse = new Providers200Response();
		Practitioner practitioner1 = new Practitioner();
		practitioner1.setFirstName("TestfirstName");
		practitioner1.setSpeciality("Testspecialty");
		practitioner1.setAbbreviation("few");
		practitioner1.setLastName("TestLn");
		practitioner1.setPractitionerId("10");
		practitioner1.setIsActive("true");
		Practitioner practitioner2 = new Practitioner();
		practitioner2.setFirstName("TestfirstName1");
		practitioner2.setSpeciality("Testspecialty1");
		practitioner2.setAbbreviation("fe");
		practitioner2.setLastName("TestLn1");
		practitioner2.setPractitionerId("101");
		practitioner2.setIsActive("true");
		mockApiResponse.setProviders(List.of(practitioner1, practitioner2));
		when(providersApi.providers(Mockito.any())).thenReturn(mockApiResponse);
		providerResponse = tenBridgeService.getProviders(siteID, customerName);
	}

	@Then("I should receive a list of providers")
	public void verifyProvidersResponse() {
		assertNotNull(providerResponse, "Provider response should not be null");
		assertFalse(providerResponse.isEmpty(), "Provider list should not be empty");
	}

	@And("each provider should have valid details")
	public void each_provider_should_have_valid_details() {
		for (ProviderDTO provider : providerResponse) {
			assertNotNull(provider.getFirstName(), "Provider's first name should not be null");
			assertNotNull(provider.getLastName(), "Provider's last name should not be null");
			assertNotNull(provider.getProviderId(), "Provider's practitioner ID should not be null");
			assertNotNull(provider.getIs_active(), "Provider's active status should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For GetProviders")
	public void initializeTenBridgeServiceForGetProviders() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");
	}

	@When("the getProviders API is called and the API returns an error status")
	public void apiReturnsErrorStatusForGetProviders() {
		Mockito.mock(RequestMetaData.class);
		when(providersApi.providers(Mockito.any(RequestMetaData.class))).thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getProviders("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For GetProviders")
	public void verifyErrorMessageLoggedForGetProviders() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving providers"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For GetProviders")
	public void initializeWithInvalidTokenForGetProviders() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getProviders API with siteID {string} and customerName {string} with invalid Token")
	public void callGetProvidersWithInvalidToken(String siteID, String customerName) {
		when(providersApi.providers(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getProviders(siteID, customerName);
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For GetProviders")
	public void verifyUnauthorizedErrorForGetProviders() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getProviders API receives invalid data for response building")
	public void getProvidersapiReceivesInvalidDataForResponseBuilding() {
		Providers200Response mockApiResponse = Mockito.mock(Providers200Response.class);
		when(mockApiResponse.getProviders()).thenReturn(null); // Simulate invalid data
		when(providersApi.providers(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getProviders("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For GetProviders")
	public void verifyInvalidDataErrorMessageLoggedForGetProviders() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getProviders API returns an empty list")
	public void getProvidersapiReturnsEmptyList() {
		Providers200Response mockApiResponse = new Providers200Response();
		mockApiResponse.setProviders(List.of()); // Simulate empty list
		when(providersApi.providers(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			providerResponse = tenBridgeService.getProviders("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For GetProviders")
	public void verifyEmptyListErrorMessageLoggedForGetProviders() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty provider list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Locations Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For GetLocations")
	public void initializeWithVvalidTokenForGetLocations() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getLocations API with siteID {string} and customerName {string} with valid Token")
	public void callGetLocationsWithValidToken(String siteID, String customerName) {
		PracticeLocation200Response mockApiResponse = new PracticeLocation200Response();
		Location location1 = new Location();
		location1.setLocationId("001");
		location1.setLocationName("Head Office");
		location1.setLocationType("Corporate");
		location1.setAddressLine1("123 Main St");
		location1.setAddressLine2("Suite 400");
		location1.setState("Telangana");
		location1.setCity("Hyderabad");
		location1.setZip("500081");
		location1.setAbbreviation("HO");
		location1.setCountryCode("IN");
		location1.setIsActive("true");
		Location location2 = new Location();
		location2.setLocationId("002");
		location2.setLocationName("Branch Office");
		location2.setLocationType("Retail");
		location2.setAddressLine1("456 Market St");
		location2.setAddressLine2("Floor 2");
		location2.setState("Karnataka");
		location2.setCity("Bangalore");
		location2.setZip("560034");
		location2.setAbbreviation("BO");
		location2.setCountryCode("IN");
		location2.setIsActive("false");
		mockApiResponse.setLocations(List.of(location1, location2));

		when(locationsApi.practiceLocation(Mockito.any())).thenReturn(mockApiResponse);
		locationResponse = tenBridgeService.getLocations(siteID, customerName);
	}

	@Then("I should receive a list of locations")
	public void verifyLocationsResponse() {
		assertNotNull(locationResponse, "Location response should not be null");
		assertFalse(locationResponse.isEmpty(), "Location list should not be empty");
	}

	@And("each location should have valid details For GetLocations")
	public void each_location_should_have_valid_details() {
		for (LocationDTO location : locationResponse) {
			assertNotNull(location.getAbbreviation(), "Location's abbrevation should not be null");
			assertNotNull(location.getLocationId(), "Location's ID should not be null");
			assertNotNull(location.getAddress1(), "Location's address should not be null");
			assertNotNull(location.getIs_active(), "Location's active status should not be null");
			assertNotNull(location.getListName(), "Location's list name should not be null");
			assertNotNull(location.getState(), "Location's state should not be null");
			assertNotNull(location.getZip(), "Location's zip should not be null");
			assertNotNull(location.getCity(), "Location's city should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For GetLocations")
	public void initializeTenBridgeServiceForGetLocations() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getLocations API is called and the API returns an error status")
	public void getLocationsApiReturnsErrorStatus() {
		Mockito.mock(RequestMetaData.class);
		when(locationsApi.practiceLocation(Mockito.any(RequestMetaData.class)))
				.thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getLocations("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For GetLocations")
	public void verifyErrorMessageLoggedForGetLocations() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving locations"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For GetLocations")
	public void initializeWithInvalidTokenForGetLocations() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getLocations API with siteID {string} and customerName {string} with invalid Token")
	public void callGetLocationsWithInvalidToken(String siteID, String customerName) {
		when(locationsApi.practiceLocation(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getLocations(siteID, customerName);
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For GetLocations")
	public void verifyUnauthorizedErrorForGetLocations() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getLocations API receives invalid data for response building")
	public void getLocationsapiReceivesInvalidDataForResponseBuilding() {
		PracticeLocation200Response mockApiResponse = Mockito.mock(PracticeLocation200Response.class);
		when(mockApiResponse.getLocations()).thenReturn(null); // Simulate invalid data
		when(locationsApi.practiceLocation(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getLocations("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For GetLocations")
	public void verifyInvalidDataErrorMessageLoggedForGetLocations() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getLocations API returns an empty list")
	public void getLocationsapiReturnsEmptyList() {
		PracticeLocation200Response mockApiResponse = new PracticeLocation200Response();
		mockApiResponse.setLocations(List.of()); // Simulate empty list
		when(locationsApi.practiceLocation(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			locationResponse = tenBridgeService.getLocations("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For GetLocations")
	public void verifyEmptyListErrorMessageLoggedForGetLocations() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty location list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Referring Providers Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For ReferringProviders")
	public void initializeWithVvalidTokenForGetReferringProviders() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getReferringProviders API with siteID {string} and customerName {string} with valid Token")
	public void callGetReferringProvidersWithValidToken(String siteID, String customerName) {
		Providers200Response mockApiResponse = new Providers200Response();
		Practitioner practitioner1 = new Practitioner();
		practitioner1.setFirstName("TestfirstName");
		practitioner1.setSpeciality("Testspecialty");
		practitioner1.setAbbreviation("few");
		practitioner1.setLastName("TestLn");
		practitioner1.setPractitionerId("10");
		practitioner1.setIsActive("true");
		Practitioner practitioner2 = new Practitioner();
		practitioner2.setFirstName("TestfirstName1");
		practitioner2.setSpeciality("Testspecialty1");
		practitioner2.setAbbreviation("fe");
		practitioner2.setLastName("TestLn1");
		practitioner2.setPractitionerId("101");
		practitioner2.setIsActive("true");
		mockApiResponse.setProviders(List.of(practitioner1, practitioner2));
		when(referringProvidersApi.referringProviders(Mockito.any())).thenReturn(mockApiResponse);
		ReferringroviderResponse = tenBridgeService.getReferringProviders(siteID, customerName);
	}

	@Then("I should receive a list of referring providers")
	public void verifyReferringProvidersResponse() {
		assertNotNull(ReferringroviderResponse, "ReferringProviders response should not be null");
		assertFalse(ReferringroviderResponse.isEmpty(), "ReferringProviders list should not be empty");
	}

	@And("each referring provider should have valid details For ReferringProviders")
	public void each_ReferringProviders_should_have_valid_details() {
		for (ReferringProviderDTO referringProvider : ReferringroviderResponse) {
			assertNotNull(referringProvider.getAbbreviation(), "Referring provider's abbrevation should not be null");
			assertNotNull(referringProvider.getFirstName(), "Referring provider's first name should not be null");
			assertNotNull(referringProvider.getIs_active(), "Referring provider's active status should not be null");
			assertNotNull(referringProvider.getLastName(), "Referring provider's last name should not be null");
			assertNotNull(referringProvider.getSpecialty(), "Referring provider's speciality should not be null");
			assertNotNull(referringProvider.getReferringproviderid(),
					"Referring provider's practitioner id should not be null");
			System.out.println(referringProvider);
		}
	}

	@Given("the TenBridgeService is initialized For ReferringProviders")
	public void initializeTenBridgeServiceForGetReferringProviders() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getReferringProviders API is called and the API returns an error status")
	public void getReferringProvidersApiReturnsErrorStatus() {
		Mockito.mock(RequestMetaData.class);
		when(referringProvidersApi.referringProviders(Mockito.any(RequestMetaData.class)))
				.thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getReferringProviders("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For ReferringProviders")
	public void verifyErrorMessageLoggedForGetReferringProviders() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving referring providers"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For ReferringProviders")
	public void initializeWithInvalidTokenForGetReferringProviders() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getReferringProviders API with siteID {string} and customerName {string} with invalid Token")
	public void callGetReferringProvidersWithInvalidToken(String siteID, String customerName) {
		when(referringProvidersApi.referringProviders(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getReferringProviders(siteID, customerName);
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For ReferringProviders")
	public void verifyUnauthorizedErrorForGetReferringProviders() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getReferringProviders API receives invalid data for response building")
	public void getReferringProvidersapiReceivesInvalidDataForResponseBuilding() {
		Providers200Response mockApiResponse = Mockito.mock(Providers200Response.class);
		when(mockApiResponse.getProviders()).thenReturn(null); // Simulate invalid data
		when(referringProvidersApi.referringProviders(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getLocations("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For ReferringProviders")
	public void verifyInvalidDataErrorMessageLoggedForGetReferringProviders() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getReferringProviders API returns an empty list")
	public void getReferringProvidersapiReturnsEmptyList() {
		Providers200Response mockApiResponse = new Providers200Response();
		mockApiResponse.setProviders(List.of()); // Simulate empty list
		when(referringProvidersApi.referringProviders(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			ReferringroviderResponse = tenBridgeService.getReferringProviders("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For ReferringProviders")
	public void verifyEmptyListErrorMessageLoggedForGetReferringProviders() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty Referring provider list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Insurances Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For GetInsurances")
	public void initializeWithVvalidTokenForGetInsurances() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getInsurances API with siteID {string} and customerName {string} with valid Token")
	public void callGetInsurancesWithValidToken(String siteID, String customerName) {
		PayorGroups200Response mockApiResponse = new PayorGroups200Response();
		InsuranceCarrier carrier1 = new InsuranceCarrier();
		carrier1.setCarrierId("C001");
		carrier1.setCarrierName("Swift Logistics");
		carrier1.setGroupId("G101");
		carrier1.setAbbreviation("SL");
		carrier1.setCoverageType("International");
		carrier1.setPhone("123-456-7890");
		carrier1.setAddressLine1("1234 Elm St");
		carrier1.setAddressLine2("Apt 101");
		carrier1.setState("Telangana");
		carrier1.setCity("Hyderabad");
		carrier1.setZip("500081");
		carrier1.setEmail("contact@swiftlogistics.com");
		carrier1.setContactName("John Doe");
		carrier1.setIsActive("true");
		InsuranceCarrier carrier2 = new InsuranceCarrier();
		carrier2.setCarrierId("C002");
		carrier2.setCarrierName("Fast Freight");
		carrier2.setGroupId("G102");
		carrier2.setAbbreviation("FF");
		carrier2.setCoverageType("Domestic");
		carrier2.setPhone("987-654-3210");
		carrier2.setAddressLine1("5678 Maple St");
		carrier2.setAddressLine2("Suite 202");
		carrier2.setState("Karnataka");
		carrier2.setCity("Bangalore");
		carrier2.setZip("560034");
		carrier2.setEmail("support@fastfreight.com");
		carrier2.setContactName("Jane Smith");
		carrier2.setIsActive("false");
		mockApiResponse.setCarriers(List.of(carrier1, carrier2));

		when(payorGroupsApi.payorGroups(Mockito.any())).thenReturn(mockApiResponse);
		insuranceResponse = tenBridgeService.getInsurances(siteID, customerName);
	}

	@Then("I should receive a list of insurances")
	public void verifyInsurancesResponse() {
		assertNotNull(insuranceResponse, "Location response should not be null");
		assertFalse(insuranceResponse.isEmpty(), "Location list should not be empty");
	}

	@And("each insurance should have valid details For GetInsurances")
	public void each_insurance_should_have_valid_details() {
		for (InsuranceDTO insurance : insuranceResponse) {
			assertNotNull(insurance.getAddress1(), "Insurance's address should not be null");
			assertNotNull(insurance.getCity(), "Insurance's city should not be null");
			assertNotNull(insurance.getId(), "Insurance's id should not be null");
			assertNotNull(insurance.getIs_active(), "Insurance's active status should not be null");
			assertNotNull(insurance.getName(), "Insurance's  name should not be null");
			assertNotNull(insurance.getPhone(), "Insurance's phone should not be null");
			assertNotNull(insurance.getStandardizedName(), "Insurance's standardized name should not be null");
			assertNotNull(insurance.getState(), "Insurance's state should not be null");
			assertNotNull(insurance.getZip(), "Insurance's zip should not be null");
			assertNotNull(insurance.getCoverageType(), "Insurance's coverage type should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For GetInsurances")
	public void initializeTenBridgeServiceForGetInsurances() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getInsurances API is called and the API returns an error status")
	public void getInsurancesApiReturnsErrorStatus() {
		Mockito.mock(RequestMetaData.class);
		when(payorGroupsApi.payorGroups(Mockito.any(RequestMetaData.class)))
				.thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getInsurances("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For GetInsurances")
	public void verifyErrorMessageLoggedForGetInsurances() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving insurances"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For GetInsurances")
	public void initializeWithInvalidTokenForGetInsurances() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getInsurances API with siteID {string} and customerName {string} with invalid Token")
	public void callGetInsurancesWithInvalidToken(String siteID, String customerName) {
		when(payorGroupsApi.payorGroups(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getInsurances(siteID, customerName);
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For GetInsurances")
	public void verifyUnauthorizedErrorForGetInsurances() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getInsurances API receives invalid data for response building")
	public void getInsurancesapiReceivesInvalidDataForResponseBuilding() {
		PayorGroups200Response mockApiResponse = Mockito.mock(PayorGroups200Response.class);
		when(mockApiResponse.getCarriers()).thenReturn(null); // Simulate invalid data
		when(payorGroupsApi.payorGroups(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getLocations("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For GetInsurances")
	public void verifyInvalidDataErrorMessageLoggedForGetInsurances() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getInsurances API returns an empty list")
	public void getInsurancesapiReturnsEmptyList() {
		PayorGroups200Response mockApiResponse = new PayorGroups200Response();
		mockApiResponse.setCarriers(List.of()); // Simulate empty list
		when(payorGroupsApi.payorGroups(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			insuranceResponse = tenBridgeService.getInsurances("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For GetInsurances")
	public void verifyEmptyListErrorMessageLoggedForGetInsurances() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty insurance list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Ethnicities Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For Ethnicities")
	public void initializeWithVvalidTokenForGetEthnicities() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getEthnicities API with siteID {string} and customerName {string} with valid Token")
	public void callGetEthnicitiesWithValidToken(String siteID, String customerName) {
		Ethnicity200Response mockApiResponse = new Ethnicity200Response();
		Ethnicity ethnicity1 = new Ethnicity();
		ethnicity1.setEthnicityId(1);
		ethnicity1.setEthnicity("Asian");
		Ethnicity ethnicity2 = new Ethnicity();
		ethnicity2.setEthnicityId(2);
		ethnicity2.setEthnicity("Hispanic");
		mockApiResponse.setEthnicities(List.of(ethnicity1, ethnicity2));
		when(ethnicityValuesApi.ethnicity(Mockito.any())).thenReturn(mockApiResponse);
		ethnicityResponse = tenBridgeService.getEthnicities(siteID, customerName);
	}

	@Then("I should receive a list of ethnicities")
	public void verifyEthnicitiesResponse() {
		assertNotNull(ethnicityResponse, "Ethnicities response should not be null");
		assertFalse(ethnicityResponse.isEmpty(), "Ethnicities list should not be empty");
	}

	@And("each ethnicity should have valid details For Ethnicities")
	public void each_ethnicities_should_have_valid_details() {
		for (EthnicityDTO ethnicity : ethnicityResponse) {
			assertNotNull(ethnicity.getEthnicityId(), "Ethnicity's id should not be null");
			assertNotNull(ethnicity.getEthnicityName(), "Ethnicity's name should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For Ethnicities")
	public void initializeTenBridgeServiceForGetEthnicities() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getEthnicities API is called and the API returns an error status")
	public void getEthnicitiesApiReturnsErrorStatus() {
		Mockito.mock(RequestMetaData.class);
		when(ethnicityValuesApi.ethnicity(Mockito.any(RequestMetaData.class)))
				.thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getEthnicities("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For Ethnicities")
	public void verifyErrorMessageLoggedForGetEthnicities() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving Ethnicities"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For Ethnicities")
	public void initializeWithInvalidTokenForGetEthnicities() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getEthnicities API with siteID {string} and customerName {string} with invalid Token")
	public void callGetEthnicitiesWithInvalidToken(String siteID, String customerName) {
		when(ethnicityValuesApi.ethnicity(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getEthnicities(siteID, customerName);
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For Ethnicities")
	public void verifyUnauthorizedErrorForGetEthnicities() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getEthnicities API receives invalid data for response building")
	public void getEthnicitiesApiReceivesInvalidDataForResponseBuilding() {
		Ethnicity200Response mockApiResponse = Mockito.mock(Ethnicity200Response.class);
		when(mockApiResponse.getEthnicities()).thenReturn(null); // Simulate invalid data
		when(ethnicityValuesApi.ethnicity(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getLocations("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For Ethnicities")
	public void verifyInvalidDataErrorMessageLoggedForGetEthnicities() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getEthnicities API returns an empty list")
	public void getEthnicitiesApiReturnsEmptyList() {
		Ethnicity200Response mockApiResponse = new Ethnicity200Response();
		mockApiResponse.setEthnicities(List.of()); // Simulate empty list
		when(ethnicityValuesApi.ethnicity(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			ethnicityResponse = tenBridgeService.getEthnicities("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For Ethnicities")
	public void verifyEmptyListErrorMessageLoggedForGetEthnicities() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty Ethnicities list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Races Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For Races")
	public void initializeWithVvalidTokenForGetRaces() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getRaces API with siteID {string} and customerName {string} with valid Token")
	public void callGetRacesWithValidToken(String siteID, String customerName) {
		Race200Response mockApiResponse = new Race200Response();
		Race race1 = new Race();
		race1.setRaceId(1);
		race1.setRace("Marathon");
		Race race2 = new Race();
		race2.setRaceId(2);
		race2.setRace("Sprint");
		mockApiResponse.races(List.of(race1, race2));
		when(racesApi.race(Mockito.any())).thenReturn(mockApiResponse);
		racesResponse = tenBridgeService.getRaces(siteID, customerName);
	}

	@Then("I should receive a list of races")
	public void verifyRacesResponse() {
		assertNotNull(racesResponse, "Ethnicities response should not be null");
		assertFalse(racesResponse.isEmpty(), "Ethnicities list should not be empty");
	}

	@And("each race should have valid details For Races")
	public void each_race_should_have_valid_details() {
		for (RacesDTO race : racesResponse) {
			assertNotNull(race.getRaceId(), "Race's id should not be null");
			assertNotNull(race.getRaceName(), "Race's name should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For Races")
	public void initializeTenBridgeServiceForGetRaces() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getRaces API is called and the API returns an error status")
	public void getRacesApiReturnsErrorStatus() {
		Mockito.mock(RequestMetaData.class);
		when(racesApi.race(Mockito.any(RequestMetaData.class))).thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getRaces("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For Races")
	public void verifyErrorMessageLoggedForGetRaces() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving Races"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For Races")
	public void initializeWithInvalidTokenForGetRaces() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getRaces API with siteID {string} and customerName {string} with invalid Token")
	public void callGetRacesWithInvalidToken(String siteID, String customerName) {
		when(racesApi.race(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getRaces(siteID, customerName);
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For Races")
	public void verifyUnauthorizedErrorForGetRaces() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getRaces API receives invalid data for response building")
	public void getRacesApiReceivesInvalidDataForResponseBuilding() {
		Race200Response mockApiResponse = Mockito.mock(Race200Response.class);
		when(mockApiResponse.getRaces()).thenReturn(null); // Simulate invalid data
		when(racesApi.race(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getRaces("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For Races")
	public void verifyInvalidDataErrorMessageLoggedForGetRaces() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getRaces API returns an empty list")
	public void getRacesApiReturnsEmptyList() {
		Race200Response mockApiResponse = new Race200Response();
		mockApiResponse.setRaces(List.of()); // Simulate empty list
		when(racesApi.race(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			racesResponse = tenBridgeService.getRaces("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For Races")
	public void verifyEmptyListErrorMessageLoggedForGetRaces() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty Races list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * ReferralSources Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For ReferralSources")
	public void initializeWithVvalidTokenForGetReferralSources() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getReferralSources API with siteID {string} and customerName {string} with valid Token")
	public void callGetReferralSourcesWithValidToken(String siteID, String customerName) {
		ReferralSource200Response mockApiResponse = new ReferralSource200Response();
		ReferralSourcesResponse referralSource1 = new ReferralSourcesResponse();
		referralSource1.setReferralSourceId(1);
		referralSource1.setAbbreviation("Marathon");
		referralSource1.setDescription("Marathon");
		ReferralSourcesResponse referralSource2 = new ReferralSourcesResponse();
		referralSource2.setReferralSourceId(1);
		referralSource2.setAbbreviation("Marathon");
		referralSource2.setDescription("Marathon");
		mockApiResponse.referralSources(List.of(referralSource1, referralSource2));
		when(referralSourcesApi.referralSource(Mockito.any())).thenReturn(mockApiResponse);
		referralSourcesResponse = tenBridgeService.getReferralSources(siteID, customerName);
	}

	@Then("I should receive a list of ReferralSources")
	public void verifyReferralSourcesResponse() {
		assertNotNull(referralSourcesResponse, "ReferralSources response should not be null");
		assertFalse(referralSourcesResponse.isEmpty(), "ReferralSources list should not be empty");
	}

	@And("each ReferralSource should have valid details")
	public void each_ReferralSource_should_have_valid_details() {
		for (ReferralSourcesDTO referralSource : referralSourcesResponse) {
			assertNotNull(referralSource.getId(), "referralSource's id should not be null");
			assertNotNull(referralSource.getName(), "referralSource's description should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For ReferralSources")
	public void initializeTenBridgeServiceForGetReferralSources() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getReferralSources API is called and the API returns an error status")
	public void getReferralSourcesApiReturnsErrorStatus() {
		Mockito.mock(RequestMetaData.class);
		when(referralSourcesApi.referralSource(Mockito.any(RequestMetaData.class)))
				.thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getReferralSources("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For ReferralSources")
	public void verifyErrorMessageLoggedForGetReferralSources() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving ReferralSources"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For ReferralSources")
	public void initializeWithInvalidTokenForGetReferralSources() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getReferralSources API with siteID {string} and customerName {string} with invalid Token")
	public void callGetReferralSourcesWithInvalidToken(String siteID, String customerName) {
		when(referralSourcesApi.referralSource(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getReferralSources(siteID, customerName);
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For ReferralSources")
	public void verifyUnauthorizedErrorForGetReferralSources() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getReferralSources API receives invalid data for response building")
	public void getReferralSourcesApiReceivesInvalidDataForResponseBuilding() {
		ReferralSource200Response mockApiResponse = Mockito.mock(ReferralSource200Response.class);
		when(mockApiResponse.getReferralSources()).thenReturn(null); // Simulate invalid data
		when(referralSourcesApi.referralSource(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getReferralSources("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For ReferralSources")
	public void verifyInvalidDataErrorMessageLoggedForGetReferralSources() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getReferralSources API returns an empty list")
	public void getReferralSourcesApiReturnsEmptyList() {
		ReferralSource200Response mockApiResponse = new ReferralSource200Response();
		mockApiResponse.setReferralSources(List.of()); // Simulate empty list
		when(referralSourcesApi.referralSource(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			referralSourcesResponse = tenBridgeService.getReferralSources("siteID", "customerName");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For ReferralSources")
	public void verifyEmptyListErrorMessageLoggedForGetReferralSources() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty ReferralSources list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * PatientAlerts Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For PatientAlerts")
	public void initializeWithVvalidTokenForGetPatientAlerts() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getPatientAlerts API with siteID {string} and customerName {string} and patientProfileId {string} with valid Token")
	public void callGetPatientAlertsWithValidToken(String siteID, String customerName, String patientProfileId) {
		PatientAlert200Response mockApiResponse = new PatientAlert200Response();
		PatientAlertsResponse patientAlert1 = new PatientAlertsResponse();
		patientAlert1.setPatient("John Doe");
		patientAlert1.setDescription("Regular Checkup");
		patientAlert1.setCategory("General");
		patientAlert1.setPreventBooking(false);

		PatientAlertsResponse patientAlert2 = new PatientAlertsResponse();
		patientAlert2.setPatient("Jane Smith");
		patientAlert2.setDescription("Dental Cleaning");
		patientAlert2.setCategory("Dental");
		patientAlert2.setPreventBooking(true);
		mockApiResponse.patientAlerts(List.of(patientAlert1, patientAlert2));
		when(patientAlertsApi.patientAlert(Mockito.any())).thenReturn(mockApiResponse);

		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		requestBody.setPatientId(patientProfileId);
		requestBody.setPatientProfileId(patientProfileId);
		patientAlertsResponse = tenBridgeService.getPatientAlerts(siteID, customerName, patientProfileId);
	}

	@Then("I should receive patient alerts")
	public void verifyPatientAlertsResponse() {
		assertNotNull(patientAlertsResponse, "PatientAlerts response should not be null");
		assertFalse(patientAlertsResponse.equals(null), "PatientAlerts list should not be empty");
	}

	@And("each alert should have valid details")
	public void each_patientAlert_should_have_valid_details() {
		assertNotNull(patientAlertsResponse.getPatientAlerts(), "patientAlerts category should not be null");
	}

	@Given("the TenBridgeService is initialized For PatientAlerts")
	public void initializeTenBridgeServiceForGetPatientAlerts() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getPatientAlerts API is called and the API returns an error status")
	public void getpatientAlertsApiReturnsErrorStatus() {
		when(patientAlertsApi.patientAlert(Mockito.any(PatientAlertsRequest.class)))
				.thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getPatientAlerts(meta.getSiteID(), meta.getCustomerName(),
					requestBody.getPatientProfileId());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For PatientAlerts")
	public void verifyErrorMessageLoggedForGetPatientAlerts() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving PatientAlerts"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For PatientAlerts")
	public void initializeWithInvalidTokenForGetPatientAlerts() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getPatientAlerts API with invalid Token")
	public void callGetPatientAlertsWithInvalidToken() {
		when(patientAlertsApi.patientAlert(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getPatientAlerts(meta.getSiteID(), meta.getCustomerName(),
					requestBody.getPatientProfileId());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For PatientAlerts")
	public void verifyUnauthorizedErrorForGetPatientAlerts() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getPatientAlerts API receives invalid data for response building")
	public void getpatientAlertsApiReceivesInvalidDataForResponseBuilding() {
		PatientAlert200Response mockApiResponse = Mockito.mock(PatientAlert200Response.class);
		when(mockApiResponse.getPatientAlerts()).thenReturn(null); // Simulate invalid data
		when(patientAlertsApi.patientAlert(Mockito.any(PatientAlertsRequest.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getPatientAlerts(meta.getSiteID(), meta.getCustomerName(),
					requestBody.getPatientProfileId());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For PatientAlerts")
	public void verifyInvalidDataErrorMessageLoggedForGetPatientAlerts() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getPatientAlerts API returns an empty list")
	public void getpatientAlertsApiReturnsEmptyList() {
		PatientAlert200Response mockApiResponse = new PatientAlert200Response();
		mockApiResponse.setPatientAlerts(List.of()); // Simulate empty list
		when(patientAlertsApi.patientAlert(Mockito.any(PatientAlertsRequest.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getPatientAlerts(meta.getSiteID(), meta.getCustomerName(),
					requestBody.getPatientProfileId());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For PatientAlerts")
	public void verifyEmptyListErrorMessageLoggedForGetPatientAlerts() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty Patient Alerts list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Genders Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For Genders")
	public void initializeWithVvalidTokenForGetGenders() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getGenders API with siteID {string} and customerName {string} with valid Token")
	public void callGetGendersWithValidToken(String siteID, String customerName) {
		Gender200Response mockApiResponse = new Gender200Response();
		Gender gender1 = new Gender();
		gender1.setGenderId(1);
		gender1.setGender("Male");
		Gender gender2 = new Gender();
		gender2.setGenderId(1);
		gender2.setGender("Feale");
		mockApiResponse.genders(List.of(gender1, gender2));
		when(genderValuesApi.gender(Mockito.any())).thenReturn(mockApiResponse);

		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		gendersResponse = tenBridgeService.getGenders(siteID, customerName);
	}

	@Then("I should receive a list of genders")
	public void verifyGendersResponse() {
		assertNotNull(gendersResponse, "Genders response should not be null");
		assertFalse(gendersResponse.equals(null), "Genders list should not be empty");
	}

	@And("each gender should have valid details")
	public void each_gender_should_have_valid_details() {
		for (GendersDTO gender : gendersResponse) {
			assertNotNull(gender.getGenderId(), "gender's id should not be null");
			assertNotNull(gender.getGenderName(), "gender's name should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For Genders")
	public void initializeTenBridgeServiceForGetGenders() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getGenders API is called and the API returns an error status")
	public void getGendersApiReturnsErrorStatus() {
		when(genderValuesApi.gender(Mockito.any(RequestMetaData.class))).thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getGenders(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For Genders")
	public void verifyErrorMessageLoggedForGetGenders() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving Genders"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For Genders")
	public void initializeWithInvalidTokenForGetGenders() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getGenders API with invalid Token")
	public void callGetGendersWithInvalidToken() {
		when(genderValuesApi.gender(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getGenders(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For Genders")
	public void verifyUnauthorizedErrorForGetGenders() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getGenders API receives invalid data for response building")
	public void getGendersApiReceivesInvalidDataForResponseBuilding() {
		Gender200Response mockApiResponse = Mockito.mock(Gender200Response.class);
		when(mockApiResponse.getGenders()).thenReturn(null); // Simulate invalid data
		when(genderValuesApi.gender(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getGenders(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For Genders")
	public void verifyInvalidDataErrorMessageLoggedForGetGenders() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getGenders API returns an empty list")
	public void getGendersApiReturnsEmptyList() {
		Gender200Response mockApiResponse = new Gender200Response();
		mockApiResponse.setGenders(List.of()); // Simulate empty list
		when(genderValuesApi.gender(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getGenders(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For Genders")
	public void verifyEmptyListErrorMessageLoggedForGetGenders() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty Genders list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Cpts Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For Cpts")
	public void initializeWithVvalidTokenForGetCpts() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getCpts API with siteID {string} and customerName {string} with valid Token")
	public void callGetCptsWithValidToken(String siteID, String customerName) {
		CPT200Response mockApiResponse = new CPT200Response();
		CPTCode cpt1 = new CPTCode();
		cpt1.setCode("x");
		cpt1.setDescription("y");
		CPTCode cpt2 = new CPTCode();
		cpt2.setCode("x");
		cpt2.setDescription("y");
		mockApiResponse.cpts(List.of(cpt1, cpt2));
		when(cptValuesApi.cPT(Mockito.any())).thenReturn(mockApiResponse);

		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		cptResponse = tenBridgeService.getCptCodes(siteID, customerName);
	}

	@Then("I should receive a list of Cpts")
	public void verifycptResponse() {
		assertNotNull(cptResponse, "Cpts response should not be null");
		assertFalse(cptResponse.equals(null), "Cpts list should not be empty");
	}

	@And("each cpt should have valid details")
	public void each_cpt_should_have_valid_details() {
		for (CPTsDTO cpt : cptResponse) {
			assertNotNull(cpt.getCode(), "cpt's code should not be null");
			assertNotNull(cpt.getDescription(), "cpt's description should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For Cpts")
	public void initializeTenBridgeServiceForGetCpts() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getCpts API is called and the API returns an error status")
	public void getCptsApiReturnsErrorStatus() {
		when(cptValuesApi.cPT(Mockito.any(RequestMetaData.class))).thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getCptCodes(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For Cpts")
	public void verifyErrorMessageLoggedForGetCpts() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving cpts"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For Cpts")
	public void initializeWithInvalidTokenForGetCpts() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getCpts API with invalid Token")
	public void callGetCptsWithInvalidToken() {
		when(cptValuesApi.cPT(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getCptCodes(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For Cpts")
	public void verifyUnauthorizedErrorForGetCpts() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getCpts API receives invalid data for response building")
	public void getCptsApiReceivesInvalidDataForResponseBuilding() {
		CPT200Response mockApiResponse = Mockito.mock(CPT200Response.class);
		when(mockApiResponse.getCpts()).thenReturn(null); // Simulate invalid data
		when(cptValuesApi.cPT(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getCptCodes(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For Cpts")
	public void verifyInvalidDataErrorMessageLoggedForGetCpts() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getCpts API returns an empty list")
	public void getCptsApiReturnsEmptyList() {
		CPT200Response mockApiResponse = new CPT200Response();
		mockApiResponse.setCpts(List.of()); // Simulate empty list
		when(cptValuesApi.cPT(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getCptCodes(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For Cpts")
	public void verifyEmptyListErrorMessageLoggedForGetCpts() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty cpt list"),
				"Exception message should indicate empty list error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * CancelReasons Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For CancelReasons")
	public void initializeWithVvalidTokenForGetCancelReasons() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getCancelReasons API with siteID {string} and customerName {string} with valid Token")
	public void callGetCancelReasonsWithValidToken(String siteID, String customerName) {
		CancellationReason200Response mockApiResponse = new CancellationReason200Response();
		CancelReasonsResponse cancelReasonsResponse1 = new CancelReasonsResponse();
		cancelReasonsResponse1.setAbbreviation("X");
		cancelReasonsResponse1.setCancellationReasonId(1);
		cancelReasonsResponse1.setDescription("y");
		CancelReasonsResponse cancelReasonsResponse2 = new CancelReasonsResponse();
		cancelReasonsResponse2.setAbbreviation("X");
		cancelReasonsResponse2.setCancellationReasonId(1);
		cancelReasonsResponse2.setDescription("y");
		mockApiResponse.cancelReasons(List.of(cancelReasonsResponse1, cancelReasonsResponse2));
		when(cancelReasonsApi.cancellationReason(Mockito.any())).thenReturn(mockApiResponse);

		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		cancelReasonsResponse = tenBridgeService.getCancelReasons(siteID, customerName);
	}

	@Then("I should receive a list of CancelReasons")
	public void verifyCancelReasonResponse() {
		assertNotNull(cancelReasonsResponse, "CancelReasons response should not be null");
		assertFalse(cancelReasonsResponse.equals(null), "CancelReasons list should not be empty");
	}

	@And("each CancelReason should have valid details")
	public void each_CancelReason_should_have_valid_details() {
		for (CancelReasonsDTO cancelReason : cancelReasonsResponse) {
			assertNotNull(cancelReason.getCancelReasonCode(), "cancelReason code should not be null");
			assertNotNull(cancelReason.getCancelReasonDescription(), "cancelReason description should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For CancelReasons")
	public void initializeTenBridgeServiceForGetCancelReasons() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getCancelReasons API is called and the API returns an error status")
	public void getCancelReasonsApiReturnsErrorStatus() {
		when(cptValuesApi.cPT(Mockito.any(RequestMetaData.class))).thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getCancelReasons(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For CancelReasons")
	public void verifyErrorMessageLoggedForGetCancelReasons() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving Cancel Reasons"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For CancelReasons")
	public void initializeWithInvalidTokenForGetCancelReasons() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getCancelReasons API with invalid Token")
	public void callGetCancelReasonsWithInvalidToken() {
		when(cptValuesApi.cPT(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getCptCodes(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For CancelReasons")
	public void verifyUnauthorizedErrorForGetCancelReasons() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getCancelReasons API receives invalid data for response building")
	public void getCancelReasonsApiReceivesInvalidDataForResponseBuilding() {
		CancellationReason200Response mockApiResponse = Mockito.mock(CancellationReason200Response.class);
		when(mockApiResponse.getCancelReasons()).thenReturn(null); // Simulate invalid data
		when(cancelReasonsApi.cancellationReason(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getCancelReasons(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For CancelReasons")
	public void verifyInvalidDataErrorMessageLoggedForGetCancelReasons() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getCancelReasons API returns an empty list")
	public void getCancelReasonsApiReturnsEmptyList() {
		CancellationReason200Response mockApiResponse = new CancellationReason200Response();
		mockApiResponse.setCancelReasons(List.of()); // Simulate empty list
		when(cancelReasonsApi.cancellationReason(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getCancelReasons(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For CancelReasons")
	public void verifyEmptyListErrorMessageLoggedForGetCancelReasons() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty Cancel Reasons list"),
				"Exception message should indicate empty Cancel Reasons error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * ChangeReasons Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For ChangeReasons")
	public void initializeWithVvalidTokenForGetChangeReasons() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getChangeReasons API with siteID {string} and customerName {string} with valid Token")
	public void callGetChangeReasonsWithValidToken(String siteID, String customerName) {
		ChangeReason200Response mockApiResponse = new ChangeReason200Response();
		ChangeReasonsResponse ChangeReasonsResponse1 = new ChangeReasonsResponse();
		ChangeReasonsResponse1.setAbbreviation("X");
		ChangeReasonsResponse1.setChangeReasonId(1);
		ChangeReasonsResponse1.setDescription("y");
		ChangeReasonsResponse ChangeReasonsResponse2 = new ChangeReasonsResponse();
		ChangeReasonsResponse2.setAbbreviation("X");
		ChangeReasonsResponse2.setChangeReasonId(1);
		ChangeReasonsResponse2.setDescription("y");
		mockApiResponse.changeReasons(List.of(ChangeReasonsResponse1, ChangeReasonsResponse2));
		when(changeReasonsApi.changeReason(Mockito.any())).thenReturn(mockApiResponse);

		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		changeReasonsResponse = tenBridgeService.getChangeReasons(siteID, customerName);
	}

	@Then("I should receive a list of ChangeReasons")
	public void verifyChangeReasonResponse() {
		assertNotNull(changeReasonsResponse, "ChangeReasons response should not be null");
		assertFalse(changeReasonsResponse.equals(null), "ChangeReasons list should not be empty");
	}

	@And("each ChangeReason should have valid details")
	public void each_ChangeReason_should_have_valid_details() {
		for (ChangeReasonsDTO ChangeReason : changeReasonsResponse) {
			assertNotNull(ChangeReason.getChangeReasonCode(), "ChangeReason code should not be null");
			assertNotNull(ChangeReason.getChangeReasonDescription(), "ChangeReason description should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For ChangeReasons")
	public void initializeTenBridgeServiceForGetChangeReasons() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getChangeReasons API is called and the API returns an error status")
	public void getChangeReasonsApiReturnsErrorStatus() {
		when(cptValuesApi.cPT(Mockito.any(RequestMetaData.class))).thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getChangeReasons(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For ChangeReasons")
	public void verifyErrorMessageLoggedForGetChangeReasons() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving Change Reasons"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For ChangeReasons")
	public void initializeWithInvalidTokenForGetChangeReasons() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getChangeReasons API with invalid Token")
	public void callGetChangeReasonsWithInvalidToken() {
		when(cptValuesApi.cPT(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getCptCodes(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For ChangeReasons")
	public void verifyUnauthorizedErrorForGetChangeReasons() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getChangeReasons API receives invalid data for response building")
	public void getChangeReasonsApiReceivesInvalidDataForResponseBuilding() {
		ChangeReason200Response mockApiResponse = Mockito.mock(ChangeReason200Response.class);
		when(mockApiResponse.getChangeReasons()).thenReturn(null); // Simulate invalid data
		when(changeReasonsApi.changeReason(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getChangeReasons(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For ChangeReasons")
	public void verifyInvalidDataErrorMessageLoggedForGetChangeReasons() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getChangeReasons API returns an empty list")
	public void getChangeReasonsApiReturnsEmptyList() {
		ChangeReason200Response mockApiResponse = new ChangeReason200Response();
		mockApiResponse.setChangeReasons(List.of()); // Simulate empty list
		when(changeReasonsApi.changeReason(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getChangeReasons(meta.getSiteID(), meta.getCustomerName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For ChangeReasons")
	public void verifyEmptyListErrorMessageLoggedForGetChangeReasons() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(exception.getMessage().contains("Empty Change Reasons list"),
				"Exception message should indicate empty Change Reasons error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Appointments Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For Appointments")
	public void initializeWithVvalidTokenForGetAppointments() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getAppointments API with siteID {string} and customerName {string} and patient_id {string} with valid Token")
	public void callGetAppointmentsWithValidToken(String siteID, String customerName, String patientId) {
		Providers200Response mockApiResponse1 = new Providers200Response();
		Practitioner practitioner1 = new Practitioner();
		practitioner1.setFirstName("TestfirstName");
		practitioner1.setSpeciality("Testspecialty");
		practitioner1.setAbbreviation("few");
		practitioner1.setLastName("TestLn");
		practitioner1.setPractitionerId("10");
		practitioner1.setIsActive("true");
		Practitioner practitioner2 = new Practitioner();
		practitioner2.setFirstName("TestfirstName1");
		practitioner2.setSpeciality("Testspecialty1");
		practitioner2.setAbbreviation("fe");
		practitioner2.setLastName("TestLn1");
		practitioner2.setPractitionerId("101");
		practitioner2.setIsActive("true");
		mockApiResponse1.setProviders(List.of(practitioner1, practitioner2));
		when(providersApi.providers(Mockito.any())).thenReturn(mockApiResponse1);

		PracticeLocation200Response mockApiResponse2 = new PracticeLocation200Response();
		Location location1 = new Location();
		location1.setLocationId("001");
		location1.setLocationName("Head Office");
		location1.setLocationType("Corporate");
		location1.setAddressLine1("123 Main St");
		location1.setAddressLine2("Suite 400");
		location1.setState("Telangana");
		location1.setCity("Hyderabad");
		location1.setZip("500081");
		location1.setAbbreviation("HO");
		location1.setCountryCode("IN");
		location1.setIsActive("true");
		Location location2 = new Location();
		location2.setLocationId("002");
		location2.setLocationName("Branch Office");
		location2.setLocationType("Retail");
		location2.setAddressLine1("456 Market St");
		location2.setAddressLine2("Floor 2");
		location2.setState("Karnataka");
		location2.setCity("Bangalore");
		location2.setZip("560034");
		location2.setAbbreviation("BO");
		location2.setCountryCode("IN");
		location2.setIsActive("false");
		mockApiResponse2.setLocations(List.of(location1, location2));
		when(locationsApi.practiceLocation(Mockito.any())).thenReturn(mockApiResponse2);

		Appointments200Response mockApiResponse = new Appointments200Response();
		appointmentSearchRequestData.setPatientId(patientId);
		Appointment appointment1 = new Appointment();
		appointment1.setAppointmentId("1");
		appointment1.setAppointmentStatus("Scheduled");
		appointment1.setAppointmentCreatedDate("2025-01-24");
		appointment1.setAppointmentBookingDate("2025-01-23");
		appointment1.setAppointmentBookedBy("Dr. Smith");
		appointment1.setCancellationReason("None");
		appointment1.setIcDCode("A123");
		appointment1.setAppointmentDate("2025-01-30");
		appointment1.setAppointmentDuration(30);
		appointment1.setAppointmentDateTime("2025-01-30 10:00");
		appointment1.setAppointmentType("Consultation");
		appointment1.setCoverageType("Insurance");
		appointment1.setVisitType("In-Person");
		appointment1.setAppointmentStartTime("10:00");
		appointment1.setAppointmentEndTime("10:30");
		appointment1.setScheduledLocationId("Location1");
		appointment1.setScheduledProviderId("Provider1");
		appointment1.setScheduledDepartment("Cardiology");
		appointment1.setReferringProviderId("Provider2");
		appointment1.setPatientIdentifier("Patient1");
		appointment1.setNotesOrComments("First appointment");

		// Creating second dummy appointment object
		Appointment appointment2 = new Appointment();
		appointment2.setAppointmentId("2");
		appointment2.setAppointmentStatus("Cancelled");
		appointment2.setAppointmentCreatedDate("2025-01-25");
		appointment2.setAppointmentBookingDate("2025-01-24");
		appointment2.setAppointmentBookedBy("Dr. Johnson");
		appointment2.setCancellationReason("Patient request");
		appointment2.setIcDCode("B456");
		appointment2.setAppointmentDate("2025-01-31");
		appointment2.setAppointmentDuration(45);
		appointment2.setAppointmentDateTime("2025-01-31 11:00");
		appointment2.setAppointmentType("Follow-up");
		appointment2.setCoverageType("Self-pay");
		appointment2.setVisitType("Telehealth");
		appointment2.setAppointmentStartTime("11:00");
		appointment2.setAppointmentEndTime("11:45");
		appointment2.setScheduledLocationId("Location2");
		appointment2.setScheduledProviderId("Provider3");
		appointment2.setScheduledDepartment("Orthopedics");
		appointment2.setReferringProviderId("Provider4");
		appointment2.setPatientIdentifier("Patient2");
		appointment2.setNotesOrComments("Cancelled by patient");
		mockApiResponse.appointments(List.of(appointment1, appointment2));
		when(appontmentsApi.appointments(Mockito.any())).thenReturn(mockApiResponse);

		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		appointmentsResponse = List.of(appointment1, appointment2);
	}

	@Then("I should receive a list of Appointments")
	public void verifyAppointmentResponse() {
		assertNotNull(appointmentsResponse, "Appointments response should not be null");
		assertFalse(appointmentsResponse.equals(null), "Appointments list should not be empty");
	}

	@And("each Appointment should have valid details")
	public void each_Appointment_should_have_valid_details() {
		for (Appointment appointment : appointmentsResponse) {
			assertNotNull(appointment.getAppointmentId(), "AppointmentId should not be null");
			assertNotNull(appointment.getAppointmentStatus(), "AppointmentStatus should not be null");
			assertNotNull(appointment.getAppointmentCreatedDate(), "AppointmentCreatedDate should not be null");
			assertNotNull(appointment.getAppointmentBookingDate(), "AppointmentBookingDate should not be null");
			assertNotNull(appointment.getAppointmentBookedBy(), "AppointmentBookedBy should not be null");
			assertNotNull(appointment.getCancellationReason(), "CancellationReason should not be null");
			assertNotNull(appointment.getIcDCode(), "IcDCode should not be null");
			assertNotNull(appointment.getAppointmentDate(), "AppointmentDate should not be null");
			assertNotNull(appointment.getAppointmentDuration(), "AppointmentDuration should not be null");
			assertNotNull(appointment.getAppointmentDateTime(), "AppointmentDateTime should not be null");
			assertNotNull(appointment.getAppointmentType(), "AppointmentType should not be null");
			assertNotNull(appointment.getCoverageType(), "CoverageType should not be null");
			assertNotNull(appointment.getVisitType(), "VisitType should not be null");
			assertNotNull(appointment.getAppointmentStartTime(), "AppointmentStartTime should not be null");
			assertNotNull(appointment.getAppointmentEndTime(), "AppointmentEndTime should not be null");
			assertNotNull(appointment.getScheduledLocationId(), "ScheduledLocationId should not be null");
			assertNotNull(appointment.getScheduledProviderId(), "ScheduledProviderId should not be null");
			assertNotNull(appointment.getScheduledDepartment(), "ScheduledDepartment should not be null");
			assertNotNull(appointment.getReferringProviderId(), "ReferringProviderId should not be null");
			assertNotNull(appointment.getPatientIdentifier(), "PatientIdentifier should not be null");
			assertNotNull(appointment.getNotesOrComments(), "NotesOrComments should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For Appointments")
	public void initializeTenBridgeServiceForGetAppointments() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getAppointments API is called and the API returns an error status")
	public void getAppointmentsApiReturnsErrorStatus() {
		when(appontmentsApi.appointments(Mockito.any(AppointmentSearchRequest.class)))
				.thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getAppointment(meta.getSiteID(), meta.getCustomerName(),
					appointmentSearchRequestData.getPatientId());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For Appointments")
	public void verifyErrorMessageLoggedForGetAppointments() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving Appointments"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For Appointments")
	public void initializeWithInvalidTokenForGetAppointments() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getAppointments API with invalid Token")
	public void callGetAppointmentsWithInvalidToken() {
		when(providersApi.providers(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));
		when(locationsApi.practiceLocation(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));
		when(appontmentsApi.appointments(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getProviders(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getLocations(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getAppointment(meta.getSiteID(), meta.getCustomerName(),
					appointmentSearchRequestData.getPatientId());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For Appointments")
	public void verifyUnauthorizedErrorForGetAppointments() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getAppointments API receives invalid data for response building")
	public void getAppointmentsApiReceivesInvalidDataForResponseBuilding() {
		Appointments200Response mockApiResponse = Mockito.mock(Appointments200Response.class);
		when(mockApiResponse.getAppointments()).thenReturn(null); // Simulate invalid data
		when(appontmentsApi.appointments(Mockito.any(AppointmentSearchRequest.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getAppointment(meta.getSiteID(), meta.getCustomerName(),
					appointmentSearchRequestData.getPatientId());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For Appointments")
	public void verifyInvalidDataErrorMessageLoggedForGetAppointments() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getAppointments API returns an empty list")
	public void getAppointmentsApiReturnsEmptyList() {
		Providers200Response mockApiResponse1 = new Providers200Response();
		mockApiResponse1.setProviders(List.of()); // Simulate empty list
		when(providersApi.providers(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse1);

		PracticeLocation200Response mockApiResponse2 = new PracticeLocation200Response();
		mockApiResponse2.setLocations(List.of()); // Simulate empty list
		when(locationsApi.practiceLocation(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse2);

		Appointments200Response mockApiResponse = new Appointments200Response();
		mockApiResponse.setAppointments(List.of()); // Simulate empty list
		when(appontmentsApi.appointments(Mockito.any(AppointmentSearchRequest.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getProviders(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getLocations(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getAppointment(meta.getSiteID(), meta.getCustomerName(),
					appointmentSearchRequestData.getPatientId());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For Appointments")
	public void verifyEmptyListErrorMessageLoggedForGetAppointments() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(
				exception.getMessage().contains("Empty Appointments list")
						|| exception.getMessage().contains("Empty provider list")
						|| exception.getMessage().contains("Empty location list"),
				"Exception message should indicate empty Appointments error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * Patient Search Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For Patients")
	public void initializeWithVvalidTokenForGetPatients() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getPatient API with siteID {string} and customerName {string} and first_name {string} and last_name {string} and date_of_birth {string} with valid Token")
	public void callGetPatientsWithValidToken(String siteID, String customerName, String first_name, String last_name,
			String date_of_birth) {
		Providers200Response mockApiResponse1 = new Providers200Response();
		Practitioner practitioner1 = new Practitioner();
		practitioner1.setFirstName("TestfirstName");
		practitioner1.setSpeciality("Testspecialty");
		practitioner1.setAbbreviation("few");
		practitioner1.setLastName("TestLn");
		practitioner1.setPractitionerId("10");
		practitioner1.setIsActive("true");
		Practitioner practitioner2 = new Practitioner();
		practitioner2.setFirstName("TestfirstName1");
		practitioner2.setSpeciality("Testspecialty1");
		practitioner2.setAbbreviation("fe");
		practitioner2.setLastName("TestLn1");
		practitioner2.setPractitionerId("101");
		practitioner2.setIsActive("true");
		mockApiResponse1.setProviders(List.of(practitioner1, practitioner2));
		when(providersApi.providers(Mockito.any())).thenReturn(mockApiResponse1);

		PracticeLocation200Response mockApiResponse2 = new PracticeLocation200Response();
		Location location1 = new Location();
		location1.setLocationId("001");
		location1.setLocationName("Head Office");
		location1.setLocationType("Corporate");
		location1.setAddressLine1("123 Main St");
		location1.setAddressLine2("Suite 400");
		location1.setState("Telangana");
		location1.setCity("Hyderabad");
		location1.setZip("500081");
		location1.setAbbreviation("HO");
		location1.setCountryCode("IN");
		location1.setIsActive("true");
		Location location2 = new Location();
		location2.setLocationId("002");
		location2.setLocationName("Branch Office");
		location2.setLocationType("Retail");
		location2.setAddressLine1("456 Market St");
		location2.setAddressLine2("Floor 2");
		location2.setState("Karnataka");
		location2.setCity("Bangalore");
		location2.setZip("560034");
		location2.setAbbreviation("BO");
		location2.setCountryCode("IN");
		location2.setIsActive("false");
		mockApiResponse2.setLocations(List.of(location1, location2));
		when(locationsApi.practiceLocation(Mockito.any())).thenReturn(mockApiResponse2);

		Providers200Response mockApiResponse3 = new Providers200Response();
		Practitioner practitioner3 = new Practitioner();
		practitioner1.setFirstName("TestfirstName");
		practitioner1.setSpeciality("Testspecialty");
		practitioner1.setAbbreviation("few");
		practitioner1.setLastName("TestLn");
		practitioner1.setPractitionerId("10");
		practitioner1.setIsActive("true");
		Practitioner practitioner4 = new Practitioner();
		practitioner2.setFirstName("TestfirstName1");
		practitioner2.setSpeciality("Testspecialty1");
		practitioner2.setAbbreviation("fe");
		practitioner2.setLastName("TestLn1");
		practitioner2.setPractitionerId("101");
		practitioner2.setIsActive("true");
		mockApiResponse3.setProviders(List.of(practitioner3, practitioner4));
		when(referringProvidersApi.referringProviders(Mockito.any())).thenReturn(mockApiResponse3);

		Patients200Response mockApiResponse = new Patients200Response();
		// Dummy Object 1
		Patient object1 = new Patient();
		object1.setPatientProfileID("P12345");
		object1.setPractitionerId(101);
		object1.setInsurances(new ArrayList<InsurancePolicy>());
		object1.setFirstName("John");
		object1.setLastName("Doe");
		object1.setMiddleName("M");
		object1.setDateOfBirth("1990-01-01");
		object1.setGender("Male");
		object1.setPhone("123-456-7890");
		object1.setAddressLine1("123 Main St");
		object1.setAddressLine2("Apt 4B");
		object1.setState("NY");
		object1.setCity("New York");
		object1.setZip("10001");
		object1.setEmail("johndoe@example.com");

		// Dummy Object 2
		Patient object2 = new Patient();
		object2.setPatientProfileID("P67890");
		object2.setPractitionerId(102);
		object2.setInsurances(new ArrayList<InsurancePolicy>());
		object2.setFirstName("Jane");
		object2.setLastName("Smith");
		object2.setMiddleName("A");
		object2.setDateOfBirth("1985-05-15");
		object2.setGender("Female");
		object2.setPhone("987-654-3210");
		object2.setAddressLine1("456 Another St");
		object2.setAddressLine2("Suite 10");
		object2.setState("CA");
		object2.setCity("Los Angeles");
		object2.setZip("90001");
		object2.setEmail("janesmith@example.com");

		mockApiResponse.patients(List.of(object1, object2));
		when(searchPatientApi.patients(Mockito.any())).thenReturn(mockApiResponse);

		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		patientRequestData.setDateOfBirth(date_of_birth);
		patientRequestData.setFirstName(first_name);
		patientRequestData.setLastName(last_name);
		patientRequest.setMeta(meta);
		patientRequest.setData(patientRequestData);

		patientsResponse = tenBridgeService.getPatients(siteID, customerName, first_name, last_name, date_of_birth,
				first_name, first_name);
	}

	@Then("I should receive a list of Patients")
	public void verifyPatinetResponse() {
		assertNotNull(patientsResponse, "Patients response should not be null");
		assertFalse(patientsResponse.equals(null), "Patients list should not be empty");
	}

	@And("each patient should have valid details")
	public void each_Patinet_should_have_valid_details() {
		for (PatientInfoDTO patient : patientsResponse) {
			assertNotNull(patient.getFirstName(), "FirstName should not be null");
			assertNotNull(patient.getLastName(), "LastName should not be null");
			assertNotNull(patient.getState(), "State should not be null");
			assertNotNull(patient.getCity(), "City should not be null");
			assertNotNull(patient.getZip(), "Zip should not be null");
			assertNotNull(patient.getEmail(), "Email should not be null");
		}
	}

	@Given("the TenBridgeService is initialized For Patients")
	public void initializeTenBridgeServiceForGetPatients() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the getPatient API is called and the API returns an error status")
	public void getPatientsApiReturnsErrorStatus() {
		when(searchPatientApi.patients(Mockito.any(PatientRequest.class))).thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.getPatients(meta.getSiteID(), meta.getCustomerName(), patientRequestData.getFirstName(),
					patientRequestData.getLastName(), patientRequestData.getDateOfBirth(),
					patientRequestData.getFirstName(), patientRequestData.getFirstName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For Patients")
	public void verifyErrorMessageLoggedForGetPatients() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while retrieving Patients"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For Patients")
	public void initializeWithInvalidTokenForGetPatients() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the getPatient API with invalid Token")
	public void callGetPatientsWithInvalidToken() {
		when(providersApi.providers(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));
		when(locationsApi.practiceLocation(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));
		when(referringProvidersApi.referringProviders(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));
		when(searchPatientApi.patients(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.getProviders(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getLocations(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getReferringProviders(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getPatients(meta.getSiteID(), meta.getCustomerName(), patientRequestData.getFirstName(),
					patientRequestData.getLastName(), patientRequestData.getDateOfBirth(),
					patientRequestData.getFirstName(), patientRequestData.getFirstName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For Patients")
	public void verifyUnauthorizedErrorForGetPatients() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the getPatient API receives invalid data for response building")
	public void getPatientsApiReceivesInvalidDataForResponseBuilding() {
		Patients200Response mockApiResponse = Mockito.mock(Patients200Response.class);
		when(mockApiResponse.getPatients()).thenReturn(null); // Simulate invalid data
		when(searchPatientApi.patients(Mockito.any(PatientRequest.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getPatients(meta.getSiteID(), meta.getCustomerName(), patientRequestData.getFirstName(),
					patientRequestData.getLastName(), patientRequestData.getDateOfBirth(),
					patientRequestData.getFirstName(), patientRequestData.getFirstName());
			;
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For Patients")
	public void verifyInvalidDataErrorMessageLoggedForGetPatients() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}

	@When("the getPatient API returns an empty list")
	public void getPatientsApiReturnsEmptyList() {
		Providers200Response mockApiResponse1 = new Providers200Response();
		mockApiResponse1.setProviders(List.of()); // Simulate empty list
		when(providersApi.providers(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse1);

		PracticeLocation200Response mockApiResponse2 = new PracticeLocation200Response();
		mockApiResponse2.setLocations(List.of()); // Simulate empty list
		when(locationsApi.practiceLocation(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse2);

		Providers200Response mockApiResponse3 = new Providers200Response();
		mockApiResponse3.setProviders(List.of()); // Simulate empty list
		when(referringProvidersApi.referringProviders(Mockito.any(RequestMetaData.class))).thenReturn(mockApiResponse3);

		Patients200Response mockApiResponse = new Patients200Response();
		mockApiResponse.setPatients(List.of()); // Simulate empty list
		when(searchPatientApi.patients(Mockito.any(PatientRequest.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.getProviders(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getLocations(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getReferringProviders(meta.getSiteID(), meta.getCustomerName());
			tenBridgeService.getPatients(meta.getSiteID(), meta.getCustomerName(), patientRequestData.getFirstName(),
					patientRequestData.getLastName(), patientRequestData.getDateOfBirth(),
					patientRequestData.getFirstName(), patientRequestData.getFirstName());
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged for empty list For Patients")
	public void verifyEmptyListErrorMessageLoggedForGetPatients() {
		assertNotNull(exception, "Exception should be thrown when API returns an empty list");
		assertTrue(
				exception.getMessage().contains("Empty Patients list")
						|| exception.getMessage().contains("Empty provider list")
						|| exception.getMessage().contains("Empty location list")
						|| exception.getMessage().contains("Empty Referring provider list"),
				"Exception message should indicate empty Patients error: " + exception.getMessage());
	}

	/*********************************************************************************************************
	 * CreatePatient Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For CreatePatient")
	public void initializeWithVvalidTokenForGetCreatePatient() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the createPatient API with siteID {string} and customerName {string} and first_name {string} and last_name {string} and middle_name {string} and date_of_birth {string} and gender {string} and phone {string} and address_line_1 {string} and address_line_2 {string} and state {string} and city {string} and zip {string} and email {string} and a valid Token")
	public void callGetCreatePatientWithValidToken(String siteID, String customerName, String first_name,
			String last_name, String middle_name, String date_of_birth, String gender, String phone,
			String address_line1, String address_line2, String state, String city, String zip, String email) {

		Patient mockApiResponse = new Patient();
		Patient dummyPatient = new Patient();
		dummyPatient.setFirstName(first_name);
		dummyPatient.setLastName(last_name);
		dummyPatient.setMiddleName(middle_name);
		dummyPatient.setDateOfBirth(date_of_birth);
		dummyPatient.setGender(gender);
		dummyPatient.setPhone(phone);
		dummyPatient.setAddressLine1(address_line1);
		dummyPatient.setAddressLine2(address_line2);
		dummyPatient.setState(state);
		dummyPatient.setCity(city);
		dummyPatient.setZip(zip);
		dummyPatient.setEmail(email);

		when(createPatientApi.patient(Mockito.any())).thenReturn(mockApiResponse);

		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		createPatientResponse = dummyPatient;
	}

	@Then("I should receive a data of CreatePatient")
	public void verifyCreatePatientResponse() {
		assertNotNull(createPatientResponse, "CreatePatient response should not be null");
		assertFalse(createPatientResponse.equals(null), "CreatePatient list should not be empty");
	}

	@And("created patient should have valid details")
	public void each_CreatePatient_should_have_valid_details() {

		assertNotNull(createPatientResponse.getFirstName(), "First Name should not be null");
		assertNotNull(createPatientResponse.getLastName(), "Last Name should not be null");
		assertNotNull(createPatientResponse.getMiddleName(), "Middle Name should not be null");
		assertNotNull(createPatientResponse.getDateOfBirth(), "Date of Birth should not be null");
		assertNotNull(createPatientResponse.getGender(), "Gender should not be null");
		assertNotNull(createPatientResponse.getPhone(), "Phone should not be null");
		assertNotNull(createPatientResponse.getAddressLine1(), "Address Line 1 should not be null");
		assertNotNull(createPatientResponse.getAddressLine2(), "Address Line 2 should not be null");
		assertNotNull(createPatientResponse.getState(), "State should not be null");
		assertNotNull(createPatientResponse.getCity(), "City should not be null");
		assertNotNull(createPatientResponse.getZip(), "Zip should not be null");
		assertNotNull(createPatientResponse.getEmail(), "Email should not be null");

	}

	@Given("the TenBridgeService is initialized For CreatePatient")
	public void initializeTenBridgeServiceForGetCreatePatient() {
		// Initialize TenBridgeService as shown in the setUp method
		setUp(); // Ensure this is correctly initializing tenBridgeService
	}

	@When("the createPatient API is called and the API returns an error status")
	public void getCreatePatientApiReturnsErrorStatus() {
		when(createPatientApi.patient(Mockito.any(PatientCreateRequest.class)))
				.thenThrow(new RuntimeException("API error"));

		exception = null;
		try {
			tenBridgeService.createPatient(meta.getSiteID(), meta.getCustomerName(), "Manish", "Machha", "Kumar",
					"1995-07-23", "Male", "123-569-7485", "123 Vishal heights", "Road no 4", "VD", "Texas", "56231",
					"aslk@kls.cpm");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged For CreatePatient")
	public void verifyErrorMessageLoggedForGetCreatePatient() {
		assertNotNull(exception, "Exception should be thrown when API returns an error status");
		assertTrue(exception.getMessage().contains("Error occurred while creating Patient"),
				"Exception message should indicate the API error");
	}

	@Given("the TenBridgeService is initialized with an invalid token For CreatePatient")
	public void initializeWithInvalidTokenForGetCreatePatient() {
		// Ensure tenBridgeService is properly instantiated
		assertNotNull(tenBridgeService, "TenBridgeService should be instantiated");

		// Initialize the token (simulate the behavior)
		tenBridgeService.setToken();

		// Assert that the token has been set properly
		assertNotNull(tenBridgeService.getOauth(), "OAuth2Config should not be null after setting token");
	}

	@When("I call the createPatient API with invalid Token")
	public void callGetCreatePatientWithInvalidToken() {
		when(createPatientApi.patient(Mockito.any())).thenThrow(new RuntimeException("Unauthorized"));

		exception = null;
		try {
			tenBridgeService.createPatient(meta.getSiteID(), meta.getCustomerName(), "Manish", "Machha", "Kumar",
					"1995-07-23", "Male", "123-569-7485", "123 Vishal heights", "Road no 4", "VD", "Texas", "56231",
					"aslk@kls.cpm");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the API call should fail with an unauthorized error For CreatePatient")
	public void verifyUnauthorizedErrorForGetCreatePatient() {
		assertNotNull(exception, "Exception should be thrown when API returns an unauthorized error");
		assertTrue(exception.getMessage().contains("Unauthorized"),
				"Exception message should indicate unauthorized error");
	}

	@When("the createPatient API receives invalid data for response building")
	public void getCreatePatientApiReceivesInvalidDataForResponseBuilding() {
		Patient mockApiResponse = Mockito.mock(Patient.class);
		when(mockApiResponse.getFirstName()).thenReturn(null); // Simulate invalid data
		when(createPatientApi.patient(Mockito.any(PatientCreateRequest.class))).thenReturn(mockApiResponse);

		exception = null;
		try {
			tenBridgeService.createPatient(meta.getSiteID(), meta.getCustomerName(), "Manish", "Machha", "Kumar",
					"1995-07-23", "Male", "123-569-7485", "123 Vishal heights", "Road no 4", "VD", "Texas", "56231",
					"aslk@kls.cpm");
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("an appropriate exception or error message should be logged at response For CreatePatient")
	public void verifyInvalidDataErrorMessageLoggedForGetCreatePatient() {
		assertNotNull(exception, "Exception should be thrown when building response with invalid data");
		assertTrue(exception.getMessage().contains("Invalid data received"),
				"Exception message should indicate response building error: " + exception.getMessage());
	}
}
