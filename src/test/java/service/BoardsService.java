package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Endpoint;
import dto.CreateBoardRequest;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoardsService extends BaseService {

    public Response createBoard(CreateBoardRequest request) {

        ObjectMapper mapper = new ObjectMapper();

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
}
