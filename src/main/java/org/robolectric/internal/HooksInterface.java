package org.robolectric.internal;

import org.robolectric.RobolectricContext;
import org.robolectric.util.DatabaseConfig;

import java.lang.reflect.Method;

public interface HooksInterface {
    public void resetStaticState();

    void setDatabaseMap(DatabaseConfig.DatabaseMap databaseMap);

    void setupApplicationState(Method method, TestLifecycle testLifecycle, RobolectricContext robolectricContext);
}
