package context;

import lombok.Getter;
import lombok.Setter;
import model.User;


public class UserContext {
    @Getter
    @Setter
    private static User currentUser;

    public static void clear() {
        currentUser = null;
    }
}
