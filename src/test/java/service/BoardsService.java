package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Endpoint;
import dto.CreateBoardRequest;
import dto.UpdateBoardRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.AllureAttachments;

import java.util.Map;

public class BoardsService extends BaseService {

    protected final ObjectMapper mapper = new ObjectMapper();

    @Step("Create board with name: {request.name}")
    public Response createBoard(CreateBoardRequest request) {

        Map<String, Object> params = mapper.convertValue(request, Map.class);

        AllureAttachments.attachRequest("Create Board Request", request);

        Response response = getRequestSpecification()
                .queryParams(params)
                .when()
                .post(Endpoint.BOARDS.getUrl());

        AllureAttachments.attachResponse("Create Board Response", response.getBody().asString());

        return response;
    }

    @Step("Delete board {boardId}")
    public Response deleteBoard(String boardId) {

        AllureAttachments.attachRequest("Delete Board Request", boardId);

        Response response = getRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .delete(Endpoint.BOARDS.getUrl() + "/{id}");

        AllureAttachments.attachResponse("Delete Board Response", response.getBody().asString());

        return response;
    }

    @Step("Get a board {boardId}")
    public Response getBoard(String boardId) {

        AllureAttachments.attachRequest("Get Board Request", boardId);

        Response response = getRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .get(Endpoint.BOARDS.getUrl() + "/{id}");

        AllureAttachments.attachResponse("Get Board Response", response.getBody().asString());

        return response;
    }

    @Step("Update board {boardId} with new data")
    public Response updateBoard(String boardId, UpdateBoardRequest updateRequest) {

        Map<String, Object> params = mapper.convertValue(updateRequest, Map.class);

        AllureAttachments.attachRequest("Update Board Request", updateRequest);

        Response response = getRequestSpecification()
                .pathParam("id", boardId)
                .queryParams(params)
                .put(Endpoint.BOARDS.getUrl() + "/{id}");

        AllureAttachments.attachResponse("Update Board Response",
                response.getBody().asString());

        return response;
    }
}
