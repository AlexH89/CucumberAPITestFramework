package stepDefinitions.pet;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import data.*;
import gherkin.deps.com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DataUtil;
import utils.RequestUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PetPostSteps {

    private final Logger LOGGER = LoggerFactory.getLogger(PetPostSteps.class);
    private final Map<ResponseEnum, Response> RESPONSES = new HashMap<ResponseEnum, Response>();
    private final Gson gson = new Gson();

    @When("^users send a pet post request with the correct parameters$")
    public void sendPetPostRequest() throws IOException {
        Pet pet = DataUtil.createDefaultPet();
        HttpResponse response = RequestUtil.performPetPostRequest(pet);
        RESPONSES.put(ResponseEnum.DEFAULT, new SingleResponse(pet, response));
    }

    @When("^users send a pet post request without the non required parameters$")
    public void sendPetPostRequestWithOnlyRequiredParameters() throws IOException {
        Pet pet = DataUtil.createPetWithOnlyRequiredParameterValues();
        HttpResponse response = RequestUtil.performPetPostRequest(pet);
        RESPONSES.put(ResponseEnum.NON_REQUIRED, new SingleResponse(pet, response));
    }

    @When("^users send a pet post request without the required parameters$")
    public void sendPetPostRequestWithoutRequiredParameters() throws IOException {
        String json = DataUtil.removeKeysInPetObject(DataUtil.createDefaultPet(), asList("name", "photoUrls"));
        HttpResponse response = RequestUtil.performPetPostRequestWithJsonString(json);
        RESPONSES.put(ResponseEnum.INVALID_PARAMETERS, new SingleResponse(json, response));
    }

    @When("^users send a pet post request with invalid parameters$")
    public void sendPetPostRequestWithInvalidParameters() throws IOException {
        // TODO: Might want to move these tests down to UT Level. Check if there already first
        // TODO: As these are user errors, they should return 400 error codes. API expects 500. Talk to developers

        List<ResponseWithPet> responses = new ArrayList<ResponseWithPet>();

        List<Object> invalidId = new ArrayList<Object>();
        invalidId.addAll(asList(-1, 12.50, true, null, "abc", "9223372036854775999"));

        List<Object> invalidCategory = new ArrayList<Object>();
        Category categoryWithInvalidValues = new Category(-1, null);
        invalidCategory.addAll(asList(1, "test", null, categoryWithInvalidValues));

        String longString = StringUtils.repeat(" ", 65535);
        List<Object> invalidName = new ArrayList<Object>();
        invalidName.addAll(asList(null, longString));

        List<Object> invalidUrl = new ArrayList<Object>();
        invalidUrl.addAll(asList(null, longString));

        List<Object> invalidTag = new ArrayList<Object>();
        Tags tagWithInvalidValues = new Tags(-1, null);
        invalidTag.addAll(asList(1, "test", null, tagWithInvalidValues));

        List<Object> invalidStatus = new ArrayList<Object>();
        invalidStatus.addAll(asList(1, null, false, "Random"));

        for (Object id : invalidId) {
            performCustomValueRequestAndAddToResponseList(responses, "id", id);
        }

        for (Object category : invalidCategory) {
            performCustomValueRequestAndAddToResponseList(responses, "category", category);
        }

        for (Object name : invalidName) {
            performCustomValueRequestAndAddToResponseList(responses, "name", name);
        }

        for (Object url : invalidUrl) {
            performCustomValueRequestAndAddToResponseList(responses, "photoUrls", url);
        }

        for (Object tag : invalidTag) {
            performCustomValueRequestAndAddToResponseList(responses, "tags", tag);
        }

        for (Object status : invalidStatus) {
            performCustomValueRequestAndAddToResponseList(responses, "status", status);
        }

        RESPONSES.put(ResponseEnum.MULTIPLE_INVALID_PARAMETERS, new MultiResponse(responses));
    }

    @When("^users send a pet post request with id parameter value of 0$")
    public void sendPostRequestWithIdParameterOfZero() throws IOException {
        Pet pet = DataUtil.createDefaultPet();
        pet.setId(new BigInteger("0"));
        HttpResponse response = RequestUtil.performPetPostRequest(pet);
        RESPONSES.put(ResponseEnum.ZERO_ID, new SingleResponse(pet, response));
    }

    @When("^users send a pet post request with an empty body$")
    public void sendPostRequestWithEmptyBody() throws IOException {
        HttpResponse response = RequestUtil.performPetPostRequestWithJsonString("{}");
        RESPONSES.put(ResponseEnum.EMPTY_BODY, new SingleResponse("{}", response));
    }

    @When("^users send a pet post request with empty parameter values$")
    public void sendPostRequestWithEmptyParameterValues() throws IOException {
        String json = DataUtil.createPetJsonWithEmptyParameterValues();
        HttpResponse response = RequestUtil.performPetPostRequestWithJsonString(json);
        RESPONSES.put(ResponseEnum.EMPTY_PARAMETERS, new SingleResponse(json, response));
    }

    @When("^users send two pet post requests with identical pet names$")
    public void sendPostRequestsWithIdenticalPetNames() throws IOException {
        Pet pet = DataUtil.createDefaultPet();
        pet.setName("Grumpy cat");

        HttpResponse response = RequestUtil.performPetPostRequest(pet);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));

        response = RequestUtil.performPetPostRequest(pet);
        RESPONSES.put(ResponseEnum.EXISTING_NAME, new SingleResponse(pet, response));
    }

    @When("^users send a pet post request with an existing id name combination$")
    public void sendPostRequestsWithIdenticalPetIdsAndNames() throws IOException {
        Pet pet = DataUtil.createDefaultPet();
        pet.setName("Grumpy cat");
        pet.setId(new BigInteger("98765"));

        HttpResponse response = RequestUtil.performPetPostRequest(pet);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));

        response = RequestUtil.performPetPostRequest(pet);
        RESPONSES.put(ResponseEnum.DUPLICATE, new SingleResponse(pet, response));
    }

    @When("^users send a pet post request without a body$")
    public void sendPostRequestWithoutBody() throws IOException {
        HttpResponse response = RequestUtil.performPetPostRequestWithJsonString(null);
        RESPONSES.put(ResponseEnum.NO_BODY, new SingleResponse("", response));
    }

    @Then("^the server should return a success status and the correct \"([^\"]*)\" response message$")
    public void assertPetPostResponse(final ResponseEnum key) throws IOException {
        SingleResponse responseObject = (SingleResponse) RESPONSES.get(key);
        Pet originalPet = responseObject.getPet();

        HttpResponse response = responseObject.getResponse();
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));

        String responseBody = new BasicResponseHandler().handleResponse(response);
        Pet returnedPet = gson.fromJson(responseBody, Pet.class);

        switch (key) {
            case ZERO_ID:
                assertThat(returnedPet.getId(), is(not(new BigInteger("0"))));
                break;
            case EMPTY_BODY:
                assertThat(returnedPet.getId(), is(not(new BigInteger("0"))));
                assertThat(returnedPet.getTags().isEmpty(), is(true));
                assertThat(returnedPet.getPhotoUrls().isEmpty(), is(true));
                break;
            case NON_REQUIRED:
                assertThat(returnedPet.getId(), is(not(new BigInteger("0"))));
                assertThat(returnedPet.getName(), equalTo(originalPet.getName()));
                assertThat(returnedPet.getPhotoUrls(), equalTo(originalPet.getPhotoUrls()));
                assertThat(returnedPet.getTags().isEmpty(), is(true));
                break;
            case EMPTY_PARAMETERS:
                Category category = new Category(0, "");
                assertThat(returnedPet.getId(), is(not(new BigInteger("0"))));
                assertThat(returnedPet.getName(), equalTo(""));
                assertThat(returnedPet.getPhotoUrls().isEmpty(), is(true));
                assertThat(returnedPet.getTags().isEmpty(), is(true));
                assertThat(returnedPet.getStatus(), equalTo(""));
                assertThat(returnedPet.getCategory(), samePropertyValuesAs(category));
                break;
            case EXISTING_NAME:
            default:
                assertThat(responseBody, equalTo(gson.toJson(responseObject.getPet())));
                break;
        }
    }

    @Then("^the server should return a \"([^\"]*)\" error status and a correct \"([^\"]*)\" error message$")
    public void assertPetPostErrorResponse(final int errorCode, final ResponseEnum key) throws IOException {
        Response responseObject = RESPONSES.get(key);

        HttpResponse response;
        switch (key) {
            case NO_BODY:
                response = ((SingleResponse) responseObject).getResponse();
                LOGGER.info("Asserting for request without a body");
                assertResponseErrorCodeAndMessage(response, errorCode, "something bad happened");
                break;
            case DUPLICATE:
                response = ((SingleResponse) responseObject).getResponse();
                StatusLine statusLine = response.getStatusLine();
                assertThat(statusLine.getStatusCode(), equalTo(errorCode));
                // todo: error message not specified. Implement later after Swagger is updated.
                break;
            case INVALID_PARAMETERS:
                response = ((SingleResponse) responseObject).getResponse();
                LOGGER.info("Asserting for pet: {}", ((SingleResponse) responseObject).getPetJSON());
                assertResponseErrorCodeAndMessage(response, errorCode, "Invalid input");
                break;
            case MULTIPLE_INVALID_PARAMETERS:
                List<ResponseWithPet> responses = ((MultiResponse) responseObject).getResponse();
                for (ResponseWithPet currentResponse : responses) {
                    HttpResponse httpResponse = currentResponse.getResponse();
                    LOGGER.info("Asserting for pet: {}", currentResponse.getPetJSON());
                    assertResponseErrorCodeAndMessage(httpResponse, errorCode, "Invalid input");
                }
                break;
            default:
                response = ((SingleResponse) responseObject).getResponse();
                LOGGER.info("Asserting for pet: {}", ((SingleResponse) responseObject).getPet().toString());
                assertResponseErrorCodeAndMessage(response, errorCode, "something bad happened");
                break;
        }
    }

    private void assertResponseErrorCodeAndMessage(final HttpResponse response, final int errorCode,
                                                   final String message) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        assertThat(statusLine.getStatusCode(), equalTo(errorCode));

        String responseBody = EntityUtils.toString(response.getEntity());
        ResponseDetails responseDetails = new Gson().fromJson(responseBody, ResponseDetails.class);
        assertThat(responseDetails.getMessage(), equalTo(message));
    }

    private void performCustomValueRequestAndAddToResponseList(final List<ResponseWithPet> responses,
                                                               final String key,
                                                               final Object value) throws IOException {
        String json = DataUtil.replaceValueInPetObjectAndReturnJSON(DataUtil.createDefaultPet(), key, value);
        HttpResponse response = RequestUtil.performPetPostRequestWithJsonString(json);
        responses.add(new ResponseWithPet(json, response));
    }
}
