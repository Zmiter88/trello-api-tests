package tests;

import cleanup.BoardCleanup;
import context.TestContext;
import context.TestContextHolder;
import context.UserContext;
import dto.*;
import factory.CreateBoardRequestFactory;
import factory.CreateCardRequestFactory;
import factory.CreateListsRequestFactory;
import factory.UserFactory;
import helper.BoardHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import model.User;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.BoardsService;
import service.CardsService;
import service.ListsService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Trello API")
@Feature("Cards")
public class CreateCardTest extends BaseTest {

    private final BoardsService boardsService = new BoardsService();
    private final ListsService listsService = new ListsService();
    private final CardsService cardsService = new CardsService();
    private final BoardCleanup boardCleanup = new BoardCleanup();
    private final BoardHelper boardHelper = new BoardHelper();

    @BeforeMethod
    public void setupUser() {
        UserContext.setCurrentUser(UserFactory.owner());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        TestContext context = TestContextHolder.getTestContext();
        String boardId = context.getBoardId();
        User boardCreator = context.getBoardCreator();
        if (boardId != null && boardCreator != null) {
            UserContext.setCurrentUser(boardCreator);
            boardCleanup.cleanupBoard(boardId);
        }
        context.setBoardId(null);
        context.setBoardCreator(null);

        UserContext.clear();
        TestContextHolder.clear();
    }

    @Test
    public void createBoardThenCreateListThenCreateCardWithThenDeleteAllRefactor() {


        // Create board
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        CreateBoardResponse boardResponse = boardHelper.createBoardSuccessfully(request);
        assertThat(boardResponse.getName()).isEqualTo(request.getName());

        // Create list
        String listName = "list " + UUID.randomUUID();
        CreateListRequest createListRequest = CreateListsRequestFactory.withName(listName, boardResponse.getId());
        Response createListsResponse = listsService.createLists(createListRequest);
        assertThat(createListsResponse.getStatusCode()).isEqualTo(200);
        CreateListResponse listResponse = createListsResponse.as(CreateListResponse.class);
        assertThat(listResponse.getName()).isEqualTo(listName);

        // Create card

        CreateCardRequest createCardRequest = CreateCardRequestFactory.defaultCard(listResponse.getId());
        Response createCardResponse = cardsService.createCards(createCardRequest);
        assertThat(createCardResponse.getStatusCode()).isEqualTo(200);
        CreateCardResponse cardResponse = createCardResponse.as(CreateCardResponse.class);
        assertThat(cardResponse.getIdList()).isEqualTo(listResponse.getId());

        // Get card

        Response getCardResponse = cardsService.getCard(cardResponse.getId());
        assertThat(getCardResponse.getStatusCode()).isEqualTo(200);
        CreateCardResponse createCardResponse1 = getCardResponse.as(CreateCardResponse.class);
        assertThat(createCardResponse1.getIdBoard()).isEqualTo(boardResponse.getId());
        assertThat(createCardResponse1.getIdList()).isEqualTo(listResponse.getId());

        // Delete board, list and card

        Response deleteBoardResponse = boardsService.deleteBoard(boardResponse.getId());
        assertThat(deleteBoardResponse.getStatusCode()).isEqualTo(200);
        DeleteBoardResponse deleteBoardResponse1 = deleteBoardResponse.as(DeleteBoardResponse.class);
        assertThat(deleteBoardResponse1.getValue()).isNull();

        //sprawdzenie czy board, card i list rzeczywiscie sie usunały

        Response getBoard = boardsService.getBoard(boardResponse.getId());
        assertThat(getBoard.getStatusCode()).isEqualTo(404);

        Response getCard = cardsService.getCard(createCardResponse1.getId());
        assertThat(getCard.getStatusCode()).isEqualTo(404);

        Response getList = listsService.getList(listResponse.getId());
        assertThat(getList.getStatusCode()).isEqualTo(404);
    }
}
