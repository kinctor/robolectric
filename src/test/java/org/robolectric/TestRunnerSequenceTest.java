package org.robolectric;

import android.app.Application;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.robolectric.annotation.Config;
import org.robolectric.internal.TestLifecycle;
import org.robolectric.util.Transcript;

import java.lang.reflect.Method;

import static org.robolectric.util.TestUtil.resourceFile;

public class TestRunnerSequenceTest {
    public static Transcript transcript;

    @Test public void shouldRunThingsInTheRightOrder() throws Exception {
        transcript = new Transcript();
        new TestRunnerSequenceTest.Runner(SimpleTest.class).run(new RunNotifier());
        transcript.assertEventsSoFar(
                "configureShadows",
                "resetStaticState",
                "setupApplicationState",
                "createApplication",
                "beforeTest",
                "prepareTest",
                "TEST!",
                "afterTest"
        );
    }

    public static class SimpleTest {
        @Test public void shouldDoNothingMuch() throws Exception {
            transcript.add("TEST!");
        }
    }

    public static class Runner extends RobolectricTestRunner {
        public Runner(Class<?> testClass) throws InitializationError {
            super(testClass);
        }

        @Override
        protected AndroidManifest createAppManifest() {
            return new AndroidManifest(resourceFile("TestAndroidManifest.xml"), resourceFile("res"), resourceFile("assets"));
        }

        @Override protected Class<? extends TestLifecycle> getTestLifecycleClass() {
            return MyTestLifecycle.class;
        }

        @Override protected void configureShadows(Config config) {
            transcript.add("configureShadows");
            super.configureShadows(config);
        }
    }

    public static class MyTestLifecycle extends DefaultTestLifecycle {
        @Override public Application createApplication(Method method) {
            transcript.add("createApplication");
            return super.createApplication(method);
        }

        @Override public void prepareTest(Object test) {
            transcript.add("prepareTest");
        }

        @Override public void beforeTest(Method method) {
            transcript.add("beforeTest");
        }

//            @Override protected void resetStaticState() {
//                transcript.add("resetStaticState");
//            }

        @Override public void afterTest(Method method) {
            transcript.add("afterTest");
        }

        @Override
        public void setupApplicationState(Method testMethod) {
            transcript.add("setupApplicationState");
        }
    }
}
