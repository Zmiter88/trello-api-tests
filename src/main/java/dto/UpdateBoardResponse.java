package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class UpdateBoardResponse {
    private String id;
    private String name;
    private Prefs prefs;
}
