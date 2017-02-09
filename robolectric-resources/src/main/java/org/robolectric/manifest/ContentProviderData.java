package org.robolectric.manifest;

import java.util.List;

public class ContentProviderData extends PackageItemData {
  private final String authorities;
  private final String readPermission;
  private final String writePermission;
  private final List<PathPermissionData> pathPermissionDatas;

  public ContentProviderData(String className, MetaData metaData, String authorities, String readPermission, String writePermission, List<PathPermissionData> pathPermissionDatas) {
    super(className, metaData);
    this.authorities = authorities;
    this.readPermission = readPermission;
    this.writePermission = writePermission;
    this.pathPermissionDatas = pathPermissionDatas;
  }

  public String getAuthorities() {
    return authorities;
  }

  public String getReadPermission() {
    return readPermission;
  }

  public String getWritePermission() {
    return writePermission;
  }

  public List<PathPermissionData> getPathPermissionDatas() {
    return pathPermissionDatas;
  }
}
