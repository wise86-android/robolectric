package org.robolectric.shadows;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.*;
import android.content.res.Resources;
import android.os.Build;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.List;

@Implements(className = "android.app.ApplicationPackageManager", isInAndroidSdk = false)
public class ShadowApplicationPackageManager extends ShadowPackageManager {

  @Implementation(minSdk = Build.VERSION_CODES.LOLLIPOP)
  public PackageInstaller getPackageInstaller() {
    return ((PackageManager) RuntimeEnvironment.getRobolectricPackageManager()).getPackageInstaller();
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
    return ((PackageManager) RuntimeEnvironment.getRobolectricPackageManager()).getComponentEnabledSetting(componentName);
  }

  @Implementation
  public @Nullable String getNameForUid(int uid) {
    return ((PackageManager) RuntimeEnvironment.getRobolectricPackageManager()).getNameForUid(uid);
  }

  @Implementation
  public @Nullable String[] getPackagesForUid(int uid) {
    return ((PackageManager) RuntimeEnvironment.getRobolectricPackageManager()).getPackagesForUid(uid);
  }

  @Implementation
  public int getApplicationEnabledSetting(String packageName) {
    return ((PackageManager) RuntimeEnvironment.getRobolectricPackageManager()).getApplicationEnabledSetting(packageName);
  }

  @Implementation
  public ProviderInfo getProviderInfo(ComponentName component, int flags) throws PackageManager.NameNotFoundException {
    return ((PackageManager)RuntimeEnvironment.getRobolectricPackageManager()).getProviderInfo(component, flags);
  }

  @Implementation
  public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
    RuntimeEnvironment.getRobolectricPackageManager().setComponentEnabledSetting(componentName, newState, flags);
  }

  @Implementation
  public void setApplicationEnabledSetting(String packageName, int newState, int flags) {
    ((PackageManager) RuntimeEnvironment.getRobolectricPackageManager()).setApplicationEnabledSetting(packageName, newState, flags);
  }

  @Implementation
  public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getApplicationInfo(packageName, flags);
  }

  @Implementation
  public ResolveInfo resolveActivity(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().resolveActivity(intent, flags);
  }

  @Implementation
  public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getPackageInfo(packageName, flags);
  }

  @Implementation
  public List<ResolveInfo> queryIntentServices(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().queryIntentServices(intent, flags);
  }

  @Implementation
  public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().queryIntentActivities(intent, flags);
  }

  @Implementation
  public int checkPermission(String permName, String pkgName) {
    return RuntimeEnvironment.getRobolectricPackageManager().checkPermission(permName, pkgName);
  }

  @Implementation
  public ActivityInfo getReceiverInfo(ComponentName className, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getReceiverInfo(className, flags);
  }

  @Implementation
  public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().queryBroadcastReceivers(intent, flags);
  }

  @Implementation
  public ResolveInfo resolveService(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().resolveService(intent, flags);
  }

  @Implementation
  public ServiceInfo getServiceInfo(ComponentName className, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getServiceInfo(className, flags);
  }

  @Implementation
  public Resources getResourcesForApplication(@NonNull ApplicationInfo app) throws PackageManager.NameNotFoundException {
    if (app.packageName.equals(RuntimeEnvironment.application.getPackageName())) {
      return RuntimeEnvironment.application.getResources();
    }
    throw new PackageManager.NameNotFoundException(app.packageName);
  }

  @Implementation
  public List<ApplicationInfo> getInstalledApplications(int flags) {
    return null;
  }

  @Implementation
  public String getInstallerPackageName(String packageName) {
    return null;
  }

  @Implementation
  public PermissionInfo getPermissionInfo(String name, int flags) throws PackageManager.NameNotFoundException {
    return null;
  }

  @Implementation
  public boolean shouldShowRequestPermissionRationale(String permission) {
    return permissionRationalMap.containsKey(permission) ? permissionRationalMap.get(permission) : false;
  }

  @Implementation
  public FeatureInfo[] getSystemAvailableFeatures() {
    return systemAvailableFeatures.isEmpty() ? null : systemAvailableFeatures.toArray(new FeatureInfo[systemAvailableFeatures.size()]);
  }

  @Implementation
  public void freeStorageAndNotify(long freeStorageSize, IPackageDataObserver observer) {

  }

  @Implementation
  public void freeStorageAndNotify(String volumeUuid, long freeStorageSize, IPackageDataObserver observer) {

  }

  @Implementation
  public void setInstallerPackageName(String targetPackage, String installerPackageName) {

  }
}
