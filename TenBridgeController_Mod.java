package com.ps.tenbridge.datahub.controller;

import java.util.ArrayList;
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
import com.ps.tenbridge.datahub.controllerImpl.TenBridgeService_Mod;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequest;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequestData;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequest;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequestBody;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;

@RestController
@RequestMapping("/api")
public class TenBridgeController_Mod {
	private static final Logger logger = LoggerFactory.getLogger(TenBridgeController_Mod.class);

	private final TenBridgeService_Mod tenBridgeServicemod;
//	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	public TenBridgeController_Mod(TenBridgeService_Mod tenBridgeServicemod) {
		this.tenBridgeServicemod = tenBridgeServicemod;

	}

	@PostMapping("/genders")
	public ResponseEntity<Object> getGenders(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "genders",
				(siteID, customerName) -> tenBridgeServicemod.getGenders(siteID, customerName));
	}

	@PostMapping("/cpts")
	public ResponseEntity<Object> getCptCodes(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "cptCodes",
				(siteID, customerName) -> tenBridgeServicemod.getCptCodes(siteID, customerName));
	}

	@PostMapping("/referral_sources")
	public ResponseEntity<Object> getReferralSources(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "referralSources",
				(siteID, customerName) -> tenBridgeServicemod.getReferralSources(siteID, customerName));
	}

	@PostMapping("/patient-alerts")
	public ResponseEntity<Object> getPatientAlerts(@RequestBody PatientAlertsRequest patientAlertsRequest) {
		try {
			// Validate required fields
			List<String> requiredFields = List.of("meta", "body", "meta.siteID", "meta.customerName",
					"body.patientProfileId");
			ResponseEntity<Object> validationResponse = validateRequiredFields(patientAlertsRequest, requiredFields);

			if (validationResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
				return validationResponse;
			}

			// Process request
			RequestMetaData meta = patientAlertsRequest.getMeta();
			PatientAlertsRequestBody body = patientAlertsRequest.getBody();
			Object patientAlerts = tenBridgeServicemod.getPatientAlerts(meta, body);
			return new ResponseEntity<>(patientAlerts, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving patient alerts", e);
			return new ResponseEntity<>("Error occurred while retrieving patient alerts",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/cancel-reasons")
	public ResponseEntity<Object> getCancelReasons(@RequestBody Map<String, String> request) {
		System.out.println(request.get("siteID"));
		return processRequest(request, List.of("siteID", "customerName"), "cancelReasons",
				(siteID, customerName) -> tenBridgeServicemod.getCancelReasons(siteID, customerName));
	}

	@PostMapping("/change-reasons")
	public ResponseEntity<Object> getChangeReasons(@RequestBody Map<String, String> request) {
		return processRequest(request, List.of("siteID", "customerName"), "changeReasons",
				(siteID, customerName) -> tenBridgeServicemod.getChangeReasons(siteID, customerName));
	}
	
	@PostMapping("/appointments")
	public ResponseEntity<Object> getAppointment(@RequestBody AppointmentSearchRequest appointmentSearchRequest) {
		try {
			List<String> requiredFields = List.of("meta", "data", "meta.siteID", "meta.customerName",
					"data.patient_id");
			ResponseEntity<Object> validationResponse = validateRequiredFields(appointmentSearchRequest,
					requiredFields);
			if (validationResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
				return validationResponse;
			}
			RequestMetaData meta = appointmentSearchRequest.getMeta();
			Object appointment = tenBridgeServicemod.getAppointment(meta, appointmentSearchRequest.getData());
			return new ResponseEntity<>(appointment, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving appointment", e);
			return new ResponseEntity<>("Error occurred while retrieving appointment",
					HttpStatus.INTERNAL_SERVER_ERROR);
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

	private ResponseEntity<Object> createErrorResponse(String errorMessage) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", errorMessage));
	}

	public ResponseEntity<Object> validateRequiredFields(Object requestBody, List<String> requiredFields) {
		try {
			Map<String, Object> requestBodyMap = new HashMap<>();
			if (requestBody instanceof AppointmentSearchRequest) {
				AppointmentSearchRequest request = (AppointmentSearchRequest) requestBody;
				RequestMetaData meta = request.getMeta();
				AppointmentSearchRequestData data = request.getData();
				requestBodyMap.put("meta", meta);
				requestBodyMap.put("data", data);
				if (meta != null) {
					requestBodyMap.put("meta.siteID", meta.getSiteID());
					requestBodyMap.put("meta.customerName", meta.getCustomerName());
				}
				if (data != null) {
					requestBodyMap.put("data.patient_id", data.getPatientId());
				}
			}
			List<String> missingFields = new ArrayList<>();
			for (String field : requiredFields) {
				if (!requestBodyMap.containsKey(field) || requestBodyMap.get(field) == null) {
					missingFields.add(field);
				}
			}
			if (!missingFields.isEmpty()) {
				String missingFieldsMessage = String.join(", ", missingFields);
				String errorMessage = "Invalid request - Missing required fields: " + missingFieldsMessage;
				return new ResponseEntity<>(Map.of("msg", errorMessage), HttpStatus.BAD_REQUEST);
			}
			return ResponseEntity.ok(Map.of("msg", "All required fields are present"));
		} catch (Exception e) {
			logger.error("Error during validation of required fields", e);
			return new ResponseEntity<>("Validation error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
