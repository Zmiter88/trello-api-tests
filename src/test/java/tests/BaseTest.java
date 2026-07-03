package tests;

import config.ConfigProperties;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterMethod;
import service.CleanupService;

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



    protected CleanupService cleanupService = new CleanupService();
    protected String boardId;

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (boardId != null) {
            cleanupService.cleanUpBoard(boardId);
            boardId = null;
        }
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
