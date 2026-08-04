package helper;

import context.TestContext;
import context.TestContextHolder;
import context.UserContext;
import dto.*;
import factory.CreateBoardRequestFactory;
import io.restassured.response.Response;
import service.BoardsService;

import static org.assertj.core.api.Assertions.assertThat;


public class BoardHelper {

    private final BoardsService boardsService = new BoardsService();


    public Response createBoard(CreateBoardRequest request) {
        return boardsService.createBoard(request);
    }

    public CreateBoardResponse createBoardSuccessfully() {
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse board = response.as(CreateBoardResponse.class);
        saveBoardContext(board);
        return board;
    }

    public CreateBoardResponse createBoardSuccessfully(CreateBoardRequest request) {
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse board = response.as(CreateBoardResponse.class);
        saveBoardContext(board);
        return board;
    }

    public UpdateBoardResponse updateBoardSuccessfully(CreateBoardResponse board, UpdateBoardRequest updateBoardRequest) {
        Response updatedResponse = boardsService.updateBoard(board.getId(), updateBoardRequest);
        assertThat(updatedResponse.getStatusCode()).isEqualTo(200);
        UpdateBoardResponse updateBoardResponse = updatedResponse.as(UpdateBoardResponse.class);
        return updateBoardResponse;
    }

    public DeleteBoardResponse deleteBoardSuccessfully(String boardId) {
        Response deleteResponse = boardsService.deleteBoard(boardId);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(200);
        DeleteBoardResponse deleteBoardResponse = deleteResponse.as(DeleteBoardResponse.class);
        return deleteBoardResponse;
    }

    private void saveBoardContext(CreateBoardResponse board) {

        TestContext context = TestContextHolder.getTestContext();

        context.setBoardId(board.getId());
        context.setBoardCreator(UserContext.getCurrentUser());
    }
}
