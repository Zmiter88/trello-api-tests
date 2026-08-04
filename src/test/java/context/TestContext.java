package context;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.User;

@Data
@NoArgsConstructor
public class TestContext {
    private String boardId;
    private User boardCreator;
    private String listId;
    private String cardId;
}
