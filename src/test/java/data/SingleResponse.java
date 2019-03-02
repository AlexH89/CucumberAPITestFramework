package data;

import org.apache.http.HttpResponse;

public class SingleResponse implements Response {

    private final HttpResponse response;
    private Pet pet;
    private String petJSON;

    public SingleResponse(final Pet pet, final HttpResponse response) {
        this.pet = pet;
        this.response = response;
    }

    public SingleResponse(final String petJSON, final HttpResponse response) {
        this.petJSON = petJSON;
        this.response = response;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public Pet getPet() {
        return pet;
    }

    public String getPetJSON() {
        return petJSON;
    }
}
