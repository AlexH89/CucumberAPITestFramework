Feature: Testing the Pet Post request with invalid parameters
Users shouldn't be able to add a new pet to the store when invalid parameters are sent

Scenario: Try and fail to add a new pet to the store by making a request with invalid parameters
    When users send a pet post request with invalid parameters
    Then the server should return a "405" error status and a correct "MULTIPLE_INVALID_PARAMETERS" error message

Scenario: Try and fail to add a new pet to the store by making a request without the required parameters
    When users send a pet post request without the required parameters
    Then the server should return a "405" error status and a correct "INVALID_PARAMETERS" error message

Scenario: Try and fail to add a new pet to the store by making a request without a body
    When users send a pet post request without a body
    Then the server should return a "500" error status and a correct "NO_BODY" error message

Scenario: Try and fail to add a new pet to the store by making a request with an existing combination of id and name
    When users send a pet post request with an existing id name combination
    Then the server should return a "409" error status and a correct "DUPLICATE" error message