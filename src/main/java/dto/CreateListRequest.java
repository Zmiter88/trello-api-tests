package dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateListRequest {
    private String listName;
    private String idBoard;
}
