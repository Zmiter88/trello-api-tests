package tests;

import cleanup.BoardCleanup;
import data.RandomDataGenerator;
import dto.CreateBoardRequest;
import dto.CreateBoardResponse;
import dto.ErrorResponse;
import factory.CreateBoardRequestFactory;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import service.BoardsService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;


import static org.hamcrest.Matchers.containsString;

@Epic("Trello API")
@Feature("Boards")
public class CreateBoardTest extends BaseTest {

    private final BoardsService boardsService = new BoardsService();
    private final BoardCleanup boardCleanup = new BoardCleanup();
    private String boardId;

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (boardId != null) {
            boardCleanup.cleanupBoard(boardId);
            boardId = null;
        }
    }

    @DataProvider(name = "validNames")
    public Object[][] validNames() {
        return new Object[][]{
                {"a"},
                {"aa"},
                {"board123"},
        };
    }

    @Story("Create board with valid name")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that board can be created with valid name")
    @Test(dataProvider = "validNames")
    public void createBoardWithRequiresFieldsRefactor(String name) {

        CreateBoardRequest request = CreateBoardRequestFactory.withName(name);

        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse createBoardResponse = response.as(CreateBoardResponse.class);
        boardId = createBoardResponse.getId();
        assertThat(createBoardResponse.getName()).isEqualTo(name);
    }

    @DataProvider(name = "invalidNames")
    public Object[][] invalidNames() {
        return new Object[][]{
                {""},
                {null},
        };
    }

    @Story("Create board with invalid name")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that board cannot be created with invalid name")
    @Test(dataProvider = "invalidNames")
    public void createBoardWithInvalidNameShouldFailRefactor(String name) {

        CreateBoardRequest request = CreateBoardRequestFactory.withName(name);

        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(400);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertThat(errorResponse.getMessage()).isEqualTo("invalid value for name");
        assertThat(errorResponse.getError()).isEqualTo("ERROR");
    }

    @Story("Create board with too long name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that board cannot be created with too long name")
    @Test
    public void createBoardWithTooLongNameShouldFailRefactor() {

        String tooLongBoardName = "a".repeat(16385);
        CreateBoardRequest request = CreateBoardRequestFactory.withName(tooLongBoardName);
        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(414);
        assertThat(response.getBody().asString()).contains("414");
        // albo tak tez mozna
        response.then().body(containsString("414"));
    }

    @DataProvider(name = "validColors")
    public Object[][] validColors() {
        return new Object[][]{
                {"blue"},
                {"orange"},
                {"green"},
                {"red"},
                {"purple"},
                {"pink"},
                {"lime"},
                {"sky"},
                {"grey"}
        };
    }

    @Story("Create board with valid colors")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that board can be created with valid colors")
    @Test(dataProvider = "validColors")
    public void shouldSetValidBackgroundFieldRefactor(String color) {

        String boardName = "board-" + color + "-" + UUID.randomUUID();
        CreateBoardRequest request = CreateBoardRequest.builder()
                .name(boardName)
                .prefsBackground(color)
                .build();

        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse createBoardResponse = response.as(CreateBoardResponse.class);
        boardId = createBoardResponse.getId();
        assertThat(createBoardResponse.getName()).isEqualTo(boardName);
        assertThat(createBoardResponse.getPrefs().getBackground()).isEqualTo(color);
    }

    @DataProvider(name = "invalidColors")
    public Object[][] invalidColors() {
        return new Object[][]{
                {""},
                {" "},
                {"invalidColor"},
                {null},
        };
    }

    @Story("Create board with invalid color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that board cannot be created with invalid colors")
    @Test(dataProvider = "invalidColors")
    public void shouldSetDefaultColorBlueWhenPrefsBackgroundIsInvalidRefactor(String color) {

        String boardName = RandomDataGenerator.boardName();
        String defaultColor = "blue";

        CreateBoardRequest request = CreateBoardRequest.builder()
                .name(boardName)
                .prefsBackground(color)
                .build();

        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse createBoardResponse = response.as(CreateBoardResponse.class);
        assertThat(createBoardResponse.getName()).isEqualTo(boardName);
        assertThat(createBoardResponse.getPrefs().getBackground()).isEqualTo(defaultColor);
    }

}
