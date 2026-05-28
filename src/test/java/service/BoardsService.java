package service;

import io.restassured.response.Response;

public class BoardsService extends BaseService {

    public Response createBoard(String name) {
        return getRequestSpecification()
                .queryParam("name", name)
                .when()
                .post("/boards");
    }
}
