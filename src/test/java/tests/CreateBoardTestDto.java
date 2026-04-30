package tests;

import dto.CreateBoardRequest;
import dto.CreateBoardResponse;
import dto.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class CreateBoardTestDto {

    @DataProvider(name = "validNames")
    public Object[][] validNames() {
        return new Object[][]{
                {"a"},
                {"aa"},
                {"board123"},
        };
    }

    @Test(dataProvider = "validNames")
    public void createBoardWithRequiresFields(String name) {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";

        CreateBoardResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .queryParam("name", name)
                        .when()
                        .post("/boards")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .as(CreateBoardResponse.class);

        assertThat(response.getName()).isEqualTo(name);
    }

    @Test
    public void createBoardWithRequiresFields() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";

        String boardName = "board" + UUID.randomUUID();
        CreateBoardRequest request = new CreateBoardRequest();
        request.setName(boardName);

        CreateBoardResponse response =
        given()
                .contentType(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .queryParam("name", request.getName())
                .when()
                .post("/boards")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .as(CreateBoardResponse.class);

       assertThat(response.getName()).isEqualTo(boardName);
    }


    @DataProvider(name = "invalidNames")
    public Object[][] invalidNames() {
        return new Object[][] {
                {""},
                {null},
        };
    }
    @Test(dataProvider = "invalidNames")
    public void createBoardWithInvalidNameShouldFail(String name) {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";


        ErrorResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .queryParam("name", name)
                        .when()
                        .post("/boards")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(400)
                        .extract()
                        .as(ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("invalid value for name");
        assertThat(response.getError()).isEqualTo("ERROR");
    }

    @Test
    public void createBoardWithTooLongNameShouldFail() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";
        String tooLongBoardName = "a".repeat(16385);


                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .queryParam("name", tooLongBoardName)
                        .when()
                        .post("/boards")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(414)
                        .body(containsString("414"));

    }

    @DataProvider(name = "validColors")
    public Object[][] validColors() {
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

    @Test(dataProvider = "validColors")
    public void shouldSetValidBackgroundField(String color) {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";
        String boardName = "board-" + color + "-" + UUID.randomUUID();

        CreateBoardResponse response =
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
                .all()
                .statusCode(200)
                .extract()
                .as(CreateBoardResponse.class);

        assertThat(response.getName()).isEqualTo(boardName);
        assertThat(response.getPrefs().getBackground()).isEqualTo(color);
    }

    @DataProvider(name = "invalidColors")
    public Object[][] invalidColors() {
        return new Object[][] {
                {""},
                {" "},
                {"invalidColor"},
                {null},
        };
    }

    @Test(dataProvider = "invalidColors")
    public void shouldSetDefaultColorBlueWhenPrefsBackgroundIsInvalid(String color) {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";
        String boardName = "board-" + color + "-" + UUID.randomUUID();
        String defaultColor = "blue";

        CreateBoardResponse response =
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
                        .all()
                        .statusCode(200)
                        .extract()
                        .as(CreateBoardResponse.class);

        assertThat(response.getName()).isEqualTo(boardName);
        assertThat(response.getPrefs().getBackground()).isEqualTo(defaultColor);
    }
}
