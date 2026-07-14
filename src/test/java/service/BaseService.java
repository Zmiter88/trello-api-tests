package service;

import config.ConfigProperties;
import context.UserContext;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import model.User;

import static io.restassured.RestAssured.given;

public abstract class BaseService {

    protected static RequestSpecification getRequestSpecification() {

        User currentUser = UserContext.getCurrentUser();

        if (currentUser == null) {
            throw new IllegalStateException("User is not set in UserContext");
        }

        RequestSpecification request = given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured())
                .baseUri(ConfigProperties.BASE_URI)
                .contentType(ContentType.JSON);

                 if (currentUser.getApiKey() != null) {
                     request.queryParam("key", currentUser.getApiKey());
                 }
                if (currentUser.getApiToken() != null) {
                    request.queryParam("token", currentUser.getApiToken());
                }

                return request;
    }
}
