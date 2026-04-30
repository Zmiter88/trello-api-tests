package helper;

import dto.DeleteBoardResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class BoardCleanupService {

    String apiKey = System.getenv("TRELLO_KEY");
    String apiToken = System.getenv("TRELLO_TOKEN");

    public BoardCleanupService() {
        RestAssured.baseURI = "https://api.trello.com/1";
    }

    public void deleteAllBoards() {

        List<String> boardIds =

        given()
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .when()
                .get("/member/me/boards")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id");

        for (String boardId : boardIds) {
            given()
                    .queryParam("key", apiKey)
                    .queryParam("token", apiToken)
                    .pathParam("id", boardId)
                    .when()
                    .delete("/boards/{id}")
                    .then()
                    .statusCode(anyOf(is(200), is(404)));
        }
    }

    public List<String> getAllBoards() {
        return given()
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .when()
                .get("/member/me/boards")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id");
    }
}
