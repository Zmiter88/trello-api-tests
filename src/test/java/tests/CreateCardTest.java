package tests;

import dto.*;
import factory.CreateBoardRequestFactory;
import factory.CreateCardRequestFactory;
import factory.CreateListsRequestFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import service.BoardsService;
import service.CardsService;
import service.ListsService;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateCardTest {

    BoardsService boardsService = new BoardsService();
    ListsService listsService = new ListsService();
    CardsService cardsService = new CardsService();

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

    @Test
    public void createBoardThenCreateListThenCreateCardWithThenDeleteAllRefactor() {

        // Create board
        String boardName = "board" + UUID.randomUUID();
        CreateBoardRequest request = CreateBoardRequestFactory.withName(boardName);
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse createBoardResponse = response.as(CreateBoardResponse.class);
        assertThat(createBoardResponse.getName()).isEqualTo(boardName);

        // Create list
        String listName = "list" + UUID.randomUUID();
        CreateListRequest createListRequest = CreateListsRequestFactory.withName(listName, createBoardResponse.getId());
        Response createListsResponse = listsService.createLists(createListRequest);
        assertThat(createListsResponse.getStatusCode()).isEqualTo(200);
        CreateListResponse createListResponseToAssert = createListsResponse.as(CreateListResponse.class);
        assertThat(createListResponseToAssert.getName()).isEqualTo(listName);

        // Create card

        CreateCardRequest createCardRequest = CreateCardRequestFactory.defaultCard(createListResponseToAssert.getId());
        Response createCardResponse = cardsService.createCards(createCardRequest);
        assertThat(createCardResponse.getStatusCode()).isEqualTo(200);
        CreateCardResponse createCardResponseToAssert = createCardResponse.as(CreateCardResponse.class);
        assertThat(createCardResponseToAssert.getIdList()).isEqualTo(createListResponseToAssert.getId());

        // Get card

        Response getCardResponse = cardsService.getCard(createCardResponseToAssert.getId());
        assertThat(getCardResponse.getStatusCode()).isEqualTo(200);
        CreateCardResponse createCardResponse1 = getCardResponse.as(CreateCardResponse.class);
        assertThat(createCardResponse1.getIdBoard()).isEqualTo(createBoardResponse.getId());
        assertThat(createCardResponse1.getIdList()).isEqualTo(createListResponseToAssert.getId());

        // Delete board, list and card

        Response deleteBoardResponse = boardsService.deleteBoard(createBoardResponse.getId());
        assertThat(deleteBoardResponse.getStatusCode()).isEqualTo(200);
        DeleteBoardResponse deleteBoardResponse1 = deleteBoardResponse.as(DeleteBoardResponse.class);
        assertThat(deleteBoardResponse1.getValue()).isNull();

        //sprawdzenie czy board, card i list rzeczywiscie sie usunały

        Response getBoard = boardsService.getBoard(createBoardResponse.getId());
        assertThat(getBoard.getStatusCode()).isEqualTo(404);

        Response getCard = cardsService.getCard(createCardResponse1.getId());
        assertThat(getCard.getStatusCode()).isEqualTo(404);

        Response getList = listsService.getList(createListResponseToAssert.getId());
        assertThat(getList.getStatusCode()).isEqualTo(404);

    }
}
