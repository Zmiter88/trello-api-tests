package tests;

import data.RandomDataGenerator;
import dto.CreateBoardRequest;
import dto.CreateBoardResponse;
import dto.ErrorResponse;
import factory.CreateBoardRequestFactory;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import service.BoardsService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;


import static org.hamcrest.Matchers.containsString;

public class CreateBoardTest extends BaseTest {

    BoardsService boardsService = new BoardsService();

    @DataProvider(name = "validNames")
    public Object[][] validNames() {
        return new Object[][]{
                {"a"},
                {"aa"},
                {"board123"},
        };
    }

    @Test(dataProvider = "validNames")
    public void createBoardWithRequiresFieldsMyRefactor(String name) {

        CreateBoardRequest request = CreateBoardRequestFactory.withName(name);

        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(200);
        CreateBoardResponse createBoardResponse = response.as(CreateBoardResponse.class);
        assertThat(createBoardResponse.getName()).isEqualTo(name);
    }


    @DataProvider(name = "invalidNames")
    public Object[][] invalidNames() {
        return new Object[][] {
                {""},
                {null},
        };
    }


    @Test(dataProvider = "invalidNames")
    public void createBoardWithInvalidNameShouldFailRefactor(String name) {

        CreateBoardRequest request = CreateBoardRequestFactory.withName(name);

        Response response = boardsService.createBoard(request);
        assertThat(response.getStatusCode()).isEqualTo(400);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertThat(errorResponse.getMessage()).isEqualTo("invalid value for name");
        assertThat(errorResponse.getError()).isEqualTo("ERROR");
    }

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
        return new Object[][] {
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
        assertThat(createBoardResponse.getName()).isEqualTo(boardName);
        assertThat(createBoardResponse.getPrefs().getBackground()).isEqualTo(color);
    }

    @DataProvider(name = "invalidColors")
    public Object[][] invalidColors() {
        return new Object[][] {
                {""},
                {" "},
                {"invalidColor"},
                {null},
        };
    }

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
