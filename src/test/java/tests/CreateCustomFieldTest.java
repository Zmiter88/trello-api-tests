package tests;

import cleanup.BoardCleanup;
import context.TestContext;
import context.TestContextHolder;
import context.UserContext;
import dto.*;
import factory.CreateBoardRequestFactory;
import factory.CreateCustomFieldFactory;
import factory.UserFactory;
import helper.BoardHelper;
import model.CustomFieldType;
import model.User;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import service.CustomFieldsService;


import static org.assertj.core.api.Assertions.assertThat;

public class CreateCustomFieldTest extends BaseTest {

    BoardHelper boardHelper = new BoardHelper();
    BoardCleanup boardCleanup = new BoardCleanup();
    CustomFieldsService customFieldsService = new CustomFieldsService();

    @BeforeMethod
    public void setupUser() {
        UserContext.setCurrentUser(UserFactory.owner());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        TestContext context = TestContextHolder.getTestContext();
        String boardId = context.getBoardId();
        User boardCreator = context.getBoardCreator();
        if (boardId != null && boardCreator != null) {
            UserContext.setCurrentUser(boardCreator);
            boardCleanup.cleanupBoard(boardId);
        }
        context.setBoardId(null);
        context.setBoardCreator(null);

        UserContext.clear();
        TestContextHolder.clear();
    }

    @Test
    public void createCustomFieldWithRequiredFields() {

        // Tworzenie boarda
        CreateBoardRequest boardRequest = CreateBoardRequestFactory.defaultBoard();
        CreateBoardResponse board = boardHelper.createBoardSuccessfully(boardRequest);
        String boardId = board.getId();

        // Tworzenie Custom Field
        CreateCustomFieldRequest customFieldRequest = CreateCustomFieldFactory.defaultCustomField(boardId);

        CreateCustomFieldResponse response = customFieldsService.createCustomField(customFieldRequest)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(CreateCustomFieldResponse.class);

        assertThat(response.getIdModel()).isEqualTo(boardId);
        assertThat(response.getType()).isEqualTo(customFieldRequest.getType().getValue());
        assertThat(response.getModelType()).isEqualTo(customFieldRequest.getModelType());
        assertThat(response.getName()).isEqualTo(customFieldRequest.getName());
        assertThat(response.getPos()).isNotNull();
    }

    @DataProvider(name = "validTypes")
    public Object[][] validTypes() {
        return new Object[][]{
                {CustomFieldType.CHECKBOX},
                {CustomFieldType.LIST},
                {CustomFieldType.NUMBER},
                {CustomFieldType.TEXT},
                {CustomFieldType.DATE},
        };
    }

    @Test(dataProvider = "validTypes")
    public void shouldCreateCustomFieldWithValidRequiredTypes(CustomFieldType type) {
        // Tworzenie boarda
        CreateBoardRequest boardRequest = CreateBoardRequestFactory.defaultBoard();
        CreateBoardResponse board = boardHelper.createBoardSuccessfully(boardRequest);
        String boardId = board.getId();

        // Tworzenie Custom Field
        CreateCustomFieldRequest customFieldRequest = CreateCustomFieldFactory.customFieldWithType(boardId, type);

        CreateCustomFieldResponse response = customFieldsService.createCustomField(customFieldRequest)
                .then()
                .statusCode(200)
                .extract()
                .as(CreateCustomFieldResponse.class);

        assertThat(response.getType()).isEqualTo(customFieldRequest.getType().getValue());
    }

    @DataProvider(name = "invalidNames")
    public Object[][] invalidNames() {
        return new Object[][]{
                {""},
                {null},
        };
    }

    @Test(dataProvider = "invalidNames")
    public void shouldNotCreateCustomFieldWithInvalidName(String name) {
        // Tworzenie boarda
        CreateBoardRequest boardRequest = CreateBoardRequestFactory.defaultBoard();
        CreateBoardResponse board = boardHelper.createBoardSuccessfully(boardRequest);
        String boardId = board.getId();

        // Tworzenie Custom Field
        CreateCustomFieldRequest customFieldRequest = CreateCustomFieldFactory.customFieldWithName(boardId, name);

        ErrorResponse response = customFieldsService.createCustomField(customFieldRequest)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("invalid value for name");
        assertThat(response.getError()).isEqualTo("BAD_REQUEST_ERROR");
    }

    @DataProvider(name = "invalidIdModel")
    public Object[][] invalidIdModel() {
        String tooLongIdModel = "a".repeat(25);
        String tooShortIdModel = "a".repeat(23);
        return new Object[][]{
                {""},
                {" "},
                {null},
                {tooShortIdModel},
                {tooLongIdModel}
        };
    }

    @Test(dataProvider = "invalidIdModel")
    public void shouldNotCreateCustomFieldWithInvalidIdModel(String idModel) {

        // Tworzenie Custom Field
        CreateCustomFieldRequest customFieldRequest = CreateCustomFieldFactory.defaultCustomField(idModel);

        ErrorResponse response = customFieldsService.createCustomField(customFieldRequest)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("Invalid id");
        assertThat(response.getError()).isEqualTo("ERROR");
    }

    @Test
    public void shouldNotCreateDuplicateCustomField() {
        // Tworzenie boarda
        CreateBoardRequest boardRequest = CreateBoardRequestFactory.defaultBoard();
        CreateBoardResponse board = boardHelper.createBoardSuccessfully(boardRequest);
        String boardId = board.getId();

        // Tworzenie Custom Field
        CreateCustomFieldRequest customFieldRequest = CreateCustomFieldFactory.defaultCustomField(boardId);

        customFieldsService.createCustomField(customFieldRequest)
                .then()
                .statusCode(200);

        // próba utworzenia duplikatu
        ErrorResponse response = customFieldsService.createCustomField(customFieldRequest)
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("A custom field with that name and type already exists");
        assertThat(response.getError()).isEqualTo("CUSTOM_FIELD_DUPLICATE_FIELD");
    }

    @Test
    public void shouldNotCreateCustomFieldWithNonExistingBoardId() {

        String nonExistingBoardId = "a".repeat(24);

        // Tworzenie Custom Field
        CreateCustomFieldRequest customFieldRequest = CreateCustomFieldFactory.defaultCustomField(nonExistingBoardId);

        ErrorResponse response = customFieldsService.createCustomField(customFieldRequest)
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("Board not found");
        assertThat(response.getError()).isEqualTo("CUSTOM_FIELD_BOARD_NOT_FOUND");
    }

}
