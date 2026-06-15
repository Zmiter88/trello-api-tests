package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardRequest {
    private String name;
    @JsonProperty("prefs_background")
    private String prefsBackground;
    private String desc;
}
