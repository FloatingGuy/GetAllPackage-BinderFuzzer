package com.example.z9.getallpackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int x =intent.getFlags()&8;
        Log.e("proyx",x+" zq"+intent.getFlags());
        sx14S(context);
    }


    public void sx14S(Context context1){

    }
}
