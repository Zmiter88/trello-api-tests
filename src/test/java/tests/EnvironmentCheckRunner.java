package tests;

import context.UserContext;
import dto.BoardResponse;
import factory.UserFactory;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import service.BoardsService;

public class EnvironmentCheckRunner extends BaseTest {

    private final BoardsService boardsService = new BoardsService();

    @Test
    public void checkOwnerBoards() {
        UserContext.setCurrentUser(UserFactory.owner());

        Response response = boardsService.getMyBoards();

        BoardResponse[] boards = response.as(BoardResponse[].class);

        System.out.println("Number of boards: " + boards.length);

        for (BoardResponse board : boards) {
            System.out.println(board.getId() + " - " + board.getName());
        }
    }

    @Test
    public void checkMemberBoards() {
        UserContext.setCurrentUser(UserFactory.member());

        Response response = boardsService.getMyBoards();

        BoardResponse[] boards = response.as(BoardResponse[].class);

        System.out.println("Number of boards: " + boards.length);

        for (BoardResponse board : boards) {
            System.out.println(board.getId() + " - " + board.getName());
        }
    }
}