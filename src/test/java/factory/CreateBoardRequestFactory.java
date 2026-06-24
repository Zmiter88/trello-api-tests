package factory;

import data.RandomDataGenerator;
import dto.CreateBoardRequest;

public class CreateBoardRequestFactory {

    // DEFAULT (najczęściej używany)
    public static CreateBoardRequest defaultBoard() {
        return CreateBoardRequest.builder()
                .name(RandomDataGenerator.boardName())
                .prefsBackground("blue")
                .desc("default board")
                .build();
    }

    // DATA-DRIVEN  (z testu)
    public static CreateBoardRequest withName(String name) {
        return CreateBoardRequest.builder()
                .name(name)
                .prefsBackground("blue")
                .desc("test board")
                .build();
    }

    // Randomized
    public static CreateBoardRequest randomBoard() {
        return CreateBoardRequest.builder()
                .name(RandomDataGenerator.boardName())
                .prefsBackground(RandomDataGenerator.color())
                .desc(RandomDataGenerator.description())
                .build();
    }
}
