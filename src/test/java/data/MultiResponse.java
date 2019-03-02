package data;

import java.util.ArrayList;
import java.util.List;

public class MultiResponse implements Response {

    public MultiResponse() {
    }

    public MultiResponse(final List<ResponseWithPet> responses) {
        this.responses = responses;
    }

    private List<ResponseWithPet> responses = new ArrayList<ResponseWithPet>();

    public List<ResponseWithPet> getResponse() {
        return responses;
    }

    public void addResponse(final ResponseWithPet response) {
        responses.add(response);
    }
}
