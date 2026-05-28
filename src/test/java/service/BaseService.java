package service;

import config.ConfigProperties;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseService {

    protected static RequestSpecification getRequestSpecification() {
        return given()
                .baseUri(ConfigProperties.BASE_URI)
                .contentType(ContentType.JSON)
                .queryParam("key", ConfigProperties.API_KEY)
                .queryParam("token", ConfigProperties.API_TOKEN);
    }
}
