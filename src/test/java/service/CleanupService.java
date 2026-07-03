package service;

import io.restassured.response.Response;

public class CleanupService {

    private final BoardsService boardsService = new BoardsService();

    public void cleanUpBoard(String boardId) {

        Response response = boardsService.deleteBoard(boardId);
        if (response.statusCode() != 200 && response.statusCode() != 404) {
            throw new RuntimeException("Cleanup failed");
        }
    }
}
