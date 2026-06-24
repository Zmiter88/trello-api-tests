package factory;

import dto.CreateListRequest;

public class CreateListsRequestFactory {

    public static CreateListRequest withName(String listName, String idBoard) {
        return CreateListRequest.builder()
                .listName(listName)
                .idBoard(idBoard)
                .build();
    }
}
