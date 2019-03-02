Feature: Testing the Pet Post request with custom parameters
Users should be able to add a new pet to the store while using custom parameters

Scenario: Add a new pet to the store by making a request with id "0" as a parameter
    When users send a pet post request with id parameter value of 0
    Then the server should return a success status and the correct "ZERO_ID" response message

Scenario: Add a new pet to the store by making a request with a body but no parameters
    When users send a pet post request with an empty body
    Then the server should return a success status and the correct "EMPTY_BODY" response message

Scenario: Add a new pet to the store by making a request without any non required parameters
    When users send a pet post request without the non required parameters
    Then the server should return a success status and the correct "NON_REQUIRED" response message

Scenario: Add a new pet to the store by making a request with empty parameters
    When users send a pet post request with empty parameter values
    Then the server should return a success status and the correct "EMPTY_PARAMETERS" response message

Scenario: Add a new pet to the store by making a request with an already existing pet name
    When users send two pet post requests with identical pet names
    Then the server should return a success status and the correct "EXISTING_NAME" response message