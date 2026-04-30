package tests;

import dto.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateCardTest {

    @Test
    public void createBoardThenCreateListThenCreateCardWithThenDeleteAll() {
        String apiKey = System.getenv("TRELLO_KEY");
        String apiToken = System.getenv("TRELLO_TOKEN");
        RestAssured.baseURI = "https://api.trello.com/1";

        // Create board
        String boardName = "board" + UUID.randomUUID();
        CreateBoardRequest createBoardRequest = new CreateBoardRequest();
        createBoardRequest.setName(boardName);

        CreateBoardResponse createBoardResponse =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .queryParam("name", createBoardRequest.getName())
                        .when()
                        .post("/boards")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .as(CreateBoardResponse.class);

        assertThat(createBoardResponse.getName()).isEqualTo(boardName);

        // Create list
        String listName = "list" + UUID.randomUUID();
        CreateListRequest createListRequest = new CreateListRequest();
        createListRequest.setListName(listName);

        CreateListResponse createListResponse =

        given()
                .contentType(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .queryParam("name", createListRequest.getListName())
                .queryParam("idBoard", createBoardResponse.getId())
                .when()
                .post("/lists")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .as(CreateListResponse.class);

        assertThat(createListResponse.getName()).isEqualTo(listName);

        // Create card
        CreateCardResponse createCardResponse =

                given()
                        .contentType(ContentType.JSON)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .queryParam("idList", createListResponse.getId())
                        .when()
                        .post("/cards")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .as(CreateCardResponse.class);

        // Get card
        CreateCardResponse getCardResponse =

        given()
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .pathParam("id", createCardResponse.getId())
                .when()
                .get("/cards/{id}")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .as(CreateCardResponse.class);

        assertThat(getCardResponse.getIdBoard()).isEqualTo(createBoardResponse.getId());
        assertThat(getCardResponse.getIdList()).isEqualTo(createListResponse.getId());

        // Delete board, list and card

        DeleteBoardResponse deleteBoardResponse =
                given()
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

        //sprawdzenie czy board, card i list rzeczywiscie sie usunały

        given()
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .pathParam("id", createBoardResponse.getId())
                .when()
                .get("boards/{id}")
                .then()
                .statusCode(404);

        given()
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .pathParam("id", createCardResponse.getId())
                .when()
                .get("cards/{id}")
                .then()
                .statusCode(404);

        given()
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .pathParam("id", createListResponse.getId())
                .when()
                .get("lists/{id}")
                .then()
                .statusCode(404);
    }
}
