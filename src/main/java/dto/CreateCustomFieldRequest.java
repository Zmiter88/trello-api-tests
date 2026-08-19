package dto;

import lombok.Builder;
import lombok.Data;
import model.CustomFieldType;

@Data
@Builder
public class CreateCustomFieldRequest {
    private String idModel;
    private String modelType;
    private String name;
    private CustomFieldType type;
    private String options;
    private Object pos;
}
