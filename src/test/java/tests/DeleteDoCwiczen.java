package tests;

import dto.CreateBoardResponse;
import dto.DeleteBoardResponse;
import helper.BoardCleanupService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DeleteDoCwiczen extends BaseTest {

    @Test
    public void deleteBoardHappyPath() {

        String boardName = generateBoardName();

        CreateBoardResponse createBoardResponse = createBoard(boardName);

        assertThat(createBoardResponse.getName()).isEqualTo(boardName);

        DeleteBoardResponse deleteBoardResponse = deleteBoard(createBoardResponse.getId());

        assertThat(deleteBoardResponse.getValue()).isNull();
    }

    private static String generateBoardName() {
        String boardName = "board" + UUID.randomUUID();
        return boardName;
    }

    private static DeleteBoardResponse deleteBoard(String boardId) {
        return getRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .delete("/boards/{id}")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .as(DeleteBoardResponse.class);
    }

    private static CreateBoardResponse createBoard(String boardName) {
        return getRequestSpecification()
                .queryParam("name", boardName)
                .when()
                .post("/boards")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .as(CreateBoardResponse.class);
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
        String boardName = generateBoardName();

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

