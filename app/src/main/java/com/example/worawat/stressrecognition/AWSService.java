package com.example.worawat.stressrecognition;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.amazonaws.models.nosql.ActivityDO;
import com.database.AWSDatabaseManagement;

import java.util.Calendar;

public class AWSService extends Service {
    ActivityDO mActivity;
    SharedPreferences sharedPreferences;
    private long startUsageTime;
    private boolean isScreenOn;
    private long startTimer;
    private long startDate;
    public AWSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate(){

            // 100% guarantee that this always happens, even if
            // your update method throws an exception

            mActivity = new ActivityDO();
            getSharedPreferences();
            AWSDatabaseManagement awsDB = new AWSDatabaseManagement();
            double currentTime = (double) System.currentTimeMillis();

            mActivity.setTimestamp(currentTime);
            mActivity.setTimePeriod(getTimePeriod());

            //getActivityIdFromAWS();

            SharedPreferences userSharePreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name_users), Context.MODE_PRIVATE);
            startDate = userSharePreferences.getLong(getString(R.string.start_date),0);
            mActivity.setUserId(userSharePreferences.getString(getString(R.string.user_email), "No Email"));
            mActivity.setActivityId(mActivity.getTimestamp() + "");
            //mActivity.setActivityId(newId+"");
            if (isScreenOn) {
                setUsageTimeService(System.currentTimeMillis());
            }
        if (isOnline(getApplicationContext())) {
            awsDB.insertData(mActivity);

            Log.i("share pref aws:", "Step Count: " + mActivity.getStepCounter() +
                    "  \nUnlock Count: " + mActivity.getUnlockCounter() +
                    "  \nUsage Tme: " + mActivity.getUsageTime() +
                    "  \nMin Usage Time: " + mActivity.getMinUsageTime() +
                    "  \nMax Usage Time: " + mActivity.getMaxUsageTime() +
                    "  \nAverage Usage Time: " + mActivity.getAverageUsageTime() +
                    "   \nStart Usage Time: " + startUsageTime +
                    "   \nisScreeon: " + isScreenOn +
                    "   \nStart Time: " + startTimer);

            mActivity = new ActivityDO();
            if ((System.currentTimeMillis() - startTimer) > 9000) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.clear();
                editor.commit();
                startTimer = System.currentTimeMillis();
                Log.i("Share Preference Clear:", true + "");
                getSharedPreferences();
                Log.i("After Clear:", "Step Count: " + mActivity.getStepCounter() +
                        "  \nUnlock Count: " + mActivity.getUnlockCounter() +
                        "  \nUsage Tme: " + mActivity.getUsageTime() +
                        "  \nMin Usage Time: " + mActivity.getMinUsageTime() +
                        "  \nMax Usage Time: " + mActivity.getMaxUsageTime() +
                        "  \nAverage Usage Time: " + mActivity.getAverageUsageTime() +
                        "   \nStart Usage Time: " + startUsageTime +
                        "   \nisScreeon: " + isScreenOn +
                        "   \nStart Time: " + startTimer);
            }
        }else {

            Log.i("No NW, Not Clear:", "Step Count: " + mActivity.getStepCounter() +
                    "  \nUnlock Count: " + mActivity.getUnlockCounter() +
                    "  \nUsage Tme: " + mActivity.getUsageTime() +
                    "  \nMin Usage Time: " + mActivity.getMinUsageTime() +
                    "  \nMax Usage Time: " + mActivity.getMaxUsageTime() +
                    "  \nAverage Usage Time: " + mActivity.getAverageUsageTime() +
                    "   \nStart Usage Time: " + startUsageTime +
                    "   \nisScreeon: " + isScreenOn +
                    "   \nStart Time: " + startTimer);
        }
        Log.i("usage Time: ",(startDate)+"");
            if (System.currentTimeMillis() - startDate <= 864000000) {
                setAlarm(getApplicationContext());
                Intent i = new Intent(Constants.AWS_FINISh);
                sendBroadcast(i);

            } else {

                stopService(new Intent(getApplicationContext(), MonitorService.class));
            }
            //mHandler.postDelayed(mStatusChecker, mInterval);

    }
    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+1000*60*30,pi); // Millisec * Second * Minute
        Log.i("Alarm set:","TRUE");
    }

    public void getSharedPreferences(){
        sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name),Context.MODE_PRIVATE);
        mActivity.setUnlockCounter((double)sharedPreferences.getInt(getString(R.string.unlock_counter),1));
        mActivity.setStepCounter((double)sharedPreferences.getInt(getString(R.string.step_counter),0));
        mActivity.setUsageTime((double)sharedPreferences.getInt(getString(R.string.usage_time),0));
        mActivity.setMinUsageTime((double)sharedPreferences.getInt(getString(R.string.min_usage_time),0));
        mActivity.setMaxUsageTime((double)sharedPreferences.getInt(getString(R.string.max_usage_time),0));
        mActivity.setAverageUsageTime((double)sharedPreferences.getInt(getString(R.string.average_usage_time),0));
        startUsageTime = sharedPreferences.getLong(getString(R.string.start_usage_time),0);
        isScreenOn = sharedPreferences.getBoolean(getString(R.string.is_screen_in),false);
        startTimer = sharedPreferences.getLong(getString(R.string.start_time),0);


        Log.i("Screen on: ",(isScreenOn)+"");
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

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
