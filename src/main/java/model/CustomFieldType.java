package model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomFieldType {
    CHECKBOX("checkbox"),
    LIST("list"),
    NUMBER("number"),
    TEXT("text"),
    DATE("date");

    private final String value;

    CustomFieldType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
