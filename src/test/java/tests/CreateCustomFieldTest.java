package tests;

import config.Endpoint;
import context.UserContext;
import data.RandomDataGenerator;
import dto.CreateBoardRequest;
import dto.CreateBoardResponse;
import dto.CreateCustomFieldRequest;
import dto.CreateCustomFieldResponse;
import factory.CreateBoardRequestFactory;
import factory.UserFactory;
import helper.BoardHelper;
import io.restassured.response.Response;
import model.CustomFieldType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.BoardsService;


import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateCustomFieldTest extends BaseTest {

    BoardHelper boardHelper = new BoardHelper();
    BoardsService boardsService = new BoardsService();

    @BeforeMethod
    public void setupUser() {
        UserContext.setCurrentUser(UserFactory.owner());
    }

    @Test
    void createCustomFieldWithRequiredFields() {

        // Tworzenie boarda
        CreateBoardRequest boardRequest = CreateBoardRequestFactory.defaultBoard();
        CreateBoardResponse board = boardHelper.createBoardSuccessfully(boardRequest);
        String boardId = board.getId();

        // Tworzenie Custom Field
        CreateCustomFieldRequest customFieldRequest = CreateCustomFieldRequest.builder()
                .name(RandomDataGenerator.customFieldName())
                .idModel(boardId)
                .modelType("board")
                .type(CustomFieldType.CHECKBOX)
                .pos("top")
                .build();

        CreateCustomFieldResponse response =
                getRequestSpecification()
                        .body(customFieldRequest)
                        .when()
                        .post(Endpoint.CUSTOM_FIELDS.getUrl())
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(CreateCustomFieldResponse.class);

        assertThat(response.getIdModel()).isEqualTo(boardId);
    }
}
