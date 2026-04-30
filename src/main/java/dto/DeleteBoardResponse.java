package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteBoardResponse {

    @JsonProperty("_value")
    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
