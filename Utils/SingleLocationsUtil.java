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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClientResponseException;

import com.veradigm.ps.tenbridge.client.ApiClient;
import com.veradigm.ps.tenbridge.client.models.RequestMetaData;
import com.veradigm.ps.tenbridge.client.models.SingleLocation200Response;
import com.veradigm.ps.tenbridge.client.models.SingleLocationRequest;
import com.veradigm.ps.tenbridge.client.models.SingleLocationRequestData;

public class SingleLocationsUtil {

	private ApiClient apiClient;

	public SingleLocationsUtil() {
		this(new ApiClient());
	}

	@Autowired
	public SingleLocationsUtil(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	private ResponseSpec singleLocationRequestCreation(SingleLocationRequest singleLocationRequest)
			throws RestClientResponseException {
		Object postBody = singleLocationRequest;
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

		ParameterizedTypeReference<SingleLocation200Response> localVarReturnType = new ParameterizedTypeReference<>() {
		};
		return apiClient.invokeAPI("/Opargo/dev/location", HttpMethod.POST, pathParams, queryParams, postBody,
				headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames,
				localVarReturnType);
	}

	public List<SingleLocation200Response> singleLocations(String siteID, String customerName,
			ArrayList<String> locationIds) throws RestClientResponseException {
		// Create a list to hold the responses
		List<SingleLocation200Response> responses = new ArrayList<>();

		// Loop through each location ID and fetch the single location response
		for (String locationId : locationIds) {
			SingleLocationRequest request = createSingleLocationRequest(siteID, customerName, locationId);
			ParameterizedTypeReference<SingleLocation200Response> responseType = new ParameterizedTypeReference<>() {
			};
			SingleLocation200Response response = singleLocationRequestCreation(request).body(responseType);
			responses.add(response);
		}

		// Return the list of responses
		return responses;
	}

	protected SingleLocationRequest createSingleLocationRequest(String siteID, String customerName, String locationId) {
		RequestMetaData meta = new RequestMetaData();
		meta.setSiteID(siteID);
		meta.setCustomerName(customerName);
		SingleLocationRequestData locationRequestData = new SingleLocationRequestData();
		locationRequestData.setLocationId(locationId);
		SingleLocationRequest locationRequest = new SingleLocationRequest();
		locationRequest.setMeta(meta);
		locationRequest.setData(locationRequestData);
		return locationRequest;
	}

}
