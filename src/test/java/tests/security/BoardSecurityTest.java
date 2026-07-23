package tests.security;

import cleanup.BoardCleanup;
import constants.ErrorMessages;
import context.UserContext;
import dto.*;
import factory.CreateBoardRequestFactory;
import factory.UpdateBoardRequestFactory;
import factory.UserFactory;
import helper.BoardHelper;
import io.restassured.response.Response;
import model.BoardMemberType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import service.BoardsService;
import tests.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardSecurityTest extends BaseTest {

    private final BoardsService boardsService = new BoardsService();
    private final BoardHelper boardHelper = new BoardHelper();
    private final BoardCleanup boardCleanup = new BoardCleanup();
    private String boardId;

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        UserContext.setCurrentUser(UserFactory.owner());
        if (boardId != null) {
            boardCleanup.cleanupBoard(boardId);
            boardId = null;
        }
        UserContext.clear();
    }

    @Test
    public void shouldReturn401WhenCreateBoardWithInvalidApiKey() {

        UserContext.setCurrentUser(UserFactory.invalidApiKey());
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody().asString()).isEqualTo(ErrorMessages.INVALID_KEY);
    }

    @Test
    public void shouldReturn401WhenCreateBoardWithInvalidApiToken() {

        UserContext.setCurrentUser(UserFactory.invalidApiToken());
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody().asString()).isEqualTo(ErrorMessages.INVALID_APP_TOKEN);
    }

    @Test
    public void shouldReturn401WhenCreateBoardWhenApiKeyIsMissing() {

        UserContext.setCurrentUser(UserFactory.missingApiKey());
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody().asString()).isEqualTo(ErrorMessages.INVALID_KEY);
    }

    @Test
    public void shouldReturn401WhenCreateBoardWhenApiTokenIsMissing() {

        UserContext.setCurrentUser(UserFactory.missingApiToken());
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody().asString()).isEqualTo(ErrorMessages.MISSING_SCOPES);
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
        assertThat(updateBoardResponse.getBody().asString()).isEqualTo(ErrorMessages.UNAUTHORIZED_PERMISSION_REQUESTED);
    }

    @Test
    public void shouldNotAllowUserWithoutBoardAccessToDeleteBoard() {

        UserContext.setCurrentUser(UserFactory.owner());

        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();

        UserContext.setCurrentUser(UserFactory.member());

        Response deleteBoardResponse = boardsService.deleteBoard(boardId);

        assertThat(deleteBoardResponse.getStatusCode()).isEqualTo(401);
        assertThat(deleteBoardResponse.getBody().asString()).isEqualTo(ErrorMessages.UNAUTHORIZED_PERMISSION_REQUESTED);
    }

    @Test
    public void shouldAllowMemberAddedAsAdminByOwnerToUpdateBoard() {

        UserContext.setCurrentUser(UserFactory.owner());
        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();
        Response addMemberResponse = boardsService.addMember(boardId, UserFactory.member(), BoardMemberType.ADMIN);
        assertThat(addMemberResponse.getStatusCode()).isEqualTo(200);
        UserContext.setCurrentUser(UserFactory.member());
        UpdateBoardRequest updateBoardRequest = UpdateBoardRequestFactory.defaultUpdate();
        Response updateResponse = boardsService.updateBoard(boardId, updateBoardRequest);
        assertThat(updateResponse.getStatusCode()).isEqualTo(200);
        UpdateBoardResponse updateBoardResponse = updateResponse.as(UpdateBoardResponse.class);
        assertThat(updateBoardResponse.getName()).isEqualTo(updateBoardRequest.getName());
    }

    @Test
    public void shouldNotAllowMemberAddedAsNormalByOwnerToUpdateBoard() {

        UserContext.setCurrentUser(UserFactory.owner());
        CreateBoardResponse board = boardHelper.createBoardSuccessfully();
        boardId = board.getId();
        Response addMemberResponse = boardsService.addMember(boardId, UserFactory.member(), BoardMemberType.NORMAL);
        assertThat(addMemberResponse.getStatusCode()).isEqualTo(200);
        UserContext.setCurrentUser(UserFactory.member());
        UpdateBoardRequest updateBoardRequest = UpdateBoardRequestFactory.defaultUpdate();
        Response updateResponse = boardsService.updateBoard(boardId, updateBoardRequest);
        assertThat(updateResponse.getStatusCode()).isEqualTo(401);
        assertThat(updateResponse.getBody().asString()).isEqualTo(ErrorMessages.UNAUTHORIZED_PERMISSION_REQUESTED);
    }
}
