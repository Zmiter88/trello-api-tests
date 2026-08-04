package factory;

import dto.CreateListRequest;

public class CreateListsRequestFactory {

    public static CreateListRequest withName(String name, String idBoard) {
        return CreateListRequest.builder()
                .name(name)
                .idBoard(idBoard)
                .build();
    }
}
