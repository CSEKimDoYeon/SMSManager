package com.example.kimdoyeon.smsmanager.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SMS_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i){
        if(i.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Toast.makeText(c, "SMS RECEIVE", Toast.LENGTH_SHORT).show();
        }
    }
}
