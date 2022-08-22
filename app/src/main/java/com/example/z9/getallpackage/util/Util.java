package com.example.z9.getallpackage.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
public class Util {

    public static int SYSTEM_APPS = 0x1;
    public static int ALL_APPS = 0x2;
    public static int External_APPS = 0x3;

    public static final int MODULE_ACTIVITY = 0x0;
    public static final int MODULE_SERVICE = 0x1;
    public static final int MODULE_RECEIVER = 0x2;
    public static final int MODULE_ALL = 0x3;

    public static String init = "";

    public static List<AppInfo> getPackageInfo(Context context, int type){
        List<AppInfo> pkgInfoList = new ArrayList<AppInfo>();


        /*
        * 此处修改由于使用系统原生API获取不到部分App的问题，具体原因未知，修改为直接获取所有已安装应用，不使用flag，在获取到所有应用后根据包名单独去获取activity，service，receiver。
        *
        * */

//        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(
//                PackageManager.GET_DISABLED_COMPONENTS
//                        | PackageManager.GET_ACTIVITIES
//                        | PackageManager.GET_RECEIVERS
//                        | PackageManager.GET_INSTRUMENTATION
//                        | PackageManager.GET_SERVICES
//        );

        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo1 = null;
            PackageInfo packageInfo2 = null;
            PackageInfo packageInfo3 = null;
            PackageInfo packageInfo = null;
            try {
                packageInfo1 = context.getPackageManager().getPackageInfo(packages.get(i).packageName, PackageManager.GET_ACTIVITIES );
                packageInfo2 = context.getPackageManager().getPackageInfo(packages.get(i).packageName, PackageManager.GET_SERVICES );
                packageInfo3 = context.getPackageManager().getPackageInfo(packages.get(i).packageName, PackageManager.GET_RECEIVERS );
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            packageInfo = packages.get(i);
            packageInfo.activities = packageInfo1.activities;
            packageInfo.services = packageInfo2.services;
            packageInfo.receivers = packageInfo3.receivers;
            if (type == SYSTEM_APPS)
            {
                if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
                {
                    pkgInfoList.add(fillAppInfo(packageInfo, context));
                }
            }else if(type == External_APPS)
            {
                if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                {
                    pkgInfoList.add(fillAppInfo(packageInfo, context));
                }
            }else
            {
                pkgInfoList.add(fillAppInfo(packageInfo, context));
            }

        }
        return pkgInfoList;
    }

    private static AppInfo fillAppInfo(PackageInfo packageInfo, Context context){
        AppInfo appInfo = new AppInfo();
        appInfo.setInfo(packageInfo);
        appInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
        appInfo.setPackageName(packageInfo.packageName);
        appInfo.setIcon( packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
        return appInfo;

    }
}
