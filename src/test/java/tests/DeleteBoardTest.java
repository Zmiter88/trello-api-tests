package tests;

import dto.CreateBoardRequest;
import dto.CreateBoardResponse;
import dto.DeleteBoardResponse;
import helper.BoardCleanupService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DeleteBoardTest {

    @Test
    public void deleteBoardHappyPath() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";
        String boardName = "board" + UUID.randomUUID();

        CreateBoardResponse createBoardResponse =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .queryParam("name", boardName)
                        .when()
                        .post("/boards")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .as(CreateBoardResponse.class);

        assertThat(createBoardResponse.getName()).isEqualTo(boardName);

        DeleteBoardResponse deleteBoardResponse =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .pathParam("id", createBoardResponse.getId())
                        .when()
                        .delete("/boards/{id}")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .as(DeleteBoardResponse.class);

        assertThat(deleteBoardResponse.getValue()).isNull();
    }

    @Test
    public void deleteBoardWithInvalidId() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";
        String id = "invalidId_" + UUID.randomUUID();

                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .pathParam("id", id)
                        .when()
                        .delete("/boards/{id}")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(400)
                        .body(equalTo("invalid id"));
    }

    @Test
    public void deleteBoardAlreadyDeleted() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";
        String boardName = "board" + UUID.randomUUID();

        CreateBoardResponse createBoardResponse =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .queryParam("name", boardName)
                        .when()
                        .post("/boards")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .as(CreateBoardResponse.class);

        assertThat(createBoardResponse.getName()).isEqualTo(boardName);

        DeleteBoardResponse deleteBoardResponse =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .pathParam("id", createBoardResponse.getId())
                        .when()
                        .delete("/boards/{id}")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .as(DeleteBoardResponse.class);

        assertThat(deleteBoardResponse.getValue()).isNull();

        given()
                .contentType(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .pathParam("id", createBoardResponse.getId())
                .when()
                .delete("/boards/{id}")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(404)
                .body(equalTo("The requested resource was not found."));
    }
    @Test
    public void deleteAllBoardsTest() {
        BoardCleanupService cleanupService = new BoardCleanupService();
        cleanupService.deleteAllBoards();
        List<String> remainingBoards = cleanupService.getAllBoards();
        assertThat(remainingBoards.isEmpty());
    }
}
