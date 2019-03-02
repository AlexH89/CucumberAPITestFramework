Feature: Testing the Pet Post request
Users should be able to add a new pet to the store

Scenario: Add a new pet to the store
    When users send a pet post request with the correct parameters
    Then the server should return a success status and the correct "DEFAULT" response message