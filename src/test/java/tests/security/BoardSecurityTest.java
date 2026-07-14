package tests.security;

import context.UserContext;
import dto.*;
import factory.CreateBoardRequestFactory;
import factory.UpdateBoardRequestFactory;
import factory.UserFactory;
import helper.BoardHelper;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import service.BoardsService;
import tests.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardSecurityTest extends BaseTest {

    private final BoardsService boardsService = new BoardsService();
    private final BoardHelper boardHelper = new BoardHelper();
    private String boardId;

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        UserContext.clear();
    }

    @Test
    public void shouldReturn401WhenCreateBoardWithInvalidApiKey() {

        UserContext.setCurrentUser(UserFactory.invalidApiKey());
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody().asString()).isEqualTo("invalid key");
    }

    @Test
    public void shouldReturn401WhenCreateBoardWithInvalidApiToken() {

        UserContext.setCurrentUser(UserFactory.invalidApiToken());
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody().asString()).isEqualTo("invalid app token");
    }

    @Test
    public void shouldReturn401WhenCreateBoardWhenApiKeyIsMissing() {

        UserContext.setCurrentUser(UserFactory.missingApiKey());
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody().asString()).isEqualTo("invalid key");
    }

    @Test
    public void shouldReturn401WhenCreateBoardWhenApiTokenIsMissing() {

        UserContext.setCurrentUser(UserFactory.missingApiToken());
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody().asString()).contains("missing scopes");
    }

    @Test
    public void shouldNotAllowUserWithoutBoardAccessToUpdateBoard() {

        UserContext.setCurrentUser(UserFactory.owner());

        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();

        UserContext.setCurrentUser(UserFactory.member());

        UpdateBoardRequest updateBoardRequest = UpdateBoardRequestFactory.defaultUpdate();

        Response updateBoardResponse = boardsService.updateBoard(boardId, updateBoardRequest);
        assertThat(updateBoardResponse.getStatusCode()).isEqualTo(401);
        assertThat(updateBoardResponse.getBody().asString()).contains("unauthorized permission requested");
    }

    @Test
    public void shouldNotAllowUserWithoutBoardAccessToDeleteBoard() {

        UserContext.setCurrentUser(UserFactory.owner());

        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();

        UserContext.setCurrentUser(UserFactory.member());

        Response deleteBoardResponse = boardsService.deleteBoard(boardId);

        assertThat(deleteBoardResponse.getStatusCode()).isEqualTo(401);
        assertThat(deleteBoardResponse.getBody().asString()).contains("unauthorized permission requested");
    }
}
