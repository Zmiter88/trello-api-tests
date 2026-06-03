package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Endpoint;
import dto.CreateListRequest;
import io.restassured.response.Response;

import java.util.Map;

public class ListsService extends BaseService {

    public Response createLists(CreateListRequest request) {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> params = mapper.convertValue(request, Map.class);
        return getRequestSpecification()
                .queryParams(params)
                .when()
                .post(Endpoint.LISTS.getUrl());
    }

    public Response getList(String listId) {
        return getRequestSpecification()
                .pathParam("id", listId)
                .when()
                .get(Endpoint.CARDS.getUrl() + "/{id}");
    }
}
