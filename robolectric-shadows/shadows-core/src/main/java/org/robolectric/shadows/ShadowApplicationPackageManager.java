package org.robolectric.shadows;

import android.annotation.Nullable;
import android.content.ComponentName;
import android.content.pm.*;
import android.os.Build;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.List;

@Implements(className = "android.app.ApplicationPackageManager", isInAndroidSdk = false)
public class ShadowApplicationPackageManager extends ShadowPackageManager {

  @Implementation(minSdk = Build.VERSION_CODES.LOLLIPOP)
  public PackageInstaller getPackageInstaller() {
    return RuntimeEnvironment.getPackageManager().getPackageInstaller();
  }

  @Implementation
  public List<PackageInfo> getInstalledPackages(int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().getInstalledPackages(flags);
  }

  @Implementation
  public ActivityInfo getActivityInfo(ComponentName component, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getActivityInfo(component, flags);
  }

  @Implementation
  public boolean hasSystemFeature(String name) {
    return RuntimeEnvironment.getRobolectricPackageManager().hasSystemFeature(name);
  }

  @Implementation
  public int getComponentEnabledSetting(ComponentName componentName) {
    return RuntimeEnvironment.getPackageManager().getComponentEnabledSetting(componentName);
  }

  @Implementation
  public @Nullable String getNameForUid(int uid) {
    return RuntimeEnvironment.getPackageManager().getNameForUid(uid);
  }

  @Implementation
  public @Nullable String[] getPackagesForUid(int uid) {
    return RuntimeEnvironment.getPackageManager().getPackagesForUid(uid);
  }

  @Implementation
  public int getApplicationEnabledSetting(String packageName) {
    return RuntimeEnvironment.getPackageManager().getApplicationEnabledSetting(packageName);
  }

  @Implementation
  public ProviderInfo getProviderInfo(ComponentName component, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getPackageManager().getProviderInfo(component, flags);
  }

  @Implementation
  public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
    RuntimeEnvironment.getPackageManager().setComponentEnabledSetting(componentName, newState, flags);
  }

  @Implementation
  public void setApplicationEnabledSetting(String packageName, int newState, int flags) {
    RuntimeEnvironment.getPackageManager().setApplicationEnabledSetting(packageName, newState, flags);
  }
}
