package com.ps.tenbridge.datahub.controller;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.tenbridge.datahub.controllerImpl.TenBridgeService;
import com.ps.tenbridge.datahub.utility.EncryptionHelper;
import com.ps.tenbridge.datahub.utility.EncryptionKeyConstants;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequest;
import com.veradigm.ps.tenbridge.client.models.NewPatient;
import com.veradigm.ps.tenbridge.client.models.PatientCreateRequest;
import com.veradigm.ps.tenbridge.client.models.Practitioner;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;

@RestController
@RequestMapping("/api")
public class TenBridgeController {
	private static final Logger logger = LoggerFactory.getLogger(TenBridgeController.class);

	private final TenBridgeService tenBridgeService;

	@Autowired
	public TenBridgeController(TenBridgeService tenBridgeService) {
		this.tenBridgeService = tenBridgeService;

	}

	@PostMapping("/providers")
	public ResponseEntity<Object> getProviders(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "providers",
				(siteID, customerName) -> tenBridgeService.getProviders(siteID, customerName));
	}

	@PostMapping("/locations")
	public ResponseEntity<Object> getLocations(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "locations",
				(siteID, customerName) -> tenBridgeService.getLocations(siteID, customerName));
	}

	@PostMapping("/insurances")
	public ResponseEntity<Object> getInsurances(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "insuranceCarriers",
				(siteID, customerName) -> tenBridgeService.getInsurances(siteID, customerName));
	}

	@PostMapping("/insurance-carriers")
	public ResponseEntity<Object> getInsuranceCarriers(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "insuranceCarriers",
				(siteID, customerName) -> tenBridgeService.getInsuranceCarriers(siteID, customerName));
	}

	@PostMapping("/referring-providers")
	public ResponseEntity<Object> getReferringProviders(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "referringproviders",
				(siteID, customerName) -> tenBridgeService.getReferringProviders(siteID, customerName));
	}

	@PostMapping("/ethnicities")
	public ResponseEntity<Object> getEthnicities(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "ethnicities",
				(siteID, customerName) -> tenBridgeService.getEthnicities(siteID, customerName));
	}

	@PostMapping("/races")
	public ResponseEntity<Object> getRaces(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "races",
				(siteID, customerName) -> tenBridgeService.getRaces(siteID, customerName));
	}

	@PostMapping("/cancel-reasons")
	public ResponseEntity<Object> getCancelReasons(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "cancelReasons",
				(siteID, customerName) -> tenBridgeService.getCancelReasons(siteID, customerName));
	}

	@PostMapping("/change-reasons")
	public ResponseEntity<Object> getChangeReasons(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "changeReasons",
				(siteID, customerName) -> tenBridgeService.getChangeReasons(siteID, customerName));
	}

	@PostMapping("/referral-sources")
	public ResponseEntity<Object> getReferralSources(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "referralSources",
				(siteID, customerName) -> tenBridgeService.getReferralSources(siteID, customerName));
	}

	@PostMapping("/patient-search")
	public ResponseEntity<Object> getPatient(@RequestBody Map<String, String> request) {

		try {
			// Validate required fields in PatientRequest

			if (request.get("siteID") == null || request.get("siteID").isEmpty() || request.get("customerName") == null
					|| request.get("customerName").isEmpty() || request.get("last") == null
					|| request.get("last").isEmpty() || request.get("dob") == null || request.get("dob").isEmpty()
					|| request.get("transactionId") == null || request.get("transactionId").isEmpty()) {
				return new ResponseEntity<>("Invalid request: required fields missing ", HttpStatus.BAD_REQUEST);
			}

			// Extract values after validation
			String siteID = request.get("siteID");
			String customerName = request.get("customerName");
			String firstName = request.get("first");
			String lastName = request.get("last");
			String dateOfBirth = parseDate(request.get("dob"));
			String tid = request.get("transactionId");
			String patientProfileId = request.get("patientProfileId");
			String patientNumber = request.get("patientNumber");

			if (request.get("patientProfileId") != null // existing patient
					&& !request.get("patientProfileId").isEmpty()) {
				patientProfileId = EncryptionHelper.decrypt(patientProfileId,
						EncryptionKeyConstants.SSO_ENCRYPTION_KEY);

				// patient Number
			} else if (request.get("patientNumber") != null && !request.get("patientNumber").isEmpty()) {
				patientNumber = request.get("patientNumber");

			}

			try {

			} catch (DateTimeParseException e) {
				return new ResponseEntity<Object>("Invalid date format: " + e.getMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Fetch patients
			Object patients = tenBridgeService.getPatients(siteID, customerName, firstName, lastName, dateOfBirth,
					patientProfileId, patientNumber);

			return createSuccessResponseforPatientSearch("patients", patients, tid);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured while retreiving patients",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/slots")
	public ResponseEntity<Object> getSlots(@RequestBody Map<String, String> request) {
		try {
			if (request.get("siteID") == null || request.get("siteID").isEmpty() || request.get("customerName") == null
					|| request.get("customerName").isEmpty() || request.get("appointmentType") == null
					|| request.get("appointmentType").isEmpty() || request.get("startDate") == null
					|| request.get("startDate").isEmpty()) {
				return new ResponseEntity<>("Invalid request: required fields missing ", HttpStatus.BAD_REQUEST);
			}

			// Extract values after validation
			String siteID = request.get("siteID");
			String customerName = request.get("customerName");
			String appointmentType = request.get("appointmentType");
			String startDate = request.get("startDate");
			Object slots = tenBridgeService.getSlots(siteID, customerName, appointmentType, startDate);
			return new ResponseEntity<>(slots, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving slots", e);
			return new ResponseEntity<>("Error occurred while retrieving slots", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/create-patient")
	public ResponseEntity<Object> createPatient(@RequestBody Map<String, String> request) {
		try {
			if (request.get("siteID") == null || request.get("customerName") == null
					|| request.get("first_name") == null || request.get("last_name") == null
					|| request.get("middle_name") == null || request.get("date_of_birth") == null
					|| request.get("gender") == null || request.get("phone") == null
					|| request.get("address_line_1") == null || request.get("address_line_2") == null
					|| request.get("state") == null || request.get("city") == null || request.get("zip") == null
					|| request.get("email") == null) {
				return new ResponseEntity<>("Invalid request: required fields missing ", HttpStatus.BAD_REQUEST);
			}

			Object patientObject = tenBridgeService.createPatient(request.get("siteID"), request.get("customerName"),
					request.get("first_name"), request.get("last_name"), request.get("middle_name"),
					request.get("date_of_birth"), request.get("gender"), request.get("phone"),
					request.get("address_line_1"), request.get("address_line_2"), request.get("state"),
					request.get("city"), request.get("zip"), request.get("email"));
			return new ResponseEntity<>(patientObject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while creating patient", e);
			return new ResponseEntity<>("Error occurred while creating patient", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/appointment-notes")
	public ResponseEntity<Object> getAppointmentNotes(@RequestBody Map<String, String> request) {
		try {
			if (request.get("siteID") == null || request.get("customerName") == null
					|| request.get("appointment_id") == null || request.get("patient_profile_id") == null) {
				return new ResponseEntity<>("Invalid request: required fields missing ", HttpStatus.BAD_REQUEST);
			}
			Object appointmentNotes = tenBridgeService.getAppointmentNotes(request.get("siteID"),
					request.get("customerName"), request.get("appointment_id"), request.get("patient_profile_id"));
			return new ResponseEntity<>(appointmentNotes, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving appointmentNotes", e);
			return new ResponseEntity<>("Error occurred while retrieving appointmentNotes",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/appointments")
	public ResponseEntity<Object> getAppointment( @RequestBody Map<String, String> request) {
		try {
			if (request.get("siteID") == null || request.get("siteID").isEmpty()
					|| request.get("customerName") == null || request.get("customerName").isEmpty()
					|| request.get("patient_id") == null
					|| request.get("patient_id").isEmpty()) {
				return new ResponseEntity<>("Invalid request: required fields missing ", HttpStatus.BAD_REQUEST);
			}
			Object appointment = tenBridgeService.getAppointment(request.get("siteID"),
					request.get("customerName"),
					request.get("patient_id"));
			return new ResponseEntity<>(appointment, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving appointment", e);
			return new ResponseEntity<>("Error occurred while retrieving appointment",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/cancel-appointment")
	public ResponseEntity<Object> cancelAppointment(@RequestBody Map<String, String> request) {
		try {
			if (request.get("siteID") == null || request.get("customerName") == null
					|| request.get("requested_appointment_id") == null) {
				return new ResponseEntity<>("Invalid request: required fields missing ", HttpStatus.BAD_REQUEST);
			}
			Object cancelledAppointment = tenBridgeService.cancelAppointment(request.get("siteID"),
					request.get("customerName"), request.get("requested_appointment_id"));
			return new ResponseEntity<>(cancelledAppointment, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while cancelling appointment", e);
			return new ResponseEntity<>("Error occurred while cancelling appointment",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/schedule-search")
	public ResponseEntity<Object> scheduleSearch(@RequestBody Map<String, String> request) {
		try {
			if (request.get("siteID") == null || request.get("customerName") == null
					|| request.get("patientProfileId") == null || request.get("start_date") == null) {
				return new ResponseEntity<>("Invalid request: required fields missing ", HttpStatus.BAD_REQUEST);
			}
			Object schedule = tenBridgeService.scheduleSearch(request.get("siteID"), request.get("customerName"),
					request.get("patientProfileId"), request.get("start_date"));
			return new ResponseEntity<>(schedule, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving schedule", e);
			return new ResponseEntity<>("Error occurred while retrieving schedule", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<Object> processRequest(Map<String, String> request, List<String> requiredAttributes,
			String entityName, BiFunction<String, String, Object> serviceCall) {
		try {
			// Validate the request
			List<String> missingAttributes = validateAttributes(request, requiredAttributes);
			if (!missingAttributes.isEmpty()) {
				return createBadRequestResponse(missingAttributes);
			}

			// Extract required attributes
			String siteID = request.get("siteID");
			String customerName = request.get("customerName");

			// Perform the service call
			Object result = serviceCall.apply(siteID, customerName);

			// Build the response
			return createSuccessResponse(entityName, result);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving " + entityName, e);
			return createErrorResponse("Error occurred while retrieving " + entityName);
		}
	}

	public List<String> validateAttributes(Map<String, String> request, List<String> requiredAttributes) {
		return requiredAttributes.stream()
				.filter(attr -> !request.containsKey(attr) || request.get(attr) == null || request.get(attr).isEmpty())
				.toList();
	}

	private ResponseEntity<Object> createBadRequestResponse(List<String> missingAttributes) {
		String errorMessage = "Missing required attributes: " + String.join(", ", missingAttributes);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", errorMessage));
	}

	private ResponseEntity<Object> createSuccessResponse(String entityName, Object result) {
		Map<String, Object> responseBody = new HashMap<>();
		Map<String, Object> body = new HashMap<>();
		body.put(entityName, result);
		responseBody.put("body", body);
		return ResponseEntity.ok(responseBody);
	}

	private ResponseEntity<Object> createSuccessResponseforPatientSearch(String entityName, Object result, String tid) {
		Map<String, Object> responseBody = new HashMap<>();
		Map<String, Object> body = new HashMap<>();
		body.put(entityName, result);
		// Create the header map
		Map<String, Object> header = new HashMap<>();
		header.put("tid", tid);
		header.put("status", "success");
		responseBody.put("body", body);
		responseBody.put("header", header);
		return ResponseEntity.ok(responseBody);
	}

	private ResponseEntity<Object> createErrorResponse(String errorMessage) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", errorMessage));
	}

	private String parseDate(String dateString) {
		String parsedDate = null;
		if (dateString != null && dateString.length() > 7 && dateString.indexOf("null") == -1
				&& dateString.indexOf("1914") == -1) {
			// need to convert to yyyy-MM-dd format
			String year, day, month;
			year = dateString.substring(dateString.lastIndexOf("-") + 1, dateString.length());
			month = dateString.substring(0, dateString.indexOf("-"));
			day = dateString.substring(dateString.indexOf("-") + 1, dateString.lastIndexOf("-"));

			if (day.length() <= 1) {
				day = "0" + day;
			}
			if (month.length() <= 1) {
				month = "0" + month;
			}

			parsedDate = year + "-" + month + "-" + day;
		}
		return parsedDate;
	}
}
