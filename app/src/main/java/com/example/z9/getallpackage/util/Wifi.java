package com.example.z9.getallpackage.util;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class Wifi {
    public static void getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) (context.getSystemService(context.WIFI_SERVICE));
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();//获取DHCP 的信息
        String wifiProperty = "当前连接Wifi信息如下：" + "ip:" + FormatString(dhcpInfo.ipAddress) + "mask:" + FormatString(dhcpInfo.netmask) + "netgate:" + FormatString(dhcpInfo.gateway) + "dns:" + FormatString(dhcpInfo.dns1);
        String otherwifiInfo="Mac: "+wifiInfo.getMacAddress()+" ip: "+intToIp(wifiInfo.getIpAddress())+"ssid: "+wifiInfo.getSSID()+"networkid: "+wifiInfo.getNetworkId();
        Log.e("proyx",wifiProperty+"  "+otherwifiInfo);
    }



    //其中dhcpInfo属性的值为int型，要转换成通常见到的32位地址则需要转换方法
    public static String FormatString(int value) {
        String strValue = "";
        byte[] ary = intToByteArray(value);
        for (int i = ary.length - 1; i >= 0; i--) {
            strValue += (ary[i] & 0xFF);
            if (i > 0) {
                strValue += ".";
            }
        }
        return strValue;
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    public static String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }
}
