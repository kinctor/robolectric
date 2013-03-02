package org.robolectric.internal;

import android.app.Application;
import android.content.res.Resources;
import org.robolectric.AndroidManifest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricContext;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.res.ResourceLoader;
import org.robolectric.shadows.ShadowAccountManager;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowBitmapFactory;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowContext;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.ShadowMediaStore;
import org.robolectric.shadows.ShadowMimeTypeMap;
import org.robolectric.shadows.ShadowPowerManager;
import org.robolectric.shadows.ShadowResources;
import org.robolectric.shadows.ShadowStatFs;
import org.robolectric.shadows.ShadowTypeface;
import org.robolectric.util.DatabaseConfig;

import java.lang.reflect.Method;

import static org.robolectric.Robolectric.shadowOf;

public class Hooks implements HooksInterface {
    public void resetStaticState() {
        Robolectric.getShadowWrangler().silence();
        Robolectric.application = null;
        ShadowAccountManager.reset();
        ShadowBitmapFactory.reset();
        ShadowDrawable.reset();
        ShadowMediaStore.reset();
        ShadowLog.reset();
        ShadowContext.clearFilesAndCache();
        ShadowLooper.resetThreadLoopers();
        ShadowDialog.reset();
        ShadowContentResolver.reset();
//        ShadowLocalBroadcastManager.reset();
        ShadowMimeTypeMap.reset();
        ShadowPowerManager.reset();
        ShadowStatFs.reset();
        ShadowTypeface.reset();
    }

    @Override public void setDatabaseMap(DatabaseConfig.DatabaseMap databaseMap) {
        DatabaseConfig.setDatabaseMap(databaseMap);
    }

    @Override public void setupApplicationState(Method method, TestLifecycle testLifecycle, RobolectricContext robolectricContext) {
        ResourceLoader systemResourceLoader = RobolectricTestRunner.getSystemResourceLoader(robolectricContext.getSystemResourcePath());
        ShadowResources.setSystemResources(systemResourceLoader);

        AndroidManifest appManifest = robolectricContext.getAppManifest();
        ResourceLoader resourceLoader = RobolectricTestRunner.getAppResourceLoader(systemResourceLoader, appManifest);

        Robolectric.application = ShadowApplication.bind((Application) testLifecycle.createApplication(method), appManifest, resourceLoader);

        String qualifiers = RobolectricTestRunner.determineResourceQualifiers(method);
        shadowOf(Resources.getSystem().getConfiguration()).overrideQualifiers(qualifiers);
        shadowOf(Robolectric.application.getResources().getConfiguration()).overrideQualifiers(qualifiers);
    }
}
