package helper;

import dto.*;
import factory.CreateBoardRequestFactory;
import io.restassured.response.Response;
import service.BoardsService;

import static org.assertj.core.api.Assertions.assertThat;


public class BoardHelper {

    private final BoardsService boardsService = new BoardsService();

    public CreateBoardResponse createBoardSuccessfully() {
        CreateBoardRequest request = CreateBoardRequestFactory.defaultBoard();
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse createBoardResponse = response.as(CreateBoardResponse.class);
        return createBoardResponse;
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

    public void cleanUpBoard(String boardId) {

        Response response = boardsService.deleteBoard(boardId);
        if (response.statusCode() != 200 && response.statusCode() != 404) {
            throw new RuntimeException("Cleanup failed");
        }
    }
}
