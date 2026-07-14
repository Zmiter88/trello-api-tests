package tests;

import config.ConfigProperties;
import context.UserContext;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import model.User;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static RequestSpecification getRequestSpecification() {

        User currentUser = UserContext.getCurrentUser();

        if (currentUser == null) {
            throw new IllegalStateException("User is not set in UserContext");
        }

        return given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured())
                .baseUri(ConfigProperties.BASE_URI)
                .contentType(ContentType.JSON)
                .queryParam("key", currentUser.getApiKey())
                .queryParam("token", currentUser.getApiToken());
    }



    private static final String FILE_PATH = "src/main/resources/config.properties";

    static {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileReader(FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String name : appProps.stringPropertyNames()) {
            String value = appProps.getProperty(name);
            System.setProperty(name, value);
        }
    }
}
