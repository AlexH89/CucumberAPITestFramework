package utils;

import com.github.javafaker.Faker;
import data.Category;
import data.Pet;
import data.Tags;
import gherkin.deps.com.google.gson.Gson;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class DataUtil {

    private static final Gson gson = new Gson();
    private static final Faker faker = new Faker();

    public static Pet createDefaultPet() {
        Category category = new Category(0, faker.book().genre());
        Tags tag = new Tags(0, faker.book().author());
        List<String> urlList = asList(faker.internet().url());
        BigInteger id = new BigInteger(20, new Random());
        return new Pet(id, category, faker.cat().name(), urlList, asList(tag), "available");
    }

    public static String createPetJsonWithEmptyParameterValues() {
        return "{ \"id\": \"\",\"category\": { \"id\": \"\", \"name\": \"\" },\"name\": \"\",\"photoUrls\": [  ]," +
                "\"tags\": [  ],\"status\": \"\"}";
    }

    public static Pet createPetWithOnlyRequiredParameterValues() {
        List<String> urlList = asList(faker.internet().url());
        return new Pet(null, null, faker.cat().name(), urlList, null, null);
    }

    public static String replaceValueInPetObjectAndReturnJSON(final Pet pet, final String key, final Object value) {
        JSONObject jsonObject = removeKeyFromJSONAndReturnJSONObject(pet, asList(key));
        jsonObject.put(key, value);
        return gson.toJson(jsonObject);
    }

    public static String removeKeysInPetObject(final Pet pet, final List<String> keys) {
        JSONObject jsonObject = removeKeyFromJSONAndReturnJSONObject(pet, keys);
        return gson.toJson(jsonObject);
    }

    private static JSONObject removeKeyFromJSONAndReturnJSONObject(final Pet pet, final List<String> keys) {
        String json = gson.toJson(pet);
        JSONObject jsonObject = new JSONObject(json);
        for (String key : keys) {
            jsonObject.remove(key);
        }
        return jsonObject;
    }
}
