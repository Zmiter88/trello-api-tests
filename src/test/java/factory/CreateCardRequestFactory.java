package factory;

import dto.CreateCardRequest;
import dto.CreateListRequest;

public class CreateCardRequestFactory {

    public static CreateCardRequest defaultCard(String idList) {
        return CreateCardRequest.builder()
                .idList(idList)
                .build();
    }
}
