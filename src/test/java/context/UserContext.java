package context;
import model.User;


public class UserContext {

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static void clear() {
        currentUser.remove();
    }
}
