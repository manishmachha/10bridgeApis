package com.ps.tenbridge.datahub.controller;

import java.lang.reflect.Field;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.tenbridge.datahub.controllerImpl.TenBridgeService_Mod;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequest;
import com.veradigm.ps.tenbridge.client.models.AppointmentSearchRequestData;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequest;
import com.veradigm.ps.tenbridge.client.models.PatientAlertsRequestBody;
import com.veradigm.ps.tenbridge.client.models.PatientCreateRequest;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;
import com.veradigm.ps.tenbridge.client.models.SlotRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TenBridgeController_Mod {
	private static final Logger logger = LoggerFactory.getLogger(TenBridgeController_Mod.class);

	private final TenBridgeService_Mod tenBridgeServicemod;

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
			List<String> requiredFields = List.of("meta", "body", "siteID", "customerName", "body.patientProfileId");
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
	public ResponseEntity<Object> getAppointment(
			@Valid @RequestBody AppointmentSearchRequest appointmentSearchRequest) {
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

	@PostMapping("/slots")
	public ResponseEntity<Object> getSlots(@RequestBody SlotRequest slotRequest) {
		try {
			List<String> requiredFields = List.of("meta", "data", "meta.siteID", "meta.customerName",
					"data.appointment_type", "data.start_date");
			ResponseEntity<Object> validationResponse = validateRequiredFields(slotRequest, requiredFields);
			if (validationResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
				return validationResponse;
			}
			Object slots = tenBridgeServicemod.getSlots(slotRequest);
			return new ResponseEntity<>(slots, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving slots", e);
			return new ResponseEntity<>("Error occurred while retrieving slots", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/create-patient")
	public ResponseEntity<Object> createPatient(@RequestBody PatientCreateRequest patientCreateRequest) {
		try {
			List<String> requiredFields = List.of("meta", "data", "doctor_info", "meta.siteID", "meta.customerName",
					"data.first_name", "data.last_name", "data.middle_name", "data.date_of_birth", "data.gender",
					"data.phone", "data.address_line_1", "data.address_line_2", "data.state", "data.city", "data.zip",
					"data.email", "doctor_info.Practitioner_id", "doctor_info.speciality", "doctor_info.is_physician",
					"doctor_info.is_practice_member", "doctor_info.first_name", "doctor_info.last_name",
					"doctor_info.middle_name", "doctor_info.abbreviation");
			ResponseEntity<Object> validationResponse = validateRequiredFields(patientCreateRequest, requiredFields);
			if (validationResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
				return validationResponse;
			}	
			Object patientObject = tenBridgeServicemod.createPatient(patientCreateRequest);
			return new ResponseEntity<>(patientObject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while creating patient", e);
			e.printStackTrace();
			return new ResponseEntity<>("Error occurred while creating patient", HttpStatus.INTERNAL_SERVER_ERROR);
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

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public ResponseEntity<Object> validateRequiredFields(Object requestBody, List<String> requiredFields) {
		try {
			// Convert object to map for flexible field validation
			Map<String, Object> requestBodyMap = objectMapper.convertValue(requestBody, Map.class);
			// Map<String, Object> requestBodyMap = convertToMap(requestBody);
			System.out.println(requestBodyMap);
			System.out.println("----------------------------------");
			System.out.println(objectMapper.convertValue(requestBody, Map.class));
			List<String> missingFields = new ArrayList<>();

			// Check for missing fields
			for (String field : requiredFields) {
				if (!isFieldPresent(requestBodyMap, field)) {
					missingFields.add(field);
				}
			}

			// Return response based on validation result
			if (!missingFields.isEmpty()) {
				String missingFieldsMessage = String.join(", ", missingFields);
				String errorMessage = "Invalid request - Missing required fields: " + missingFieldsMessage;
				return new ResponseEntity<>(Map.of("message", errorMessage), HttpStatus.BAD_REQUEST);
			}

			return ResponseEntity.ok(Map.of("message", "All required fields are present"));
		} catch (Exception e) {
			logger.error("Error during validation of required fields", e);
			return new ResponseEntity<>("Validation error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean isFieldPresent(Map<String, Object> map, String fieldPath) {
		String[] keys = fieldPath.split("\\.");
		Map<String, Object> currentMap = map;

		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			if (!currentMap.containsKey(key)) {
				return false;
			}
			Object value = currentMap.get(key);
			if (i == keys.length - 1) {
				return value != null;
			}
			if (!(value instanceof Map)) {
				return false;
			}
			currentMap = (Map<String, Object>) value;
		}
		return true;
	}

	public static Map<String, Object> convertToMap(Object object) throws IllegalAccessException {
		if (object == null) {
			return null;
		}

		Map<String, Object> resultMap = new HashMap<>();
		Class<?> clazz = object.getClass();

		while (clazz != null) { // Traverse the class hierarchy
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true); // Make private fields accessible

				// Exclude fields like "JSON_PROPERTY_*"
				if (field.getName().startsWith("JSON_PROPERTY_")) {
					continue;
				}

				Object value = field.get(object);

				// Recursively handle nested objects
				if (value != null && !isPrimitiveOrWrapper(value.getClass()) && !(value instanceof String)) {
					value = convertToMap(value); // Recursive call for nested objects
				}

				resultMap.put(field.getName(), value); // Keep the field name as it is
			}
			clazz = clazz.getSuperclass(); // Move to the superclass
		}

		return resultMap;
	}

	/**
	 * Checks if a class is a primitive or wrapper type.
	 */
	private static boolean isPrimitiveOrWrapper(Class<?> type) {
		return type.isPrimitive() || type == Boolean.class || type == Byte.class || type == Character.class
				|| type == Double.class || type == Float.class || type == Integer.class || type == Long.class
				|| type == Short.class || type == String.class;
	}
}
