package com.example.z9.getallpackage;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Main4Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Log.e("proyx",getIntent().getScheme()+" xxxxxxxxxxxxxxxxxxx");
    }
}
