package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateBoardTest extends BaseTest {

    @Test
    public void createBoardWithRequiresFieldsTest() {
        String boardName = "board " + UUID.randomUUID();
        String boardId = "69eb592c5a709fd9c3edcf99";

        getRequestSpecification()
                .queryParam("name", boardName)
                .when()
                .post("/boards")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("name", equalTo(boardName));

    }

    @Test
    public void createBoardWithDescription() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        String boardName = "board " + UUID.randomUUID();
        RestAssured.baseURI = "https://api.trello.com/1";
        String description = "Opis mojego boarda";

        given()
                .contentType(ContentType.JSON)
                .queryParam("name", boardName)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .queryParam("desc", description)
                .when()
                .post("/boards")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("name", equalTo(boardName))
                .body("desc", equalTo(description));
    }

    @Test
    public void createBoardWithoutRequireField() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";

        given()
                .contentType(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .when()
                .post("/boards")
                .then()
                .log()
                .all()
                .statusCode(400)
                .body("message", equalTo("invalid value for name"));

    }

    @Description("If prefs_background is invalid set default value blue")
    @Test
    public void shouldSetDefaultBlueWhenPrefsBackgroundIsInvalid() {
        String apikey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        String boardName = "board " + UUID.randomUUID();
        String prefsBackground = "invalid_" + UUID.randomUUID();
        RestAssured.baseURI = "https://api.trello.com/1";

        getRequestSpecification()
                .queryParam("key", apikey)
                .queryParam("token", apiToken)
                .queryParam("name", boardName)
                .queryParam("prefs_background", prefsBackground)
                .when()
                .post("/boards")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .body("name", equalTo(boardName))
                .body("prefs.background", equalTo("blue"));

    }


    @Description("If prefs_background is null set default value blue")
    @Test
    public void shouldSetDefaultBlueWhenPrefsBackgroundIsNull() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        String boardName = "board " + UUID.randomUUID();
        String prefsBackground = null;
        RestAssured.baseURI = "https://api.trello.com/1";

        given()
                .contentType(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .queryParam("name", boardName)
                .queryParam("prefs_background", prefsBackground)
                .when()
                .post("/boards")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .body("name", equalTo(boardName))
                .body("prefs.background", equalTo("blue"));
    }

    @Test
    public void shouldSetValidBackgroundFieldLoopVersion() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        List<String> validColors = List.of("blue", "orange", "green", "red", "purple", "pink", "lime", "sky", "grey");
        RestAssured.baseURI = "https://api.trello.com/1";

        for (String color : validColors) {
            String boardName = "board-" + color + "-" + UUID.randomUUID();
            given()
                    .contentType(ContentType.JSON)
                    .queryParam("key", apiKey)
                    .queryParam("token", apiToken)
                    .queryParam("name", boardName)
                    .queryParam("prefs_background", color)
                    .when()
                    .post("/boards")
                    .then()
                    .log()
                    .ifValidationFails()
                    .statusCode(200)
                    .body("name", equalTo(boardName))
                    .body("prefs.background", equalTo(color));
        }
    }

    @DataProvider(name = "colors")
    public Object[][] colorsProvider() {
        return new Object[][] {
            {"blue"},
            {"orange"},
            {"green"},
            {"red"},
            {"purple"},
            {"pink"},
            {"lime"},
            {"sky"},
            {"grey"}
        };
    }

    @Test(dataProvider = "colors")
    public void shouldSetValidBackgroundFieldDataProviderVersion(String color) {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";
        String boardName = "board-" + color + "-" + UUID.randomUUID();

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("key", apiKey)
                    .queryParam("token", apiToken)
                    .queryParam("name", boardName)
                    .queryParam("prefs_background", color)
                    .when()
                    .post("/boards")
                    .then()
                    .log()
                    .ifValidationFails()
                    .statusCode(200)
                    .body("name", equalTo(boardName))
                    .body("prefs.background", equalTo(color));
    }
}
