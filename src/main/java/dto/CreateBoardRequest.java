package dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardRequest {
    private String name;
    private String prefsBackground;
    private String desc;
}
