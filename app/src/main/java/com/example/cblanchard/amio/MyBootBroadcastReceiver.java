package com.example.cblanchard.amio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cblanchard on 01/03/2018.
 */

public class MyBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        if(pref.getBoolean("Check", true)!=false){
            editor.putBoolean("Check",false );
        }else {
            context.startService(intent);
        }
    }
}
