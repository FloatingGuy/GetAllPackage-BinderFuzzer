package com.example.z9.getallpackage.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

public class Singature {
    public static int checkAppSignature(Context context) {
        String SIGNATURE = "5BC15D360865BBE11F70BD87E4B544F93A0633A8";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                byte[] signatureBytes = signature.toByteArray();
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sha1Sign=bytesToHexString(md.digest());
                Log.e("proyx1SHA1", sha1Sign);
                Log.e("proyx1",signature.toCharsString());
                Log.e("proyx","md5: "+getSignatureString(signature,"MD5"));
                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("proyx2Base64", "Include this string as a value for SIGNATURE:" + currentSignature);
                if (SIGNATURE.equalsIgnoreCase(sha1Sign)) {
                    return 1;
                }
            }
        } catch (Exception e) {
            //assumes an issue in checking signature., but we let the caller decide on what to do.2627
            e.printStackTrace();
        }
        return 0;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getSignatureString(Signature sig, String type) {
        byte[] hexBytes = sig.toByteArray();
        String fingerprint = "error!";
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            if (digest != null) {
                byte[] digestBytes = digest.digest(hexBytes);
                StringBuilder sb = new StringBuilder();
                for (byte digestByte : digestBytes) {
                    sb.append((Integer.toHexString((digestByte & 0xFF) | 0x100)).substring(1, 3));
                }
                fingerprint = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fingerprint;
    }
    public static char[] toChars(byte[] mSignature) {
        byte[] sig = mSignature;
        final int N = sig.length;
        final int N2 = N * 2;
        char[] text = new char[N2];
        for (int j = 0; j < N; j++) {
            byte v = sig[j];
            int d = (v >> 4) & 0xf;
            text[j * 2] = (char) (d >= 10 ? ('a' + d - 10) : ('0' + d));
            d = v & 0xf;
            text[j * 2 + 1] = (char) (d >= 10 ? ('a' + d - 10) : ('0' + d));
        }
        return text;
    }
}
