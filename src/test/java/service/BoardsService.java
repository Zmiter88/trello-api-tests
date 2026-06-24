package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Endpoint;
import dto.CreateBoardRequest;
import dto.UpdateBoardRequest;
import io.restassured.response.Response;

import java.util.Map;

public class BoardsService extends BaseService {

    protected final ObjectMapper mapper = new ObjectMapper();

    public Response createBoard(CreateBoardRequest request) {

        Map<String, Object> params = mapper.convertValue(request, Map.class);
        return getRequestSpecification()
                .queryParams(params)
                .when()
                .post(Endpoint.BOARDS.getUrl());
    }

    public Response deleteBoard(String boardId) {
        return getRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .delete(Endpoint.BOARDS.getUrl() + "/{id}");
    }

    public Response getBoard(String boardId) {
        return getRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .get(Endpoint.BOARDS.getUrl() + "/{id}");
    }

    public Response updateBoard(String boardId, UpdateBoardRequest updateRequest) {

        Map<String, Object> params = mapper.convertValue(updateRequest, Map.class);
        return getRequestSpecification()
                .pathParam("id", boardId)
                .queryParams(params)
                .when()
                .put(Endpoint.BOARDS.getUrl() + "/{id}");
    }
}
