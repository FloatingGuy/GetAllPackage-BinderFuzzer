package com.example.z9.getallpackage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class BeforeMainActivity extends Activity {

    private final static int ACCESS_COARSE_LOCATION = 101;
    private final static int WRITE_EXTERNAL_STORAGE_CODE = 102;
    private final static int MODIFY_PHONE_STATE = 103;
    private final static int RECEIVE_SMS = 104;

    public static PermissionModel[] models = new PermissionModel[]{
            new PermissionModel(Manifest.permission.ACCESS_COARSE_LOCATION, "我们需要读取手机信息的权限来标识您的身份", ACCESS_COARSE_LOCATION),
            new PermissionModel(Manifest.permission.READ_PHONE_STATE, "我们需要您允许我们读写你的存储卡，以方便我们临时保存一些数据", MODIFY_PHONE_STATE),
            new PermissionModel(Manifest.permission.WRITE_EXTERNAL_STORAGE, "我们需要您允许我们读写你的存储卡，以方便我们临时保存一些数据", WRITE_EXTERNAL_STORAGE_CODE),
            new PermissionModel(Manifest.permission.RECEIVE_SMS, "我们需要读取信息的权限来标识您的身份", RECEIVE_SMS)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 23) {
            openDemo();
            return;
        }
        checkPermissions();
    }

    private void openDemo() {
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this,MainActivity.class);
        getApplicationContext().startActivity(intent);
        this.finish();
    }

    private void checkPermissions() {
        try {
            for (PermissionModel model : models) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, model.permission)) {
                    ActivityCompat.requestPermissions(this, new String[]{model.permission}, model.requestCode);
                    return;
                }
            }
            openDemo();
            // 到这里就表示有米所有需要的权限已经通过申请，权限已经申请就打开demo
        } catch (Throwable e) {
            Log.e("gxfc", "", e);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MODIFY_PHONE_STATE:
            case ACCESS_COARSE_LOCATION:
                // 如果用户不允许，我们视情况发起二次请求或者引导用户到应用页面手动打开
                if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {

                    // 二次请求，表现为：以前请求过这个权限，但是用户拒接了
                    // 在二次请求的时候，会有一个“不再提示的”checkbox
                    // 因此这里需要给用户解释一下我们为什么需要这个权限，否则用户可能会永久不在激活这个申请
                    // 方便用户理解我们为什么需要这个权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        /*new AlertDialog.Builder(this).setTitle("权限申请").setMessage(findPermissionExplain(permissions[0]))
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkPermissions();
                                    }
                                }).show();*/
                    }
                    // 到这里就表示已经是第3+次请求，而且此时用户已经永久拒绝了，这个时候，我们引导用户到应用权限页面，让用户自己手动打开
                    else {
                        Toast.makeText(this, "部分权限被拒绝获取，将会会影响后续功能的使用，建议重新打开", Toast.LENGTH_LONG).show();
                        openAppPermissionSetting(123456789);
                    }
                }
                break;
            default:
                break;
        }
        // 到这里就表示用户允许了本次请求，我们继续检查是否还有待申请的权限没有申请
        if (isAllRequestedPermissionGranted()) {
            //Log.e("proyx", "all");
            openDemo();
        } else {
            checkPermissions();
        }
    }

    private String findPermissionExplain(String permission) {
        if (models != null) {
            for (PermissionModel model : models) {
                if (model != null && model.permission != null && model.permission.equals(permission)) {
                    return model.explain;
                }
            }
        }
        return null;
    }

    private boolean isAllRequestedPermissionGranted() {
        for (final PermissionModel model : models) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, model.permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case 123456789:
                if (isAllRequestedPermissionGranted()) {
                    openDemo();
                } else {
                    Toast.makeText(this, "部分权限被拒绝获取，退出", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean openAppPermissionSetting(int requestCode) {
        try {
            Intent intent =
                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + this.getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            // Android L 之后Activity的启动模式发生了一些变化
            // 如果用了下面的 Intent.FLAG_ACTIVITY_NEW_TASK ，并且是 startActivityForResult
            // 那么会在打开新的activity的时候就会立即回调 onActivityResult
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivityForResult(intent, requestCode);
            return true;
        } catch (Throwable e) {
            Log.e("gxfc", "", e);
        }
        return false;
    }

    public static class PermissionModel {
        /**
         * 请求的权限
         */
        public String permission;
        /**
         * 解析为什么请求这个权限
         */
        public String explain;
        /**
         * 请求代码
         */
        public int requestCode;

        public PermissionModel(String permission, String explain, int requestCode) {
            this.permission = permission;
            this.explain = explain;
            this.requestCode = requestCode;
        }
    }
}
