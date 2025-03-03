Feature: Ten Bridge Controller Scenarios  
  As a client  
  I want to retrieve data for all services through the Ten Bridge Controller API  
  So that I can ensure the controller behaves as expected under various conditions


  Scenario: Successfully retrieve races
    Given a request with valid attributes for races
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests races
    Then the response status should be 200
    And the response body should contain "races"

  Scenario: Missing required attributes in the request for race
    Given a request with missing attributes 
      | key          | value           |
      | customerName | OpargoEpicTest  |
    When the client requests races
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"
    
  Scenario: Error occurred during races call
    Given a request with valid attributes for races
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling races
    When the client requests races
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving races"

  Scenario: Successfully retrieve providers
    Given a request with valid attributes for providers
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests providers
    Then the response status should be 200
    And the response body should contain "providers"

  Scenario: Missing required attributes in the request for providers
    Given a request with missing attributes 
      | customerName |
      | OpargoEpicTest |
    When the client requests providers
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"

  Scenario: Error occurred during providers call
    Given a request with valid attributes for providers
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling providers
    When the client requests providers
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving providers"
    
    Scenario: Successfully retrieve locations
    Given a request with valid attributes for locations
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests locations
    Then the response status should be 200
    And the response body should contain "locations"
    
    
    Scenario: Missing required attributes in the request for locations
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests locations
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"

  Scenario: Error occurred during locations call
    Given a request with valid attributes for locations
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling locations
    When the client requests locations
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving locations"
    
    Scenario: Successfully retrieve ethnicities
    Given a request with valid attributes for ethnicities
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests ethnicities
    Then the response status should be 200
    And the response body should contain "ethnicities"
    
    
    Scenario: Missing required attributes in the request for ethnicities
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests ethnicities
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"

  Scenario: Error occurred during ethnicities call
    Given a request with valid attributes for ethnicities
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling ethnicities
    When the client requests ethnicities
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving ethnicities"
    
    Scenario: Successfully retrieve insuranceCarriers
    Given a request with valid attributes for insuranceCarriers
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests insuranceCarriers
    Then the response status should be 200
    And the response body should contain "insuranceCarriers"
    
    Scenario: Missing required attributes in the request for insuranceCarriers
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests insuranceCarriers
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"

  Scenario: Error occurred during insuranceCarriers call
    Given a request with valid attributes for insuranceCarriers
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling insuranceCarriers
    When the client requests insuranceCarriers
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving insuranceCarriers"
    
    Scenario: Successfully retrieve referringProviders
    Given a request with valid attributes for referringproviders
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests referringproviders
    Then the response status should be 200
    And the response body should contain "referringproviders"

     Scenario: Missing required attributes in the request for referringproviders
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests referringproviders
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"

  Scenario: Error occurred during referringproviders call
    Given a request with valid attributes for referringproviders
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling referringproviders
    When the client requests referringproviders
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving referringproviders"
    

       Scenario: Successfully retrieve patientAlerts
    Given a request with valid attributes for patientAlerts
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
      | patientProfileId | eTplvxRvcd-eT1nEI8BvQRQ3  |
    When the client requests patientAlerts
    Then the response status should be 200
    And the response body should contain "patientAlerts"
 
     Scenario: Missing required attributes in the request for patientAlerts
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests patientAlerts
    Then the response status should be 400
    And the response body should contain "Invalid request: required fields missing"
 
  Scenario: Error occurred during patientAlerts call
    Given a request with valid attributes for patientAlerts
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
      | patientProfileId | eTplvxRvcd-eT1nEI8BvQRQ3  |	
    And the service throws an exception when calling patientAlerts
    When the client requests patientAlerts
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving patient alerts"
    
   Scenario: Successfully retrieve patientSearch
    Given a request with valid attributes for patientSearch
      | key           | value           |
      | siteID        | 621             |
      | customerName  | OpargoEpicTest  |
      | first         | Theodore        |
      | last          | Mychart         |
      | dob           | 07-07-1948      |
      | transactionId | hgmnb           |
    When the client requests patientSearch
    Then the response status should be 200
    And the response body should contain "patients"
 
   Scenario: Missing required attributes in the request for patientSearch
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests patientSearch
    Then the response status should be 400
    And the response body should contain "Invalid request: required fields missing"
 
     
     Scenario: Successfully retrieve changeReasons
    Given a request with valid attributes for changeReasons
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests changeReasons
    Then the response status should be 200
    And the response body should contain "changeReasons"

     Scenario: Missing required attributes in the request for changeReasons
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests changeReasons
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"

  Scenario: Error occurred during changeReasons call
    Given a request with valid attributes for changeReasons
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling changeReasons
    When the client requests changeReasons
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving changeReasons"
    
    Scenario: Successfully retrieve cancelReasons
    Given a request with valid attributes for cancelReasons
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests cancelReasons
    Then the response status should be 200
    And the response body should contain "cancelReasons"

     Scenario: Missing required attributes in the request for cancelReasons
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests cancelReasons
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"

  Scenario: Error occurred during cancelReasons call
    Given a request with valid attributes for cancelReasons
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling cancelReasons
    When the client requests cancelReasons
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving cancelReasons"
    
    Scenario: Successfully retrieve referralSources
    Given a request with valid attributes for referralSources
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    When the client requests referralSources
    Then the response status should be 200
    And the response body should contain "referralSources"

     Scenario: Missing required attributes in the request for referralSources
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests referralSources
    Then the response status should be 400
    And the response body should contain "Missing required attributes: siteID"

  Scenario: Error occurred during referralSources call
    Given a request with valid attributes for referralSources
      | key          | value           |
      | siteID       | 621             |
      | customerName | OpargoEpicTest  |
    And the service throws an exception when calling referralSources
    When the client requests referralSources
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving referralSources"

Scenario: Successfully retrieve appointmentSearch
    Given a request with valid attributes for AppointmentSearch
      | key           | value                           |
      | siteID        | 621                             |
      | customerName  | OpargoEpicTest                  |
      | patient_id    | e63wRTbPfr1p8UW81d8Seiw3        |
    When the client requests AppointmentSearch
    Then the response status should be 200
    And the response body should contain "AppointmentInfoDTO"
 
   Scenario: Missing required attributes in the request for AppointmentSearch
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests AppointmentSearch
    Then the response status should be 400
    And the response body should contain "Invalid request: required fields missing"
 
  Scenario: Error occurred during appointmentSearch call
   Given a request with valid attributes for AppointmentSearch
      | key           | value                           |
      | siteID        | 621                             |
      | customerName  | OpargoEpicTest                  |
      | patient_id    | e63wRTbPfr1p8UW81d8Seiw3        |
    And the service throws an exception when calling AppointmentSearch
    When the client requests AppointmentSearch
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving appointment"

    Scenario: Successfully retrieve BookAppointment
    Given a request with valid attributes for BookAppointment
      | key           | value                           |
      | siteID        | 621                             |
      | customerName  | OpargoEpicTest                  |
      | requestedAppointmentId    | etenCaqETW7InVSZEOPGeUjaVwWzM8rz6W76LYg0ZlJbTEhMPuTBMQ7GdQNF9plBT3        |
      | patientIdentifier    | e63wRTbPfr1p8UW81d8Seiw3        |
    When the client requests BookAppointment
    Then the response status should be 200
    And the response body should contain "AppointmentInfoDTO"

   Scenario: Missing required attributes in the request for BookAppointment
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests BookAppointment
    Then the response status should be 400
    And the response body should contain "Invalid request: required fields missing"

  Scenario: Error occurred during BookAppointment call
   Given a request with valid attributes for BookAppointment
      | key           | value                           |
      | siteID        | 621                             |
      | customerName  | OpargoEpicTest                  |
      | patient_id    | e63wRTbPfr1p8UW81d8Seiw3        |
      | requestedAppointmentId    | etenCaqETW7InVSZEOPGeUjaVwWzM8rz6W76LYg0ZlJbTEhMPuTBMQ7GdQNF9plBT3        |
      | patientIdentifier    | e63wRTbPfr1p8UW81d8Seiw3        |
    And the service throws an exception when calling BookAppointment
    When the client requests BookAppointment
    Then the response status should be 500
    And the response body should contain "Error occurred while creating appointment"
    
    Scenario: Successfully retrieve Slots
    Given a request with valid attributes for Slots
      | key           | value                           |
      | siteID        | 621                             |
      | customerName  | OpargoEpicTest                  |
      | appointmentType  | Office Visit                |
      | startDate    | 2024-12-09T13:00:00Z            |
    When the client requests Slots
    Then the response status should be 200
    And the response body should contain "Slots"

   Scenario: Missing required attributes in the request for Slots
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests Slots
    Then the response status should be 400
    And the response body should contain "Invalid request: required fields missing"

  Scenario: Error occurred during Slots call
   Given a request with valid attributes for Slots
      | key           | value                           |
      | siteID        | 621                             |
      | customerName  | OpargoEpicTest                  |
      | appointmentType  | Office Visit                |
      | startDate    | 2024-12-09T13:00:00Z            |
    And the service throws an exception when calling Slots
    When the client requests Slots
    Then the response status should be 500
    And the response body should contain "Error occurred while retrieving slots"
    
     Scenario: Successfully retrieve CancelAppointment
    Given a request with valid attributes for CancelAppointment
      | key           | value                           |
      | siteID        | 621                             |
      | customerName  | OpargoEpicTest                  |
      | requested_appointment_id     | eQx67kmlpYhPsd3Bbsln71ZCuwmKKLEqt84sKTWWZ4mk3        |
    When the client requests CancelAppointment
    Then the response status should be 200
    And the response body should contain "CancelAppointmentResponse"

   Scenario: Missing required attributes in the request for CancelAppointment
    Given a request with missing attributes
      | customerName |
      | OpargoEpicTest |
    When the client requests CancelAppointment
    Then the response status should be 400
    And the response body should contain "Invalid request: required fields missing"

  Scenario: Error occurred during CancelAppointment call
   Given a request with valid attributes for CancelAppointment
      | key           | value                           |
      | siteID        | 621                             |
      | customerName  | OpargoEpicTest                  |
      | requested_appointment_id     | eQx67kmlpYhPsd3Bbsln71ZCuwmKKLEqt84sKTWWZ4mk3        |
    And the service throws an exception when calling CancelAppointment
    When the client requests CancelAppointment
    Then the response status should be 500
    And the response body should contain "Error occurred while cancelling appointment"