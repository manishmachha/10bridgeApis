package com.ps.tenbridge.datahub.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClientResponseException;

import com.veradigm.ps.tenbridge.client.ApiClient;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;
import com.veradigm.ps.tenbridge.client.models.SinglePractitioner200Response;
import com.veradigm.ps.tenbridge.client.models.SinglePractitionerRequest;
import com.veradigm.ps.tenbridge.client.models.SinglePractitionerRequestData;

public class SinglePractitionersUtil {
	private ApiClient apiClient;

	public SinglePractitionersUtil() {
		this(new ApiClient());
	}

	@Autowired
	public SinglePractitionersUtil(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	private ResponseSpec singlePractitionerRequestCreation(SinglePractitionerRequest singlePractitionerRequest)
			throws RestClientResponseException {
		Object postBody = singlePractitionerRequest;
		// create path and map variables
		final Map<String, Object> pathParams = new HashMap<>();

		final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		final HttpHeaders headerParams = new HttpHeaders();
		final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
		final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

		final String[] localVarAccepts = { "application/json" };
		final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		final String[] localVarContentTypes = { "application/json" };
		final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

		String[] localVarAuthNames = new String[] { "OAuth2" };

		ParameterizedTypeReference<SinglePractitioner200Response> localVarReturnType = new ParameterizedTypeReference<>() {
		};
		return apiClient.invokeAPI("/Opargo/dev/practitioner", HttpMethod.POST, pathParams, queryParams, postBody,
				headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames,
				localVarReturnType);
	}

	public SinglePractitioner200Response singlePractitioner(SinglePractitionerRequest singlePractitionerRequest)
			throws RestClientResponseException {
		ParameterizedTypeReference<SinglePractitioner200Response> localVarReturnType = new ParameterizedTypeReference<>() {
		};
		return singlePractitionerRequestCreation(singlePractitionerRequest).body(localVarReturnType);
	}

	public List<SinglePractitioner200Response> singlePractitioners(String siteID, String customerName,
			ArrayList<String> PractitionerIds) throws RestClientResponseException {
		// Create a list to hold the responses
		List<SinglePractitioner200Response> responses = new ArrayList<>();

		// Loop through each practitioner ID and fetch the single practitioner response
		for (String practitionerId : PractitionerIds) {
			SinglePractitionerRequest request = createSinglePractitionerRequest(siteID, customerName, practitionerId);
			ParameterizedTypeReference<SinglePractitioner200Response> responseType = new ParameterizedTypeReference<>() {
			};
			SinglePractitioner200Response response = singlePractitionerRequestCreation(request).body(responseType);
			responses.add(response);
		}

		// Return the list of responses
		return responses;
	}

	public ResponseEntity<SinglePractitioner200Response> singlePractitionerWithHttpInfo(
			SinglePractitionerRequest singlePractitionerRequest) throws RestClientResponseException {
		ParameterizedTypeReference<SinglePractitioner200Response> localVarReturnType = new ParameterizedTypeReference<>() {
		};
		return singlePractitionerRequestCreation(singlePractitionerRequest).toEntity(localVarReturnType);
	}

	public ResponseSpec singlePractitionerWithResponseSpec(SinglePractitionerRequest singlePractitionerRequest)
			throws RestClientResponseException {
		return singlePractitionerRequestCreation(singlePractitionerRequest);
	}

	protected SinglePractitionerRequest createSinglePractitionerRequest(String siteID, String customerName,
			String practitionerId) {
		RequestMetaData meta = new RequestMetaData();
		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		SinglePractitionerRequestData practitionerRequestData = new SinglePractitionerRequestData();
		practitionerRequestData.setPractitionerId(practitionerId);
		SinglePractitionerRequest practitionerRequest = new SinglePractitionerRequest();
		practitionerRequest.setMeta(meta);
		practitionerRequest.setData(practitionerRequestData);
		return practitionerRequest;
	}
}
