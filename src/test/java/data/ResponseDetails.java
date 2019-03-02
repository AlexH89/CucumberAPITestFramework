package data;

public class ResponseDetails {

    private final String code;
    private final String type;
    private final String message;

    public ResponseDetails(final String code, final String type, final String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
