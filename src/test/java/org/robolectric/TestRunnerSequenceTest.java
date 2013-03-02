package org.robolectric;

import android.app.Application;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.robolectric.bytecode.ShadowMap;
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

        @Override protected Class<? extends TestLifecycle> getTestLifecycleClass() {
            return MyTestLifecycle.class;
        }

        @Override protected synchronized ShadowMap createShadowMap() {
            transcript.add("configureShadows");
            return super.createShadowMap();
        }

        public static class MyTestLifecycle extends DefaultTestLifecycle {
            @Override public void beforeTest(Method method) {
                transcript.add("beforeTest");
            }

//            @Override protected void resetStaticState() {
//                transcript.add("resetStaticState");
//            }

            @Override
            public void setupApplicationState(Method testMethod) {
                transcript.add("setupApplicationState");
            }
        }
    }
}
