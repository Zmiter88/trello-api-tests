package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    String name;
    String apiKey;
    String apiToken;
    String memberId;
    Role role;
}
