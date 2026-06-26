package tests;

import dto.*;
import factory.UpdateBoardRequestFactory;
import helper.BoardHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import service.BoardsService;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Trello API")
@Feature("Boards")
public class UpdateBoardTest extends BaseTest {

    private final BoardsService boardsService = new BoardsService();
    private final BoardHelper boardHelper = new BoardHelper();
    private String boardId;

    @Story("Update board")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that board was updated")
    @Test
    public void shouldUpdateBoard() {

        // Tworzenie boarda
        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();

        // Update boarda
        UpdateBoardRequest updateBoardRequest = UpdateBoardRequestFactory.defaultUpdate();
        UpdateBoardResponse updateDto = boardHelper.updateBoardSuccessfully(board, updateBoardRequest);
        assertThat(updateDto.getName()).isEqualTo(updateBoardRequest.getName());
        assertThat(updateDto.getPrefs().getBackground()).isEqualTo(updateBoardRequest.getPrefsBackground());
    }

    @DataProvider(name = "invalidNames")
    public Object[][] invalidNames() {
        return new Object[][] {
                {""},
                {null},
        };
    }

    @Story("Update board with invalid name")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that board cannot be updated with invalid name")
    @Test(dataProvider = "invalidNames")
    public void shouldNotAllowUpdateBoardWithInvalidName(String boardName) {

        // Tworzenie boarda
        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();

        // Update boarda
        UpdateBoardRequest updateBoardRequest = UpdateBoardRequestFactory.withName(boardName);
        Response updatedResponse = boardsService.updateBoard(board.getId(), updateBoardRequest);
        assertThat(updatedResponse.getStatusCode()).isEqualTo(400);
        ErrorResponse errorDto = updatedResponse.as(ErrorResponse.class);
        assertThat(errorDto.getMessage()).isEqualTo("invalid value for name");
        assertThat(errorDto.getError()).isEqualTo("ERROR");
    }



    @Story("Update board with invalid id")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that board can be updated with invalid id")
    @Test
    public void shouldNotAllowUpdateBoardWithInvalidId() {

        UpdateBoardRequest updateBoardRequest = UpdateBoardRequestFactory.defaultUpdate();

        String invalidId = "invalidId";

        Response updatedResponse = boardsService.updateBoard(invalidId, updateBoardRequest);
        assertThat(updatedResponse.getStatusCode()).isEqualTo(400);
        assertThat(updatedResponse.getBody().asString()).contains("invalid id");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (boardId != null) {
            boardHelper.cleanupBoard(boardId);
            boardId = null;
        }
    }
}
