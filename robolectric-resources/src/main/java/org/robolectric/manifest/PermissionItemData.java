package org.robolectric.manifest;

public class PermissionItemData {

  private final String name;
  private final String label;
  private final String description;
  private final String permissionGroup;
  private final String protectionLevel;
  private final MetaData metaData;

  public PermissionItemData(String name, String label, String description, String permissionGroup, String protectionLevel, MetaData metaData) {
    this.protectionLevel = protectionLevel;
    this.metaData = metaData;
    this.name = name;
    this.label = label;
    this.description = description;
    this.permissionGroup = permissionGroup;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getPermissionGroup() {
    return permissionGroup;
  }

  public String getLabel() {
    return label;
  }

  public String getProtectionLevel() {
    return protectionLevel;
  }

  public MetaData getMetaData() {
    return metaData;
  }
}
