package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DeleteBoardResponse {

    @JsonProperty("_value")
    private Object value;

}
