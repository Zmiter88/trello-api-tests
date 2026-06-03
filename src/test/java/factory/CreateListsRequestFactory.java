package factory;

import dto.CreateListRequest;

public class CreateListsRequestFactory {

    public static CreateListRequest withName(String name, String idBoard) {
        return CreateListRequest.builder()
                .listName(name)
                .idBoard(idBoard)
                .build();
    }
}
