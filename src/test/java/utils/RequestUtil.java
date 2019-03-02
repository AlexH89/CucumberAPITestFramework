package utils;

import data.Pet;
import gherkin.deps.com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RequestUtil {

    private static final String PET_STORE_URL = "https://petstore.swagger.io/v2/";
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    private static HttpResponse executePostRequest(final HttpPost post, final String parameters) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        post.setHeader("cache-control", "no-cache");

        if (parameters != null) {
            HttpEntity entity = new ByteArrayEntity(parameters.getBytes("UTF-8"));
            post.setEntity(entity);
        }

        LOGGER.info("Making post request with parameters: {}", parameters);
        return client.execute(post);
    }

    public static HttpResponse performPetPostRequest(final Pet parameters) throws IOException {
        HttpPost post = new HttpPost(PET_STORE_URL + "pet");
        return executePostRequest(post, new Gson().toJson(parameters));
    }

    public static HttpResponse performPetPostRequestWithJsonString(final String json) throws IOException {
        HttpPost post = new HttpPost(PET_STORE_URL + "pet");
        return executePostRequest(post, json);
    }
}
