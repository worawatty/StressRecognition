package com.example.worawat.stressrecognition;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.models.nosql.ActivityDO;
import com.database.AWSDatabaseManagement;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitorService extends Service implements SensorEventListener{
    private UserPresentBroadcastReceiver responses;


    float step;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesService;

    private long startUsageTime;
    private long endUsageTime;

    private final int mInterval =900000;
    private int newId;
    private android.os.Handler mHandler;

    private ActivityDO mActivity;
    private long startTimer ;
    private boolean isScreenOn;
    long startDate;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public MonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());
        IntentFilter mStatusIntentFilter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        mStatusIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mStatusIntentFilter.addAction(Constants.AWS_FINISh);
        responses=new UserPresentBroadcastReceiver();
        registerReceiver(responses, mStatusIntentFilter);
        mHandler=new android.os.Handler();
        mActivity = new ActivityDO();
        isScreenOn=true;
        startTimer= System.currentTimeMillis();
        sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_service),Context.MODE_PRIVATE);
        SharedPreferences.Editor serviceStatusEditor=sharedPreferences.edit();
        serviceStatusEditor.putBoolean(getString(R.string.service_status),true);
        serviceStatusEditor.commit();






        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);



        Log.i("Service Initiate","True");

        sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name),Context.MODE_PRIVATE);
        startUsageTime=System.currentTimeMillis();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(getString(R.string.start_usage_time),startUsageTime);

        editor.commit();
        initiateActivityObject();
        //startRepeatingTask();
        //schedulerExplorerUpdateAWS();
        setAlarm(getApplicationContext());
        mSensorManager.registerListener(this,mStepCounterSensor,SensorManager.SENSOR_DELAY_FASTEST);
        Log.i("share pref  onCreate:", "Step Count: "+mActivity.getStepCounter()+
                "  \nUnlock Count: "+mActivity.getUnlockCounter()+
                "  \nUsage Tme: "+mActivity.getUsageTime()+
                "  \nMin Usage Time: "+mActivity.getMinUsageTime()+
                "  \nMax Usage Time: "+mActivity.getMaxUsageTime()+
                "  \nAverage Usage Time: "+mActivity.getAverageUsageTime());

    }

    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+1000*10,pi); // Millisec * Second * Minute
        Log.i("Alarm set:","TRUE");
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        writeToSharedPreference();
        unregisterReceiver(responses);
        Log.i("share pref  onDestroy:", "Step Count: "+mActivity.getStepCounter()+
                "  \nUnlock Count: "+mActivity.getUnlockCounter()+
                "  \nUsage Tme: "+mActivity.getUsageTime()+
                "  \nMin Usage Time: "+mActivity.getMinUsageTime()+
                "  \nMax Usage Time: "+mActivity.getMaxUsageTime()+
                "  \nAverage Usage Time: "+mActivity.getAverageUsageTime());
    }

    public void initiateActivityObject(){
        sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name),Context.MODE_PRIVATE);
        mActivity.setUnlockCounter((double)sharedPreferences.getInt(getString(R.string.unlock_counter),1));
        mActivity.setStepCounter((double)sharedPreferences.getInt(getString(R.string.step_counter),0));
        mActivity.setUsageTime((double)sharedPreferences.getInt(getString(R.string.usage_time),0));
        mActivity.setMinUsageTime((double)sharedPreferences.getInt(getString(R.string.min_usage_time),0));
        mActivity.setMaxUsageTime((double)sharedPreferences.getInt(getString(R.string.max_usage_time),0));
        mActivity.setAverageUsageTime((double)sharedPreferences.getInt(getString(R.string.average_usage_time),0));
    }

    public void writeToSharedPreference(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(getResources().getString(R.string.unlock_counter),mActivity.getUnlockCounter().intValue());
        editor.putInt(getResources().getString(R.string.step_counter),mActivity.getStepCounter().intValue());
        editor.putInt(getResources().getString(R.string.usage_time),mActivity.getUsageTime().intValue());
        editor.putInt(getResources().getString(R.string.max_usage_time),mActivity.getMaxUsageTime().intValue());
        editor.putInt(getResources().getString(R.string.min_usage_time),mActivity.getMinUsageTime().intValue());
        editor.putInt(getResources().getString(R.string.average_usage_time),mActivity.getAverageUsageTime().intValue());
        editor.commit();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        step =event.values[0];
        double currentStep = mActivity.getStepCounter();
        mActivity.setStepCounter(currentStep+1);
        Log.i("Event: ","OnSensorChanged");

        writeToSharedPreference();
        //Log.i("shared",sharedPreferences.getFloat(getString(R.string.step_counter),33.33f)+"");
        sendIntentToActivity();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void sendIntentToActivity(){
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
        localIntent.putExtra(Constants.COUNTER, ""+mActivity.getStepCounter().intValue());
        localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, mActivity.getUnlockCounter().intValue()+"");
        localIntent.putExtra(Constants.USAGE_TIME,mActivity.getUsageTime().intValue()+"");
        localIntent.putExtra(Constants.MAX_USAGE_TIME,mActivity.getMaxUsageTime().intValue()+"");
        localIntent.putExtra(Constants.MIN_USAGE_TIME,mActivity.getMinUsageTime().intValue()+"");
        localIntent.putExtra(Constants.AVG_USAGE_TIME,mActivity.getAverageUsageTime().intValue()+"");
        sendBroadcast(localIntent);
    }

    public void schedulerExplorerUpdateAWS() {
        final Runnable beeper = new Runnable() {
            public void run() {
                try {
                    Log.i("Runable","Delay is running");
                } finally {
                    // 100% guarantee that this always happens, even if
                    // your update method throws an exception

                    AWSDatabaseManagement awsDB=new AWSDatabaseManagement();
                    double currentTime = (double)System.currentTimeMillis();

                    mActivity.setTimestamp(currentTime);
                    mActivity.setTimePeriod(getTimePeriod());

                    //getActivityIdFromAWS();

                    SharedPreferences userSharePreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name_users), Context.MODE_PRIVATE);

                    mActivity.setUserId(userSharePreferences.getString(getString(R.string.user_email),"No Email"));
                    mActivity.setActivityId(mActivity.getTimestamp()+"");
                    //mActivity.setActivityId(newId+"");
                    if(isScreenOn){
                        setUsageTimeService(System.currentTimeMillis());
                    }
                    awsDB.insertData(mActivity);

                    Log.i("share pref Value:", "Step Count: "+mActivity.getStepCounter()+
                            "  \nUnlock Count: "+mActivity.getUnlockCounter()+
                            "  \nUsage Tme: "+mActivity.getUsageTime()+
                            "  \nMin Usage Time: "+mActivity.getMinUsageTime()+
                            "  \nMax Usage Time: "+mActivity.getMaxUsageTime()+
                            "  \nAverage Usage Time: "+mActivity.getAverageUsageTime());

                    mActivity=new ActivityDO();
                    if((System.currentTimeMillis()-startTimer)> 890000) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.clear();
                        editor.commit();
                        startTimer = System.currentTimeMillis();
                        Log.i("Share Preference Clear:", true+"");
                    }
                    initiateActivityObject();


                    //mHandler.postDelayed(mStatusChecker, mInterval);
                }
            }
        };
        scheduler.scheduleWithFixedDelay(beeper, 0, mInterval, TimeUnit.MILLISECONDS);


    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            try {
                Log.i("Runable","Delay is running");
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception

                AWSDatabaseManagement awsDB=new AWSDatabaseManagement();
                double currentTime = (double)System.currentTimeMillis();

                mActivity.setTimestamp(currentTime);
                mActivity.setTimePeriod(getTimePeriod());

                //getActivityIdFromAWS();

                SharedPreferences userSharePreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name_users), Context.MODE_PRIVATE);

                mActivity.setUserId(userSharePreferences.getString(getString(R.string.user_email),"No Email"));
                mActivity.setActivityId(mActivity.getTimestamp()+"");
                //mActivity.setActivityId(newId+"");
                if(isScreenOn){
                    setUsageTimeService(System.currentTimeMillis());
                }
                awsDB.insertData(mActivity);

                Log.i("share pref Value:", "Step Count: "+mActivity.getStepCounter()+
                        "  \nUnlock Count: "+mActivity.getUnlockCounter()+
                        "  \nUsage Tme: "+mActivity.getUsageTime()+
                        "  \nMin Usage Time: "+mActivity.getMinUsageTime()+
                        "  \nMax Usage Time: "+mActivity.getMaxUsageTime()+
                        "  \nAverage Usage Time: "+mActivity.getAverageUsageTime());

                mActivity=new ActivityDO();
                if((System.currentTimeMillis()-startTimer)> 590000) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    startTimer = System.currentTimeMillis();
                }
                initiateActivityObject();


                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };


    public void startRepeatingTask(){
        mStatusChecker.run();
    }
    public void stopRepeatingTask(){
        mHandler.removeCallbacks(mStatusChecker);
    }

    public String getTimePeriod(){
        Calendar currentTime = Calendar.getInstance();
        String period="1";
        double hour =currentTime.get(Calendar.HOUR_OF_DAY);
        Log.i("Current Time: ", hour+"");

        if(hour >= 1 && hour <6) {
            period = "1";
        }else if(hour>=6 && hour<12){
            period="2";
        }else if(hour>=12 && hour<18){
            period="3";
        }else period="4";

        return period;
    }

    private void setUsageTimeService(long endUsageTime){

        double sessionTime=(endUsageTime-startUsageTime);
        mActivity.setUsageTime(mActivity.getUsageTime()+sessionTime);
        if(sessionTime>mActivity.getMaxUsageTime()){
            mActivity.setMaxUsageTime(sessionTime);
        }
        if(sessionTime<mActivity.getMinUsageTime() && mActivity.getMinUsageTime()!=0){
            mActivity.setMinUsageTime(sessionTime);
        }else if(mActivity.getMinUsageTime()==0){
            mActivity.setMinUsageTime(sessionTime);
        }

        mActivity.setAverageUsageTime(mActivity.getUsageTime()/mActivity.getUnlockCounter());
        startUsageTime=System.currentTimeMillis();
        writeToSharedPreference();
        sendIntentToActivity();
    }



    private class UserPresentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {

            if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                startUsageTime=System.currentTimeMillis();


                mActivity.setUnlockCounter(mActivity.getUnlockCounter()+1);

                Log.i("Unlock Counter Service", mActivity.getUnlockCounter() + "");

                isScreenOn=true;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(getString(R.string.start_usage_time),startUsageTime);
                editor.putBoolean(getString(R.string.is_screen_in),isScreenOn);
                editor.commit();
                writeToSharedPreference();
                sendIntentToActivity();


            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                Log.i("Screen status:", "Screen Off");
                endUsageTime=System.currentTimeMillis();

                double sessionTime=(endUsageTime-startUsageTime);
                if(isScreenOn) {
                    mActivity.setUsageTime(mActivity.getUsageTime() + sessionTime);
                    if (sessionTime > mActivity.getMaxUsageTime()) {
                        mActivity.setMaxUsageTime(sessionTime);
                    }
                    if (sessionTime < mActivity.getMinUsageTime() && mActivity.getMinUsageTime() != 0) {
                        mActivity.setMinUsageTime(sessionTime);
                    } else if (mActivity.getMinUsageTime() == 0) {
                        mActivity.setMinUsageTime(sessionTime);
                    }

                    mActivity.setAverageUsageTime(mActivity.getUsageTime() / mActivity.getUnlockCounter());
                }
                isScreenOn=false;
                Log.i("share pref Screen off:", "Step Count: "+mActivity.getStepCounter()+
                        "  \nUnlock Count: "+mActivity.getUnlockCounter()+
                        "  \nUsage Tme: "+mActivity.getUsageTime()+
                        "  \nMin Usage Time: "+mActivity.getMinUsageTime()+
                        "  \nMax Usage Time: "+mActivity.getMaxUsageTime()+
                        "  \nAverage Usage Time: "+mActivity.getAverageUsageTime());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(getString(R.string.start_usage_time),startUsageTime);
                editor.putBoolean(getString(R.string.is_screen_in),isScreenOn);
                editor.commit();
                writeToSharedPreference();
                sendIntentToActivity();
            }else if (intent.getAction().equals(Constants.AWS_FINISh)){
                Log.i("AWS Finish Receive: ","True");
                mActivity=new ActivityDO();
                initiateActivityObject();
                startTimer=System.currentTimeMillis();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(getString(R.string.start_time),startTimer);

                editor.commit();
            }
        }
    }


}
