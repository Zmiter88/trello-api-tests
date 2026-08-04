package tests;


import cleanup.BoardCleanup;
import context.TestContext;
import context.TestContextHolder;
import context.UserContext;
import dto.CreateBoardResponse;
import dto.DeleteBoardResponse;
import factory.UserFactory;
import helper.BoardHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import model.User;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.BoardsService;

import static org.assertj.core.api.Assertions.assertThat;


@Epic("Trello API")
@Feature("Boards")
public class DeleteBoardTest extends BaseTest {

    private final BoardsService boardsService = new BoardsService();
    private final BoardHelper boardHelper = new BoardHelper();
    private final BoardCleanup boardCleanup = new BoardCleanup();

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

    @Story("Delete board")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that board was deleted")
    @Test
    public void deleteBoardHappyPathRefactor() {

        CreateBoardResponse boardResponse = boardHelper.createBoardSuccessfully();
        DeleteBoardResponse deleteBoardResponse = boardHelper.deleteBoardSuccessfully(boardResponse.getId());
        assertThat(deleteBoardResponse.getValue()).isNull();
    }


    @Story("Delete board")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that already deleted board return status code 404")
    @Test
    public void deleteBoardAlreadyDeletedRefactor() {

        CreateBoardResponse boardResponse = boardHelper.createBoardSuccessfully();
        DeleteBoardResponse deleteBoardResponse = boardHelper.deleteBoardSuccessfully(boardResponse.getId());
        Response deletedBoardResponse = boardsService.deleteBoard(boardResponse.getId());
        assertThat(deletedBoardResponse.getStatusCode()).isEqualTo(404);
        assertThat(deletedBoardResponse.getBody().asString()).contains("not found.");

    }
}

