package helper;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class ResponseValidator {

    public static ValidatableResponse validate(Response response) {
        return response.then()
                .log()
                .ifValidationFails();
    }
}
