package tests;

import dto.*;
import factory.CreateBoardRequestFactory;
import factory.CreateCardRequestFactory;
import factory.CreateListsRequestFactory;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import service.BoardsService;
import service.CardsService;
import service.ListsService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Trello API")
@Feature("Cards")
public class CreateCardTest {

    BoardsService boardsService = new BoardsService();
    ListsService listsService = new ListsService();
    CardsService cardsService = new CardsService();

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
