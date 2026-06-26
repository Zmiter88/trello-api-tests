package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
public class CreateBoardRequest {
    private String name;
    @JsonProperty("prefs_background")
    private String prefsBackground;
    private String desc;
}
