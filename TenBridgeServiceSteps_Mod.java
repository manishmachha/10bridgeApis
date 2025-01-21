package com.ps.tenbridge.integration.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hibernate.validator.internal.util.privilegedactions.GetInstancesFromServiceLoader;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import com.ps.tenbridge.datahub.config.OAuth2Config;
import com.ps.tenbridge.datahub.controllerImpl.TenBridgeService;
import com.ps.tenbridge.datahub.dto.InsuranceDTO;
import com.ps.tenbridge.datahub.dto.LocationDTO;
import com.ps.tenbridge.datahub.dto.ProviderDTO;
import com.ps.tenbridge.datahub.dto.ReferringProviderDTO;
import com.ps.tenbridge.datahub.services.authentication.TokenService;
import com.veradigm.ps.tenbridge.client.ApiClient;
import com.veradigm.ps.tenbridge.client.api.GetPayorGroupsApi;
import com.veradigm.ps.tenbridge.client.api.GetPracticeLocationsApi;
import com.veradigm.ps.tenbridge.client.api.GetProvidersApi;
import com.veradigm.ps.tenbridge.client.api.GetReferringProvidersApi;
import com.veradigm.ps.tenbridge.client.models.InsuranceCarrier;
import com.veradigm.ps.tenbridge.client.models.Location;
import com.veradigm.ps.tenbridge.client.models.PayorGroups200Response;
import com.veradigm.ps.tenbridge.client.models.PracticeLocation200Response;
import com.veradigm.ps.tenbridge.client.models.Practitioner;
import com.veradigm.ps.tenbridge.client.models.Providers200Response;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TenBridgeServiceSteps_Mod {

	private List<ProviderDTO> providerResponse;

	private List<ReferringProviderDTO> ReferringroviderResponse;

	private List<LocationDTO> locationResponse;

	private List<InsuranceDTO> insuranceResponse;

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
		tenBridgeService = new TenBridgeService(apiClient, ts, oauth, providersApi, locationsApi, payorGroupsApi, referringProvidersApi);
	}

	/*********************************************************************************************************
	 * Locations Test cases
	 *********************************************************************************************************/

	@Given("the TenBridgeService is initialized with a valid token For getProviders")
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
		RequestMetaData meta = Mockito.mock(RequestMetaData.class);
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
		RequestMetaData meta = Mockito.mock(RequestMetaData.class);
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
			assertNotNull(referringProvider.getReferringproviderid(), "Referring provider's practitioner id should not be null");
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
		RequestMetaData meta = Mockito.mock(RequestMetaData.class);
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
		RequestMetaData meta = Mockito.mock(RequestMetaData.class);
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

}
