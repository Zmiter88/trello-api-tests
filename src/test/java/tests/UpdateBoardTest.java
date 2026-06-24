package tests;

import dto.*;
import factory.CreateBoardRequestFactory;
import factory.UpdateBoardRequestFactory;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import service.BaseService;
import service.BoardsService;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Trello API")
@Feature("Boards")
public class UpdateBoardTest extends BaseTest {

    BoardsService boardsService = new BoardsService();

    @Story("Update board")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that board was updated")
    @Test
    public void shouldUpdateBoard() {

        // Tworzenie boarda
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();

        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse createBoardResponse = response.as(CreateBoardResponse.class);

        // Update boarda
        UpdateBoardRequest updateBoardRequest = UpdateBoardRequestFactory.defaultUpdate();

        Response updatedResponse = boardsService.updateBoard(createBoardResponse.getId(), updateBoardRequest);
        assertThat(updatedResponse.getStatusCode()).isEqualTo(200);
        UpdateBoardResponse updateDto = updatedResponse.as(UpdateBoardResponse.class);
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
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();

        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse createBoardResponse = response.as(CreateBoardResponse.class);

        // Update boarda
        UpdateBoardRequest updateBoardRequest = UpdateBoardRequestFactory.withName(boardName);

        Response updatedResponse = boardsService.updateBoard(createBoardResponse.getId(), updateBoardRequest);
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
}
