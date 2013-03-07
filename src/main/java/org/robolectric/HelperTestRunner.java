package org.robolectric;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.robolectric.internal.TestLifecycle;

public class HelperTestRunner extends BlockJUnit4ClassRunner {
    private final TestLifecycle testLifecycle;

    public HelperTestRunner(Class<?> testClass, TestLifecycle testLifecycle) throws InitializationError {
        super(testClass);
        this.testLifecycle = testLifecycle;
    }

    @Override protected Object createTest() throws Exception {
        Object test = super.createTest();
        testLifecycle.prepareTest(test);
        return test;
    }

    @Override public Statement classBlock(RunNotifier notifier) {
        return super.classBlock(notifier);
    }

    @Override public Statement methodBlock(FrameworkMethod method) {
        return super.methodBlock(method);
    }
}
