package context;


public class TestContextHolder {
    private static TestContext testContext;

    public static TestContext getTestContext() {
        if (testContext == null) {
            testContext = new TestContext();
        }
        return testContext;
    }

    public static void clear() {
        testContext = null;
    }
}
