package data;

import java.util.UUID;

public class RandomDataGenerator {

    public static String boardName() {
        return "board-" + UUID.randomUUID();
    }

    public static String description() {
        return "desc-" + UUID.randomUUID();
    }

    public static String color() {
        String[] colors = {"blue", "red", "green", "yellow"};
        return colors[(int) (Math.random() * colors.length)];
    }

    public static String customFieldName() {
        return "custom-field-" + UUID.randomUUID();
    }
}
