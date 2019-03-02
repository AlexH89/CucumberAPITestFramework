package data;

import org.apache.http.HttpResponse;

public class ResponseWithPet {

    private final HttpResponse response;
    private Pet pet;
    private String petJSON;

    public ResponseWithPet(final Pet pet, final HttpResponse response) {
        this.pet = pet;
        this.response = response;
    }

    public ResponseWithPet(final String petJSON, final HttpResponse response) {
        this.petJSON = petJSON;
        this.response = response;
    }

    public Pet getPet() {
        return pet;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public String getPetJSON() {
        return petJSON;
    }
}
