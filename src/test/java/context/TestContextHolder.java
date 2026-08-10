package context;


public class TestContextHolder {

    private static final ThreadLocal<TestContext> testContext = new ThreadLocal<>();

    public static TestContext getTestContext() {
        if (testContext.get() == null) {
            testContext.set(new TestContext());
        }
        return testContext.get();
    }

    public static void clear() {
        testContext.remove();
    }
}
