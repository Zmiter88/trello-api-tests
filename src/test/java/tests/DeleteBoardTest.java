package tests;


import cleanup.BoardCleanup;
import dto.CreateBoardResponse;
import dto.DeleteBoardResponse;
import helper.BoardCleanupService;
import helper.BoardHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import service.BoardsService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Epic("Trello API")
@Feature("Boards")
public class DeleteBoardTest extends BaseTest {

    private final BoardsService boardsService = new BoardsService();
    private final BoardHelper boardHelper = new BoardHelper();
    private final BoardCleanup boardCleanup = new BoardCleanup();
    private String boardId;

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (boardId != null) {
            boardCleanup.cleanupBoard(boardId);
            boardId = null;
        }
    }

    @Story("Delete board")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that board was deleted")
    @Test
    public void deleteBoardHappyPathRefactor() {

        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();
        DeleteBoardResponse deleteBoardResponse = boardHelper.deleteBoardSuccessfully(boardId);
        assertThat(deleteBoardResponse.getValue()).isNull();
    }


    @Story("Delete board")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that already deleted board return status code 404")
    @Test
    public void deleteBoardAlreadyDeletedRefactor() {

        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();
        DeleteBoardResponse deleteBoardResponse = boardHelper.deleteBoardSuccessfully(boardId);
        Response deletedBoardResponse = boardsService.deleteBoard(boardId);
        assertThat(deletedBoardResponse.getStatusCode()).isEqualTo(404);
        assertThat(deletedBoardResponse.getBody().asString()).contains("not found.");

    }

    @Test
    public void deleteAllBoardsTest() {
        getRequestSpecification();
        BoardCleanupService cleanupService = new BoardCleanupService();
        cleanupService.deleteAllBoards();
        List<String> remainingBoards = cleanupService.getAllBoards();
        assertThat(remainingBoards.isEmpty()).isTrue();
    }
}

