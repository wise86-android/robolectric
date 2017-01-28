package org.robolectric.shadows;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.*;
import android.graphics.drawable.Drawable;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.builder.RobolectricPackageManager;

import java.util.List;

@Implements(PackageManager.class)
public class ShadowPackageManager implements RobolectricPackageManager {
  @Override
  public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getPackageInfo(packageName, flags);
  }

  @Override
  public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getApplicationInfo(packageName, flags);
  }

  @Override
  @Implementation
  public ActivityInfo getActivityInfo(ComponentName className, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getActivityInfo(className, flags);
  }

  @Override
  public ActivityInfo getReceiverInfo(ComponentName className, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getReceiverInfo(className, flags);
  }

  @Override
  public ServiceInfo getServiceInfo(ComponentName className, int flags) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getServiceInfo(className, flags);
  }

  @Override
  public List<PackageInfo> getInstalledPackages(int flags) {
    throw new IllegalStateException("This method must be implemented in ShadowApplicationPackageManager");
  }

  @Override
  public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().queryIntentActivities(intent, flags);
  }

  @Override
  public List<ResolveInfo> queryIntentServices(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().queryIntentServices(intent, flags);
  }

  @Override
  public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().queryBroadcastReceivers(intent, flags);
  }

  @Override
  public ResolveInfo resolveActivity(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().resolveActivity(intent, flags);
  }

  @Override
  public ResolveInfo resolveService(Intent intent, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().resolveService(intent, flags);
  }

  @Override
  public void addResolveInfoForIntent(Intent intent, List<ResolveInfo> info) {
    RuntimeEnvironment.getRobolectricPackageManager().addResolveInfoForIntent(intent, info);
  }

  @Override
  public void addResolveInfoForIntent(Intent intent, ResolveInfo info) {
    RuntimeEnvironment.getRobolectricPackageManager().addResolveInfoForIntent(intent, info);
  }

  @Override
  public void removeResolveInfosForIntent(Intent intent, String packageName) {
    RuntimeEnvironment.getRobolectricPackageManager().removeResolveInfosForIntent(intent, packageName);
  }

  @Override
  public Drawable getActivityIcon(Intent intent) {
    return RuntimeEnvironment.getRobolectricPackageManager().getActivityIcon(intent);
  }

  @Override
  public Drawable getActivityIcon(ComponentName componentName) {
    return RuntimeEnvironment.getRobolectricPackageManager().getActivityIcon(componentName);
  }

  @Override
  public void addActivityIcon(ComponentName component, Drawable drawable) {
    RuntimeEnvironment.getRobolectricPackageManager().addActivityIcon(component, drawable);
  }

  @Override
  public void addActivityIcon(Intent intent, Drawable drawable) {
    RuntimeEnvironment.getRobolectricPackageManager().addActivityIcon(intent, drawable);
  }

  @Override
  public Drawable getApplicationIcon(String packageName) throws PackageManager.NameNotFoundException {
    return RuntimeEnvironment.getRobolectricPackageManager().getApplicationIcon(packageName);
  }

  @Override
  public void setApplicationIcon(String packageName, Drawable drawable) {
    RuntimeEnvironment.getRobolectricPackageManager().setApplicationIcon(packageName, drawable);
  }

  @Override
  public Intent getLaunchIntentForPackage(String packageName) {
    return RuntimeEnvironment.getRobolectricPackageManager().getLaunchIntentForPackage(packageName);
  }

  @Override
  public CharSequence getApplicationLabel(ApplicationInfo info) {
    return RuntimeEnvironment.getRobolectricPackageManager().getApplicationLabel(info);
  }

  @Override
  public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
    RuntimeEnvironment.getRobolectricPackageManager().setComponentEnabledSetting(componentName, newState, flags);
  }

  @Override
  public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity) {
    RuntimeEnvironment.getRobolectricPackageManager().addPreferredActivity(filter, match, set, activity);
  }

  @Override
  public int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) {
    return RuntimeEnvironment.getRobolectricPackageManager().getPreferredActivities(outFilters, outActivities, packageName);
  }

  @Override
  public ComponentState getComponentState(ComponentName componentName) {
    return RuntimeEnvironment.getRobolectricPackageManager().getComponentState(componentName);
  }

  @Override
  public void addPackage(PackageInfo packageInfo) {
    RuntimeEnvironment.getRobolectricPackageManager().addPackage(packageInfo);
  }

  @Override
  public void addPackage(String packageName) {
    RuntimeEnvironment.getRobolectricPackageManager().addPackage(packageName);
  }

  @Override
  public void addManifest(AndroidManifest androidManifest, int labelRes) {
    RuntimeEnvironment.getRobolectricPackageManager().addManifest(androidManifest, labelRes);
  }

  @Override
  public void removePackage(String packageName) {
    RuntimeEnvironment.getRobolectricPackageManager().removePackage(packageName);
  }

  @Override
  public boolean hasSystemFeature(String name) {
    return RuntimeEnvironment.getRobolectricPackageManager().hasSystemFeature(name);
  }

  @Override
  public void setSystemFeature(String name, boolean supported) {
    RuntimeEnvironment.getRobolectricPackageManager().setSystemFeature(name, supported);
  }

  @Override
  public void addDrawableResolution(String packageName, int resourceId, Drawable drawable) {
    RuntimeEnvironment.getRobolectricPackageManager().addDrawableResolution(packageName, resourceId, drawable);
  }

  @Override
  public Drawable getDrawable(String packageName, int resourceId, ApplicationInfo applicationInfo) {
    return RuntimeEnvironment.getRobolectricPackageManager().getDrawable(packageName, resourceId, applicationInfo);
  }

  @Override
  public int checkPermission(String permName, String pkgName) {
    return RuntimeEnvironment.getRobolectricPackageManager().checkPermission(permName, pkgName);
  }

  @Override
  public boolean isQueryIntentImplicitly() {
    return RuntimeEnvironment.getRobolectricPackageManager().isQueryIntentImplicitly();
  }

  @Override
  public void setQueryIntentImplicitly(boolean queryIntentImplicitly) {
    RuntimeEnvironment.getRobolectricPackageManager().setQueryIntentImplicitly(queryIntentImplicitly);
  }

  @Override
  public void reset() {
    RuntimeEnvironment.getRobolectricPackageManager().reset();
  }

  @Override
  public void setNameForUid(int uid, String name) {
    RuntimeEnvironment.getRobolectricPackageManager().setNameForUid(uid, name);
  }

  @Override
  public void setPackagesForCallingUid(String... packagesForCallingUid) {
    RuntimeEnvironment.getRobolectricPackageManager().setPackagesForCallingUid(packagesForCallingUid);
  }

  @Override
  public void setPackagesForUid(int uid, String... packagesForCallingUid) {
    RuntimeEnvironment.getRobolectricPackageManager().setPackagesForUid(uid, packagesForCallingUid);
  }

  @Override
  public PackageInfo getPackageArchiveInfo(String archiveFilePath, int flags) {
    return RuntimeEnvironment.getRobolectricPackageManager().getPackageArchiveInfo(archiveFilePath, flags);
  }
}
