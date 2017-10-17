package com.example.worawat.stressrecognition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Worawat on 12/2/2016.
 */
public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent AWSService =new Intent(context, com.example.worawat.stressrecognition.AWSService.class);
        Log.i("Alarm: ","Alarm went off");
        context.startService(AWSService);
    }

}
