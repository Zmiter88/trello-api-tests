package dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateListRequest {
    private String name;
    private String idBoard;
}
