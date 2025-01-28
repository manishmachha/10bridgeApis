### Scenarios for `getProviders` Method

Feature: Ten Bridge Service Scenarios
  As a client
  I want to retrieve data for all services using the apiClient
  So that I can verify its behavior under different conditions

  Scenario: Providers - Successfully retrieve providers with valid set token
    Given the TenBridgeService is initialized with a valid token For GetProviders
    When I call the getProviders API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of providers
    And each provider should have valid details

  Scenario: Providers - API call to providersApi.providers(meta) fails with a 400 or 500 response
    Given the TenBridgeService is initialized For GetProviders
    When the getProviders API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For GetProviders

  Scenario: Providers - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For GetProviders
    When I call the getProviders API with siteID "621" and customerName "OpargoEpicTest" with invalid Token
    Then the API call should fail with an unauthorized error For GetProviders

  Scenario: Providers - Build response fails
    Given the TenBridgeService is initialized For GetProviders
    When the getProviders API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For GetProviders

  Scenario: Providers - API response returns empty list
    Given the TenBridgeService is initialized For GetProviders
    When the getProviders API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For GetProviders

### Scenarios for `getLocations` Method

  Scenario: Locations - Successfully retrieve locations with valid set token
    Given the TenBridgeService is initialized with a valid token For GetLocations
    When I call the getLocations API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of locations
    And each location should have valid details For GetLocations

  Scenario: Locations - API call to locationsApi.practiceLocation(meta) fails with a 400 or 500 response
    Given the TenBridgeService is initialized For GetLocations
    When the getLocations API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For GetLocations

  Scenario: Locations - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For GetLocations
    When I call the getLocations API with siteID "621" and customerName "OpargoEpicTest" with invalid Token
    Then the API call should fail with an unauthorized error For GetLocations

  Scenario: Locations - Build response fails
    Given the TenBridgeService is initialized For GetLocations
    When the getLocations API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For GetLocations

  Scenario: Locations - API response returns empty list
    Given the TenBridgeService is initialized For GetLocations
    When the getLocations API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For GetLocations

### Scenarios for `getInsurances` Method

  Scenario: Insurances - Successfully retrieve insurances with valid set token
    Given the TenBridgeService is initialized with a valid token For GetInsurances
    When I call the getInsurances API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of insurances
    And each insurance should have valid details For GetInsurances

  Scenario: Insurances - API call to payerGroupApi.payorGroups(meta) fails with a 400 or 500 response
    Given the TenBridgeService is initialized For GetInsurances
    When the getInsurances API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For GetInsurances

  Scenario: Insurances - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For GetInsurances
    When I call the getInsurances API with siteID "621" and customerName "OpargoEpicTest" with invalid Token
    Then the API call should fail with an unauthorized error For GetInsurances

  Scenario: Insurances - Build response fails
    Given the TenBridgeService is initialized For GetInsurances
    When the getInsurances API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For GetInsurances

  Scenario: Insurances - API response returns empty list
    Given the TenBridgeService is initialized For GetInsurances
    When the getInsurances API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For GetInsurances

### Scenarios for `getReferringProviders` Method

  Scenario: ReferringProviders - Successfully retrieve referring providers with valid set token
    Given the TenBridgeService is initialized with a valid token For ReferringProviders
    When I call the getReferringProviders API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of referring providers
    And each referring provider should have valid details For ReferringProviders

  Scenario: ReferringProviders - API call to refferingProviderApi.referringProviders(meta) fails with a 400 or 500 response
    Given the TenBridgeService is initialized For ReferringProviders
    When the getReferringProviders API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For ReferringProviders

  Scenario: ReferringProviders - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For ReferringProviders
    When I call the getReferringProviders API with siteID "621" and customerName "OpargoEpicTest" with invalid Token
    Then the API call should fail with an unauthorized error For ReferringProviders

  Scenario: ReferringProviders - Build response fails
    Given the TenBridgeService is initialized For ReferringProviders
    When the getReferringProviders API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For ReferringProviders

  Scenario: ReferringProviders - API response returns empty list
    Given the TenBridgeService is initialized For ReferringProviders
    When the getReferringProviders API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For ReferringProviders

### Scenarios for `getEthnicities` Method

  Scenario: Ethnicities - Successfully retrieve ethnicities with valid set token
    Given the TenBridgeService is initialized with a valid token For Ethnicities
    When I call the getEthnicities API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of ethnicities
    And each ethnicity should have valid details For Ethnicities

  Scenario: Ethnicities - API call to ethnicityApi.ethnicity(meta) fails with a 400 or 500 response
    Given the TenBridgeService is initialized For Ethnicities
    When the getEthnicities API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For Ethnicities

  Scenario: Ethnicities - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For Ethnicities
    When I call the getEthnicities API with siteID "621" and customerName "OpargoEpicTest" with invalid Token
    Then the API call should fail with an unauthorized error For Ethnicities

  Scenario: Ethnicities - Build response fails
    Given the TenBridgeService is initialized For Ethnicities
    When the getEthnicities API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For Ethnicities

  Scenario: Ethnicities - API response returns empty list
    Given the TenBridgeService is initialized For Ethnicities
    When the getEthnicities API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For Ethnicities

### Scenarios for `getRaces` Method

  Scenario: Races - Successfully retrieve races with valid set token
    Given the TenBridgeService is initialized with a valid token For Races
    When I call the getRaces API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of races
    And each race should have valid details For Races

  Scenario: Races - API call to racesApi.race(meta) fails with a 400 or 500 response
    Given the TenBridgeService is initialized For Races
    When the getRaces API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For Races

  Scenario: Races - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For Races
    When I call the getRaces API with siteID "621" and customerName "OpargoEpicTest" with invalid Token
    Then the API call should fail with an unauthorized error For Races

  Scenario: Races - Build response fails
    Given the TenBridgeService is initialized For Races
    When the getRaces API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For Races

  Scenario: Races - API response returns empty list
    Given the TenBridgeService is initialized For Races
    When the getRaces API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For Races

### Scenarios for `getReferralSources` Method

  Scenario: ReferralSources - Successfully retrieve referral sources with valid set token
    Given the TenBridgeService is initialized with a valid token For ReferralSources
    When I call the getReferralSources API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of ReferralSources
    And each ReferralSource should have valid details

  Scenario: ReferralSources - API call to referralSourcesApi.referralSource(meta) fails with a 400 or 500 response
    Given the TenBridgeService is initialized For ReferralSources
    When the getReferralSources API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For ReferralSources

  Scenario: ReferralSources - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For ReferralSources
    When I call the getReferralSources API with siteID "621" and customerName "OpargoEpicTest" with invalid Token
    Then the API call should fail with an unauthorized error For ReferralSources

  Scenario: ReferralSources - Build response fails
    Given the TenBridgeService is initialized For ReferralSources
    When the getReferralSources API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For ReferralSources

  Scenario: ReferralSources - API response returns empty list
    Given the TenBridgeService is initialized For ReferralSources
    When the getReferralSources API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For ReferralSources

### Scenarios for `getPatientAlerts` Method

  Scenario: PatientAlerts - Successfully retrieve patient alerts with valid set token
    Given the TenBridgeService is initialized with a valid token For PatientAlerts
    When I call the getPatientAlerts API with siteID "621" and customerName "OpargoEpicTest" and patientProfileId "eTplvxRvcd-eT1nEI8BvQRQ3" with valid Token
    Then I should receive patient alerts
    And each alert should have valid details

  Scenario: PatientAlerts - API call to patientAlertsApi.patientAlert(request) fails with a 400 or 500 response
    Given the TenBridgeService is initialized For PatientAlerts
    When the getPatientAlerts API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For PatientAlerts

  Scenario: PatientAlerts - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For PatientAlerts
    When I call the getPatientAlerts API with invalid Token
    Then the API call should fail with an unauthorized error For PatientAlerts

  Scenario: PatientAlerts - Build response fails
    Given the TenBridgeService is initialized For PatientAlerts
    When the getPatientAlerts API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For PatientAlerts

  Scenario: PatientAlerts - API response returns empty list
    Given the TenBridgeService is initialized For PatientAlerts
    When the getPatientAlerts API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For PatientAlerts


### Scenarios for `getGenders` Method

  Scenario: Genders - Successfully retrieve genders with a valid token
    Given the TenBridgeService is initialized with a valid token For Genders
    When I call the getGenders API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of genders
    And each gender should have valid details

  Scenario: Genders - API call fails with a 400 or 500 response
    Given the TenBridgeService is initialized For Genders
    When the getGenders API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For Genders

  Scenario: Genders - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For Genders
    When I call the getGenders API with invalid Token
    Then the API call should fail with an unauthorized error For Genders

  Scenario: Genders - Build response fails
    Given the TenBridgeService is initialized For Genders
    When the getGenders API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For Genders

  Scenario: Genders - API response returns empty list
    Given the TenBridgeService is initialized For Genders
    When the getGenders API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For Genders
    
### Scenarios for `getCpts` Method

  Scenario: Cpts - Successfully retrieve Cpts with a valid token
    Given the TenBridgeService is initialized with a valid token For Cpts
    When I call the getCpts API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of Cpts
    And each cpt should have valid details

  Scenario: Cpts - API call fails with a 400 or 500 response
    Given the TenBridgeService is initialized For Cpts
    When the getCpts API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For Cpts

  Scenario: Cpts - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For Cpts
    When I call the getCpts API with invalid Token
    Then the API call should fail with an unauthorized error For Cpts

  Scenario: Cpts - Build response fails
    Given the TenBridgeService is initialized For Cpts
    When the getCpts API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For Cpts

  Scenario: Cpts - API response returns empty list
    Given the TenBridgeService is initialized For Cpts
    When the getCpts API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For Cpts    
    
### Scenarios for `getCancelReasons` Method

  Scenario: CancelReasons - Successfully retrieve CancelReasons with a valid token
    Given the TenBridgeService is initialized with a valid token For CancelReasons
    When I call the getCancelReasons API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of CancelReasons
    And each CancelReason should have valid details

  Scenario: CancelReasons - API call fails with a 400 or 500 response
    Given the TenBridgeService is initialized For CancelReasons
    When the getCancelReasons API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For CancelReasons

  Scenario: CancelReasons - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For CancelReasons
    When I call the getCancelReasons API with invalid Token
    Then the API call should fail with an unauthorized error For CancelReasons

  Scenario: CancelReasons - Build response fails
    Given the TenBridgeService is initialized For CancelReasons
    When the getCancelReasons API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For CancelReasons

  Scenario: CancelReasons - API response returns empty list
    Given the TenBridgeService is initialized For CancelReasons
    When the getCancelReasons API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For CancelReasons    


### Scenarios for `getChangeReasons` Method

  Scenario: ChangeReasons - Successfully retrieve ChangeReasons with a valid token
    Given the TenBridgeService is initialized with a valid token For ChangeReasons
    When I call the getChangeReasons API with siteID "621" and customerName "OpargoEpicTest" with valid Token
    Then I should receive a list of ChangeReasons
    And each ChangeReason should have valid details

  Scenario: ChangeReasons - API call fails with a 400 or 500 response
    Given the TenBridgeService is initialized For ChangeReasons
    When the getChangeReasons API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For ChangeReasons

  Scenario: ChangeReasons - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For ChangeReasons
    When I call the getChangeReasons API with invalid Token
    Then the API call should fail with an unauthorized error For ChangeReasons

  Scenario: ChangeReasons - Build response fails
    Given the TenBridgeService is initialized For ChangeReasons
    When the getChangeReasons API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For ChangeReasons

  Scenario: ChangeReasons - API response returns empty list
    Given the TenBridgeService is initialized For ChangeReasons
    When the getChangeReasons API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For ChangeReasons    

### Scenarios for `getAppointments` Method

  Scenario: Appointments - Successfully retrieve Appointments with a valid token
    Given the TenBridgeService is initialized with a valid token For Appointments
    When I call the getAppointments API with siteID "621" and customerName "OpargoEpicTest" and patient_id "e63wRTbPfr1p8UW81d8Seiw3" with valid Token
    Then I should receive a list of Appointments
    And each Appointment should have valid details

  Scenario: Appointments - API call fails with a 400 or 500 response
    Given the TenBridgeService is initialized For Appointments
    When the getAppointments API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For Appointments

  Scenario: Appointments - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For Appointments
    When I call the getAppointments API with invalid Token
    Then the API call should fail with an unauthorized error For Appointments

  Scenario: Appointments - Build response fails
    Given the TenBridgeService is initialized For Appointments
    When the getAppointments API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For Appointments

  Scenario: Appointments - API response returns empty list
    Given the TenBridgeService is initialized For Appointments
    When the getAppointments API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For Appointments
    
    
    
### Scenarios for `getPatient` Method

  Scenario: Patients - Successfully retrieve Patients with a valid token
    Given the TenBridgeService is initialized with a valid token For Appointments
    When I call the getPatient API with siteID "621" and customerName "OpargoEpicTest" and first_name "Opargotest" and last_name "Test" and date_of_birth "11-01-1995" with valid Token
    Then I should receive a list of Patients
    And each patient should have valid details

  Scenario: Patients - API call fails with a 400 or 500 response
    Given the TenBridgeService is initialized For Patients
    When the getPatient API is called and the API returns an error status
    Then an appropriate exception or error message should be logged For Patients

  Scenario: Patients - Invalid token causes failure
    Given the TenBridgeService is initialized with an invalid token For Patients
    When I call the getPatient API with invalid Token
    Then the API call should fail with an unauthorized error For Patients

  Scenario: Patients - Build response fails
    Given the TenBridgeService is initialized For Patients
    When the getPatient API receives invalid data for response building
    Then an appropriate exception or error message should be logged at response For Patients

  Scenario: Patients - API response returns empty list
    Given the TenBridgeService is initialized For Patients
    When the getPatient API returns an empty list
    Then an appropriate exception or error message should be logged for empty list For Patients          