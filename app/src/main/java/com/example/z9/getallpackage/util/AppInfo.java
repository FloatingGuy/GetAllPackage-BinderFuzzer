package com.example.z9.getallpackage.util;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by zpd on 15-9-22.
 */
public class AppInfo{

    private String AppName;
    private String packageName;
    private PackageInfo info;
    private Drawable icon;

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public PackageInfo getInfo() {
        return info;
    }

    public void setInfo(PackageInfo info) {
        this.info = info;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
