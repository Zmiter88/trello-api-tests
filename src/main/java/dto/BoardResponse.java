package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter

public class BoardResponse {
        private String id;
        private String name;
    }
