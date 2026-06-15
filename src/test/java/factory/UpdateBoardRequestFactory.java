package factory;

import dto.UpdateBoardRequest;

public class UpdateBoardRequestFactory {

    public static UpdateBoardRequest defaultUpdate() {
        return UpdateBoardRequest.builder()
                .name("new name")
                .prefsBackground("blue")
                .build();
    }

    public static UpdateBoardRequest withName(String name) {
        return UpdateBoardRequest.builder()
                .name(name)
                .build();
    }
}
