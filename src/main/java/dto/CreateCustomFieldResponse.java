package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class CreateCustomFieldResponse {

    private String id;
    private String idModel;
    private String modelType;
    private String type;
    private String name;
    private Long pos;
}
