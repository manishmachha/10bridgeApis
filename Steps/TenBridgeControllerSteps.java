package com.ps.tenbridge.integration.steps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ps.tenbridge.datahub.controller.TenBridgeController;
import com.ps.tenbridge.datahub.controllerImpl.TenBridgeService;
import com.ps.tenbridge.datahub.dto.AppointmentInfoDTO;
import com.ps.tenbridge.datahub.dto.CancelReasonsDTO;
import com.ps.tenbridge.datahub.dto.ChangeReasonsDTO;
import com.ps.tenbridge.datahub.dto.EthnicityDTO;
import com.ps.tenbridge.datahub.dto.InsuranceDTO;
import com.ps.tenbridge.datahub.dto.LocationDTO;
import com.ps.tenbridge.datahub.dto.PatientInfoDTO;
import com.ps.tenbridge.datahub.dto.ProviderDTO;
import com.ps.tenbridge.datahub.dto.RacesDTO;
import com.ps.tenbridge.datahub.dto.ReferralSourcesDTO;
import com.ps.tenbridge.datahub.dto.ReferringProviderDTO;
import com.ps.tenbridge.datahub.dto.patientAlerts;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TenBridgeControllerSteps {

	@Mock
	private TenBridgeService tenBridgeService;

	@InjectMocks
	private TenBridgeController controller;

	private Map<String, String> request; // Declare the request object
	private ResponseEntity<Object> response;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this); // Initialize mocks
		request = new HashMap<>(); // Initialize the request map
	}

	// Step to handle valid request for races
	@Given("a request with valid attributes for races")
	public void aRequestWithValidAttributesForGettingRaces(Map<String, String> attributes) {
		request.putAll(attributes);
		// Mock service call for valid attributes
		RacesDTO race1 = new RacesDTO("Race1", "Description1");
		RacesDTO race2 = new RacesDTO("Race2", "Description12");
		RacesDTO race3 = new RacesDTO("Race3", "Description13");

		Mockito.when(tenBridgeService.getRaces("621", "OpargoEpicTest")).thenReturn(List.of(race1, race2, race3));
	}

	// Step to simulate service throwing an exception when calling races
	@Given("the service throws an exception when calling races")
	public void theServiceThrowsAnExceptionForRaces() {
		Mockito.when(tenBridgeService.getRaces(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// Step to request races
	@When("the client requests races")
	public void theClientRequestsRaces() {
		response = controller.getRaces(request);
	}

	// Step to handle valid request for ethnicities
	@Given("a request with valid attributes for ethnicities")
	public void aRequestWithValidAttributesForGettingEthnicities(Map<String, String> attributes) {
		request.putAll(attributes);
		// Mock service call for valid attributes
		EthnicityDTO ethnicity1 = new EthnicityDTO("Ethniciti1", "Description1");
		EthnicityDTO ethnicity2 = new EthnicityDTO("Ethniciti2", "Description12");
		EthnicityDTO ethnicity3 = new EthnicityDTO("Ethniciti3", "Description13");

		Mockito.when(tenBridgeService.getEthnicities("621", "OpargoEpicTest"))
				.thenReturn(List.of(ethnicity1, ethnicity2, ethnicity3));
	}

	// Step to simulate service throwing an exception when calling ethnicities
	@Given("the service throws an exception when calling ethnicities")
	public void theServiceThrowsAnExceptionForEthnicities() {
		Mockito.when(tenBridgeService.getEthnicities(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// Step to request locations
	@When("the client requests ethnicities")
	public void theClientRequestsEthnicities() {
		response = controller.getEthnicities(request);
	}

	// Step to handle valid request for providers
	@Given("a request with valid attributes for providers")
	public void aRequestWithValidAttributesForProviders(Map<String, String> attributes) {
		request.putAll(attributes);
		// Mock service call for valid attributes
		ProviderDTO provider1 = new ProviderDTO("TestfirstName", "TestSpecialty", "TestListName", "TestLastName",
				"TestAbbreviation", "TestProviderId", "true");
		ProviderDTO provider2 = new ProviderDTO("TestfirstName1", "TestSpecialty1", "TestListName1", "TestLastName1",
				"TestAbbreviation1", "TestProviderId1", "true");
		Mockito.when(tenBridgeService.getProviders("621", "OpargoEpicTest")).thenReturn(List.of(provider1, provider2));
	}

	// Step to simulate service throwing an exception when calling providers
	@Given("the service throws an exception when calling providers")
	public void theServiceThrowsAnExceptionForProviders() {
		Mockito.when(tenBridgeService.getProviders(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// Step to request providers
	@When("the client requests providers")
	public void theClientRequestsProviders() {
		response = controller.getProviders(request);
	}

	// Step to handle valid request for referringProviders
	@Given("a request with valid attributes for referringproviders")
	public void aRequestWithValidAttributesForReferringProviders(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * { "firstName": " PHYSICIAN", "referringproviderid": "E1021", "lastName":
		 * "PHOENIX", "abbreviation": "PHXMD", "specialty": "Transplant", "is_active":
		 * true }
		 */
		// Mock service call for valid attributes
		ReferringProviderDTO referringprovider1 = new ReferringProviderDTO("PHYSICIAN", "E1021", "PHOENIX", null,
				"PHXMD", "Transplant", "true", null);
		ReferringProviderDTO referringprovider2 = new ReferringProviderDTO("PHYSICIAN", "E1021", "PHOENIX", null,
				"PHXMD", "Transplant", null, true);
		Mockito.when(tenBridgeService.getReferringProviders("621", "OpargoEpicTest"))
				.thenReturn(List.of(referringprovider1, referringprovider2));
	}

	// Step to simulate service throwing an exception when calling
	// referringProviders
	@Given("the service throws an exception when calling referringproviders")
	public void theServiceThrowsAnExceptionForReferringProviders() {
		Mockito.when(tenBridgeService.getReferringProviders(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// steps to request referringProviders
	@When("the client requests referringproviders")
	public void theClientRequestsReferringProvider() {
		response = controller.getReferringProviders(request);
	}

	// Step to simulate service throwing an exception when calling cancelReasons
	@Given("the service throws an exception when calling cancelReasons")
	public void theServiceThrowsAnExceptionForCancelReasons() {
		Mockito.when(tenBridgeService.getCancelReasons(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// Step to handle valid request for locations
	@Given("a request with valid attributes for locations")
	public void aRequestWithValidAttributesForLocations(Map<String, String> attributes) {
		request.putAll(attributes);

		/*
		 * //sample response { "locationId": "1001991", "listName": "EPH GI",
		 * "address1": "123 Anywhere Street", "city": "Verona", "state": "WI", "zip":
		 * "53593", "abbreviation": "GI", "is_active": true }
		 */

		// Mock service call for valid attributes
		LocationDTO location1 = new LocationDTO("ladjfalkf1354354", "EPH GI", "123 Anywhere Street", "Verona", "WI",
				"53593", "GI", true);
		LocationDTO location2 = new LocationDTO("falkfladj1378354", "JGF GI", "564 Anywhere Street", "Naramada", "NI",
				"53597", "LK", true);
		Mockito.when(tenBridgeService.getLocations("621", "OpargoEpicTest")).thenReturn(List.of(location1, location2));
	}

	// Step to simulate service throwing an exception when calling locations
	@Given("the service throws an exception when calling locations")
	public void theServiceThrowsAnExceptionForLocations() {
		Mockito.when(tenBridgeService.getLocations(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// Step to request locations
	@When("the client requests locations")
	public void theClientRequestsLocations() {
		response = controller.getLocations(request);
	}

	// Step to handle valid request for insurances
	@Given("a request with valid attributes for insuranceCarriers")
	public void aRequestWithValidAttributesForInsurances(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * //sample response { "id": "100", "name": "AETNA", "standardizedName":
		 * "*Not Applicable", "address1": "PO BOX 981107", "city": "EL PASO", "state":
		 * "TX", "zip": "79998-1107", "phone": "555-555-5555", "coverageType":
		 * "Commercial", "is_active": true }
		 */

		// Mock service call for valid attributes
		InsuranceDTO insurances1 = new InsuranceDTO("100hjgjh", "AETNA", "AETNA", "PO BOX 981107", "EL PASO", "TX",
				"79998-1107", "555-555-5555", "Commercial", true);
		InsuranceDTO insurances2 = new InsuranceDTO("100hjgjh", "AETNA", "AETNA", "PO BOX 981107", "EL PASO", "TX",
				"79998-1107", "555-555-5555", "Commercial", true);
		Mockito.when(tenBridgeService.getInsurances("621", "OpargoEpicTest"))
				.thenReturn(List.of(insurances1, insurances2));
	}

	// Step to simulate service throwing an exception when calling insuranceCarriers
	@Given("the service throws an exception when calling insuranceCarriers")
	public void theServiceThrowsAnExceptionForInsuranceCarriers() {
		Mockito.when(tenBridgeService.getInsurances(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// steps to request insuranceCarriers
	@When("the client requests insuranceCarriers")
	public void theClientRequestsInsuranceCarriers() {
		response = controller.getInsurances(request);
	}

	// Step to handle valid request for cancel reasons
	@Given("a request with valid attributes for cancelReasons")
	public void aRequestWithValidAttributesForCancelReasons(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * //sample response { "cancelReasonCode": "1100", "cancelReasonDescription":
		 * "Scheduled from Wait List" }
		 */

		// Mock service call for valid attributes
		CancelReasonsDTO cancelReasons1 = new CancelReasonsDTO("1100", "Scheduled from Wait List");
		CancelReasonsDTO cancelReasons2 = new CancelReasonsDTO("1102", "CMS Therapy Cap Service Not Authorized");
		Mockito.when(tenBridgeService.getCancelReasons("621", "OpargoEpicTest"))
				.thenReturn(List.of(cancelReasons1, cancelReasons2));
	}

	// steps to request cancelReasons
	@When("the client requests cancelReasons")
	public void theClientRequestsCancelReason() {
		response = controller.getCancelReasons(request);
	}

	// Step to handle valid request for change reasons
	@Given("a request with valid attributes for changeReasons")
	public void aRequestWithValidAttributesForChangeReasons(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * //sample response { "changeReasonCode": "1100", "changeReasonDescription":
		 * "*Unknown" }
		 */

		// Mock service call for valid attributes
		ChangeReasonsDTO changeReasons1 = new ChangeReasonsDTO("1100", "Scheduled from Wait List");
		ChangeReasonsDTO changeReasons2 = new ChangeReasonsDTO("1102", "CMS Therapy Cap Service Not Authorized");
		Mockito.when(tenBridgeService.getChangeReasons("621", "OpargoEpicTest"))
				.thenReturn(List.of(changeReasons1, changeReasons2));
	}

	// Step to simulate service throwing an exception when calling changeReasons
	@Given("the service throws an exception when calling changeReasons")
	public void theServiceThrowsAnExceptionForChangeReasons() {
		Mockito.when(tenBridgeService.getChangeReasons(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// steps to request changeReasons
	@When("the client requests changeReasons")
	public void theClientRequestsChangeReason() {
		response = controller.getChangeReasons(request);
	}

	// Step to handle valid request for referral sources
	@Given("a request with valid attributes for referralSources")
	public void aRequestWithValidAttributesForReferralSources(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * //sample response { "id": 1100, "name": "Beacon" }
		 */

		// Mock service call for valid attributes
		ReferralSourcesDTO referralSources1 = new ReferralSourcesDTO(1100, "Beacon");
		ReferralSourcesDTO referralSources2 = new ReferralSourcesDTO(1102, "*Deleted");
		Mockito.when(tenBridgeService.getReferralSources("621", "OpargoEpicTest"))
				.thenReturn(List.of(referralSources1, referralSources2));
	}

	// Step to simulate service throwing an exception when calling referralSources
	@Given("the service throws an exception when calling referralSources")
	public void theServiceThrowsAnExceptionForReferralSources() {
		Mockito.when(tenBridgeService.getReferralSources(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// steps to request referralSources
	@When("the client requests referralSources")
	public void theClientRequestsReferralSource() {
		response = controller.getReferralSources(request);
	}

	// Step to handle valid request for patient alerts
	@Given("a request with valid attributes for patientAlerts")
	public void aRequestWithValidAttributesForPatientAlerts(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * //sample response { "patient": "eTplvxRvcd-eT1nEI8BvQRQ3", "description":
		 * "Contact", "category": "Isolation Flag", "preventBooking": "false" }
		 */

		// Mock service call for valid attributes
		patientAlerts patientAlerts = new patientAlerts();
		patientAlerts.setPatientAlerts(attributes);
		Mockito.when(tenBridgeService.getPatientAlerts("621", "OpargoEpicTest", "eTplvxRvcd-eT1nEI8BvQRQ3"))
				.thenReturn(patientAlerts);
	}

	// Step to simulate service throwing an exception when calling patientAlerts
	@Given("the service throws an exception when calling patientAlerts")
	public void theServiceThrowsAnExceptionForPatientAlerts() {
		Mockito.when(tenBridgeService.getPatientAlerts(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// steps to request patientAlerts
	@When("the client requests patientAlerts")
	public void theClientRequestsPatientAlert() {
		response = controller.getPatientAlerts(request);
	}

	// Step to handle valid request for PatientSearch
	@Given("a request with valid attributes for patientSearch")
	public void aRequestWithValidAttributesForPatientSearch(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * //sample response { "patient": "eTplvxRvcd-eT1nEI8BvQRQ3", "description":
		 * "Contact", "category": "Isolation Flag", "preventBooking": "false" }
		 */

		PatientInfoDTO patient1 = new PatientInfoDTO();
		PatientInfoDTO patient2 = new PatientInfoDTO();
		Mockito.when(tenBridgeService.getPatients("621", "OpargoEpicTest", "Theodore", "Mychart", "07-07-1948", null,
				null, null)).thenReturn(List.of(patient1, patient2));
	}

	// Step to simulate service throwing an exception when calling PatientSearch
	@Given("the service throws an exception when calling patientSearch")
	public void theServiceThrowsAnExceptionForPatientSearch() {
		Mockito.when(tenBridgeService.getPatients(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenThrow(new RuntimeException("Simulated service error"));
	}

	// steps to request patientSearch
	@When("the client requests patientSearch")
	public void theClientRequestsPatientSearch() {
		response = controller.getPatient(request);
	}

	// Step to handle valid request for AppointmentSearch
	@Given("a request with valid attributes for AppointmentSearch")
	public void aRequestWithValidAttributesForAppointmentSearch(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * //sample response { "patient": "eTplvxRvcd-eT1nEI8BvQRQ3", "description":
		 * "Contact", "category": "Isolation Flag", "preventBooking": "false" }
		 */

		AppointmentInfoDTO appointment1 = new AppointmentInfoDTO();
		AppointmentInfoDTO appointment2 = new AppointmentInfoDTO();
		Mockito.when(tenBridgeService.getAppointment("621", "OpargoEpicTest", "e63wRTbPfr1p8UW81d8Seiw3"))
				.thenReturn(List.of(appointment1, appointment2));
	}

	// Step to simulate service throwing an exception when calling AppointmentSearch
	@Given("the service throws an exception when calling AppointmentSearch")
	public void theServiceThrowsAnExceptionForAppointmentSearch() {
		Mockito.when(tenBridgeService.getAppointment(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// steps to request AppointmentSearch
	@When("the client requests AppointmentSearch")
	public void theClientRequestsAppointmentSearch() {
		response = controller.getAppointment(request);
	}

	// Step to handle valid request for BookAppointment
	@Given("a request with valid attributes for BookAppointment")
	public void aRequestWithValidAttributesForBookAppointment(Map<String, String> attributes) {
		request.putAll(attributes);
		/*
		 * //sample response { "patient": "eTplvxRvcd-eT1nEI8BvQRQ3", "description":
		 * "Contact", "category": "Isolation Flag", "preventBooking": "false" }
		 */

		AppointmentInfoDTO appointment1 = new AppointmentInfoDTO();
		AppointmentInfoDTO appointment2 = new AppointmentInfoDTO();
		Mockito.when(tenBridgeService.bookAppointment("621", "OpargoEpicTest", "2024-12-13", 0,
				"2024-12-13T17:28:55.793Z", "string", "string", "string", "string", "string", "string", "string",
				"2024-12-13T17:28:55.793Z", "2024-12-13T17:28:55.793Z", "string", "string", "string", "string",
				"etenCaqETW7InVSZEOPGeUjaVwWzM8rz6W76LYg0ZlJbTEhMPuTBMQ7GdQNF9plBT3", "e63wRTbPfr1p8UW81d8Seiw3",
				"testing booking of appointment")).thenReturn(List.of(appointment1, appointment2));
	}

	// Step to simulate service throwing an exception when calling AppointmentSearch
	@Given("the service throws an exception when calling BookAppointment")
	public void theServiceThrowsAnExceptionForBookAppointment() {
		Mockito.when(tenBridgeService.bookAppointment(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException("Simulated service error"));
	}

	// steps to request AppointmentSearch
	@When("the client requests BookAppointment")
	public void theClientRequestsBookAppointment() {
		response = controller.bookAppointment(request);
		System.out.println("RRRRRRRRR: " + response);
	}

	// Step to handle request with missing attributes
	@Given("a request with missing attributes")
	public void aRequestWithMissingAttributesForRace(Map<String, String> attributes) {
		request.putAll(attributes);
	}

	// Step to check response status
	@Then("the response status should be {int}")
	public void theResponseStatusShouldBe(int expectedStatus) {
		Assertions.assertEquals(expectedStatus, response.getStatusCodeValue(),
				"Expected response status " + expectedStatus + " but got " + response.getStatusCodeValue());
	}

	// Step to check if response body contains the expected content
	@Then("the response body should contain {string}")
	public void theResponseBodyShouldContain(String expectedContent) {
		String responseBody = response.getBody().toString();
		System.out.println(responseBody);
		Assertions.assertTrue(responseBody.contains(expectedContent),
				"Expected response body to contain: " + expectedContent + ", but got: " + responseBody);
	}
}
