package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Endpoint;
import dto.CreateCardRequest;
import io.restassured.response.Response;

import java.util.Map;

public class CardsService extends BaseService {

    public Response createCards(CreateCardRequest request) {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> params = mapper.convertValue(request, Map.class);
        return getRequestSpecification()
                .queryParams(params)
                .when()
                .post(Endpoint.CARDS.getUrl());
    }

    public Response getCard(String cardId) {
        return getRequestSpecification()
                .pathParam("id", cardId)
                .when()
                .get(Endpoint.CARDS.getUrl() + "/{id}");
    }
}
