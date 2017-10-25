package com.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.util.Log;

import com.example.worawat.stressrecognition.Alarm;

import java.util.Calendar;

public class UsageService extends Service implements SensorEventListener{
    private final long SYNC_INTERVAL=1800000;
    public UsageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    //set the service to sync every SYNC_INTERVAL in millisecond
    public void setSyncTime(Context context){
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        //am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+1000*10,pi); // Millisec * Second * Minute
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SYNC_INTERVAL, SYNC_INTERVAL,pi);
        Log.i("Alarm set:","TRUE");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
