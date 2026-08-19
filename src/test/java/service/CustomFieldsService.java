package service;

import config.Endpoint;
import dto.CreateCustomFieldRequest;
import io.restassured.response.Response;

public class CustomFieldsService extends BaseService {

    public Response createCustomField(CreateCustomFieldRequest request) {
        return getRequestSpecification()
                .body(request)
                .when()
                .post(Endpoint.CUSTOM_FIELDS.getUrl());
    }
}
