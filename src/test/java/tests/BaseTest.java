package tests;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class BaseTest {


    protected static RequestSpecification getRequestSpecification() {
        return given()
                .baseUri(ConfigProperties.BASE_URI)
                .contentType(ContentType.JSON)
                .queryParam("key", ConfigProperties.API_KEY)
                .queryParam("token", ConfigProperties.API_TOKEN);

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
