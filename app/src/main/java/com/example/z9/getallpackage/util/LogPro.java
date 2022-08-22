package com.example.z9.getallpackage.util;

import android.util.Log;

public class LogPro {
    public static void logpro(Object string){
        if(string==null){
            Log.e("proyx","value is null ");
        }
        try{
            Log.e("proyx",String.valueOf(string)+" ");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
