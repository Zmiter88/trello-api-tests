package cleanup;

import io.restassured.response.Response;
import service.BoardsService;

public class BoardCleanup {

    private final BoardsService boardsService = new BoardsService();

    public void cleanupBoard(String boardId) {
            Response response = boardsService.deleteBoard(boardId);
            if (response.statusCode() != 200 && response.statusCode() != 404) {
                throw new RuntimeException("Cleanup failed");
            }
    }
}
