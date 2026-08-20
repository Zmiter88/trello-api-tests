package factory;

import data.RandomDataGenerator;
import dto.CreateCustomFieldRequest;
import model.CustomFieldType;

public class CreateCustomFieldFactory {

    public static CreateCustomFieldRequest defaultCustomField(String boardId) {
        return CreateCustomFieldRequest.builder()
                .name(RandomDataGenerator.customFieldName())
                .idModel(boardId)
                .modelType("board")
                .type(CustomFieldType.CHECKBOX)
                .pos("top")
                .build();
    }

    public static CreateCustomFieldRequest customFieldWithType(String boardId, CustomFieldType type) {
        return CreateCustomFieldRequest.builder()
                .name(RandomDataGenerator.customFieldName())
                .idModel(boardId)
                .modelType("board")
                .type(type)
                .pos("top")
                .build();
    }

    public static CreateCustomFieldRequest customFieldWithName(String boardId, String name) {
        return CreateCustomFieldRequest.builder()
                .name(name)
                .idModel(boardId)
                .modelType("board")
                .type(CustomFieldType.CHECKBOX)
                .pos("top")
                .build();
    }
}
