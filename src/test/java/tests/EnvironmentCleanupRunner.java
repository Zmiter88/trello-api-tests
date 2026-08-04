package tests;

import cleanup.BoardCleanup;
import context.UserContext;
import factory.UserFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Epic("Maintenance")
@Feature("Cleanup")
public class EnvironmentCleanupRunner extends BaseTest {

    private final BoardCleanup boardCleanup = new BoardCleanup();

    @Test
    public void cleanupOwnerBoards() {
        UserContext.setCurrentUser(UserFactory.owner());
        boardCleanup.cleanupAllBoards();
    }

    @Test
    public void cleanupMemberBoards() {
        UserContext.setCurrentUser(UserFactory.member());
        boardCleanup.cleanupAllBoards();
    }
}
