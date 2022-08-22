package com.example.z9.getallpackage;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.z9.getallpackage.util.AppInfo;
import com.example.z9.getallpackage.util.IntentTest;
import com.example.z9.getallpackage.util.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {
    public static ArrayList<String> whiteList = new ArrayList<>();
    private static int INTERFACE_TRANSACTION = ('_' << 24) | ('N' << 16) | ('T' << 8) | 'F';
    LinkedHashSet<String> knowServer = new LinkedHashSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readIn("/sdcard/binderfuzz/theDone.txt", whiteList);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);

        knowServer.add("SurfaceFlinger");
        knowServer.add("android.service.gatekeeper.IGateKeeperService");
        knowServer.add("ashmem_device_service");
        knowServer.add("media.extractor");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                fuzzAll();
                break;
            case R.id.button2:
                fuzzAAAAA();
                break;
            case R.id.button3:
                printAllFunctions();
                break;
        }
    }

    public void test() {
        IBinder iBinder = getTheIbinder("device_policy");
        Parcel parcelData = Parcel.obtain();
        String interfaceName = getTheInterfaceDescriptor("device_policy");
        if (interfaceName == null || interfaceName.equalsIgnoreCase("")) {
            return;
        }
        parcelData.writeInterfaceToken(interfaceName);
        //parcelData.writeString("9999");
        parcelData.writeInt(1);
        Parcel parcelReply = Parcel.obtain();
        try {
            iBinder.transact(35, parcelData, parcelReply, 0);
            int val = parcelReply.readInt();
            Log.e("proyx", val + "  hree");
            Log.e("proyx", "ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
        parcelData.recycle();
        parcelReply.recycle();
        ;
        Log.e("proyx", "we are");
    }

    public void fuzzAll() {
        String[] result = getAllServices();
        for (int i = 0; i < result.length; i++) {
            if (whiteList.contains(result[i]))
                continue;
            try {
                binderFuzzOnMany(result[i]);
                m2("/sdcard/binderfuzz/theDone.txt", "\n" + result[i]);
            } catch (Exception e) {
                e.printStackTrace();
                m2("/sdcard/binderfuzz/theDone.txt", "\n" + result[i]);
                continue;
            }
        }
    }

    public void binderFuzzOnMany(String serviceName) {
        int methodNumber = getMethodNumberOfService(serviceName);
        for (int i = 1; i <= methodNumber; i++) {
            /*if (i == 1||i==2||i==3||i==4)
                continue;*/
            if (knowServer.contains(serviceName))
                continue;
            Log.e("proyx", "method  is " + serviceName + " method id is " + i);
            crashOnService(serviceName, i, "com.example.poc", "com.example.poc", "com.example.poc", "com.example.poc", "com.example.poc", "com.example.poc");
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void crashOnService(String serviceName, int methodNum, String p1V, String p2V, String p3V, String p4V, String p5V, String p6V) {
        IBinder iBinder = getTheIbinder(serviceName);
        Parcel parcelData = Parcel.obtain();
        String interfaceName = getTheInterfaceDescriptor(serviceName);
        parcelData.writeInterfaceToken(interfaceName);
        if (p1V != null)
            parcelData.writeString(p1V);
        if (p2V != null)
            parcelData.writeString(p2V);
        if (p3V != null)
            parcelData.writeString(p3V);
        if (p4V != null)
            parcelData.writeString(p4V);
        if (p5V != null)
            parcelData.writeString(p5V);
        if (p6V != null)
            parcelData.writeString(p6V);
        Parcel parcelReply = Parcel.obtain();
        try {
            iBinder.transact(methodNum, parcelData, parcelReply, 1);//getMatchingWifiConfig
        } catch (Exception e) {
            e.printStackTrace();
        }
        parcelData.recycle();
        parcelReply.recycle();
    }

    public void fuzzAAAAA() {
        String[] result = getAllServices();
        for (int i = 0; i < result.length; i++) {
            if (whiteList.contains(result[i]))
                continue;
            /*if(!result[i].equals("package"))
                continue;*/
            try {
                binderFuzzOnManyWithAAAA(result[i]);
                m2("/sdcard/binderfuzz/theDone.txt", "\n" + result[i]);
                //Thread.sleep(8000);
            } catch (Exception e) {
                e.printStackTrace();
                m2("/sdcard/binderfuzz/theDone.txt", "\n" + result[i]);
                continue;
            }
        }
    }

    public void binderFuzzOnManyWithAAAA(String serviceName) {
        int methodNumber = getMethodNumberOfService(serviceName);
        knowServer.add("package");
        knowServer.add("contexthub");
        knowServer.add("shortcut");
        knowServer.add("input_method");
        for (int i = 1; i <= methodNumber; i++) {
//            if ((serviceName.equals("package") && i == 56) || (serviceName.equals("package") && i == 114) || (serviceName.equals("contexthub") && i == 4) || (serviceName.equals("hwAlarmService") && i == 1) || (serviceName.equals("hwAlarmService") && i == 3) || (serviceName.equals("pgservice") && i == 11) || (serviceName.equals("shortcut") && i == 5) || (serviceName.equals("shortcut") && i == 11) || (serviceName.equals("shortcut") && i == 12) || (serviceName.equals("input_method") && i == 20))
//                continue;
            if (knowServer.contains(serviceName))
                continue;
            Log.e("proyx", "method  is " + serviceName + " method id is " + i);
            crashOnServiceWithAAAA(serviceName, i);
            Log.e("proyx", "method  is " + serviceName + " method id is " + i);
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void crashOnServiceWithAAAA(String serviceName, int methodNum) {
        String thevalue = "A";
        for (int i = 0; i < 21; i++) {
            for (int j = 1; j <= i; j++) {
                IBinder iBinder = getTheIbinder(serviceName);
                Parcel parcelData = Parcel.obtain();
                String interfaceName = getTheInterfaceDescriptor(serviceName);
                if (interfaceName == null || interfaceName.equalsIgnoreCase("")) {
                    continue;
                }
                parcelData.writeInterfaceToken(interfaceName);
                parcelData.writeString(thevalue);
                setData(parcelData);
                Parcel parcelReply = Parcel.obtain();
                try {
                    iBinder.transact(methodNum, parcelData, parcelReply, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                parcelData.recycle();
                parcelReply.recycle();
                thevalue += "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
            }
        }
    }

//     不传参数， 从 第33个Binder service以后开始
    public void fuzzAAAAANull() {
        String[] result = getAllServices();
        for (int i = 0; i < result.length; i++) {
            if (whiteList.contains(result[i]))
                continue;
            /*if(!result[i].equals("power"))
                continue;*/
            if (i < 33)
                continue;
            try {
                binderFuzzOnManyWithAAAANull(result[i]);
                m2("/sdcard/binderfuzz/theDone.txt", "\n" + result[i]);
            } catch (Exception e) {
                e.printStackTrace();
                m2("/sdcard/binderfuzz/theDone.txt", "\n" + result[i]);
                continue;
            }
        }
    }

    public void binderFuzzOnManyWithAAAANull(String serviceName) {
        int methodNumber = getMethodNumberOfService(serviceName);
        for (int i = 1; i <= methodNumber; i++) {
            /*if (i == 54)
                continue;*/
            Log.e("proyx", "method  is " + serviceName + " method id is " + i);
            crashOnServiceWithAAAANull(serviceName, i);
            Log.e("proyx", "method  is " + serviceName + " method id is " + i);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void crashOnServiceWithAAAANull(String serviceName, int methodNum) {
        IBinder iBinder = getTheIbinder(serviceName);
        Parcel parcelData = Parcel.obtain();
        String interfaceName = getTheInterfaceDescriptor(serviceName);
        if (interfaceName == null || interfaceName.equalsIgnoreCase("")) {
            return;
        }
        parcelData.writeInterfaceToken(interfaceName);
        Parcel parcelReply = Parcel.obtain();
        try {
            iBinder.transact(methodNum, parcelData, parcelReply, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        parcelData.recycle();
        parcelReply.recycle();
    }




    public static void readIn(String filePath, ArrayList<String> arrayList) {
        try {
            BufferedReader bufr = new BufferedReader(new FileReader(filePath));
            String line = null;
            while ((line = bufr.readLine()) != null) {
                arrayList.add(line);
            }
            //arrayList.remove(arrayList.size()-1);
            bufr.close();
        } catch (Exception e) {
            e.printStackTrace();// TODO: handle exception
        }
    }

    public static void m2(String file, String content) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public static void setData(Parcel data) {
        data.writeInt(0x7FFFFFFF);
        data.writeSerializable(new IntentTest());
        data.writeInt(0x8FFFFFFF);
        data.writeLong(0x8FFFFFFFFFFFFFFFL);
        data.writeString("dddddddddddddddddddddddddddddddddddddddd");
        data.writeDouble(1234.5678);
    }

    public void startScan() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        ArrayList<ScanResult> list = (ArrayList<ScanResult>) wifiManager.getScanResults();
        // 得到配置好的网络连接
        List<WifiConfiguration> mWifiConfiguration = wifiManager.getConfiguredNetworks();
        for (int i = 0; i < mWifiConfiguration.size(); i++) {
            Log.e("proyx2", mWifiConfiguration.toString());
        }
    }

    //return value is the number of method in one service
    public int getMethodNumberOfService(String serviceName) {
        int result = 0;
        try {
            IBinder serviceBinder = getTheIbinder(serviceName);
            String interfaceName = getInterfaceName(serviceBinder);
            Log.i("proyx", interfaceName);
            Class theClass = Class.forName(interfaceName + "$Stub");
            Field[] fields = theClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].getName();
                    getCODE(interfaceName, fields[i].getName());
                    result++;
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    private static String getInterfaceName(IBinder serHandle) {
        try {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            serHandle.transact(INTERFACE_TRANSACTION, data, reply, 0);
            String interfacename = reply.readString();
            data.recycle();
            reply.recycle();
            return interfacename;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static int getCODE(String interfaceName, String fieldName) {
        try {
            Class clstub = Class.forName(interfaceName + "$Stub");
            Field field = clstub.getDeclaredField(fieldName);//"TRANSACTION_freeStorage"
            field.setAccessible(true);
            int val = field.getInt(null);
            return val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  0;
    }

    public static String getTheInterfaceDescriptor(String servicename) {
        IBinder iBinder = getTheIbinder(servicename);
        if (iBinder == null) {
            return "";
        }
        String result = "";
        try {
            result = iBinder.getInterfaceDescriptor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static IBinder getTheIbinder(String name) {
        IBinder iBinder = null;
        try {
            iBinder = (IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", String.class).invoke(null, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return iBinder;
    }

    public static String[] getAllServices() {
        String[] result = null;
        try {
            result = (String[]) Class.forName("android.os.ServiceManager").getMethod("listServices").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < result.length; i++) {
            Log.e("proyx", result[i]);
        }
        return result;
    }

    public void getAllPackageInfo() {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        appInfos = (ArrayList<AppInfo>) Util.getPackageInfo(this, 2);
        String str = "";

        for (int i = 0; i < appInfos.size(); i++) {
            str += "\"" + appInfos.get(i).getPackageName() + "\",";
            Log.e("proyx", appInfos.get(i).getPackageName());
        }
    }

    public static void printAllFunctions() {
        String[] result = getAllServices();
        for (int i = 0; i < result.length; i++) {
            getAllTheFunctionInOneService(result[i]);
        }
    }

    public static void getAllTheFunctionInOneService(String serviceName) {
        try {
            IBinder serviceBinder = getTheIbinder(serviceName);
            String interfaceName = getInterfaceName(serviceBinder);
            Class theClass = Class.forName(interfaceName + "$Stub");
            Field[] fields = theClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                try {
                    Log.e("proyx", interfaceName + " " + fields[i].getName());
                    Log.e("proyx", fields[i].getName() + "  dd " + getCODE(interfaceName, fields[i].getName()));
                    m2("/sdcard/binderfuzz/allFun.txt", "\n" + fields[i].getName() + " " + serviceName + " " + getCODE(interfaceName, fields[i].getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getIMEI() {
        try {
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            Class clazz = manager.getClass();
            Method getImei = clazz.getDeclaredMethod("getImei", int.class);//(int slotId)
            String xx = (String) getImei.invoke(manager, 0);
            Log.e("proyx", xx);
            getImei.invoke(manager, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
