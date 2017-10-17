package com.example.worawat.stressrecognition;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MonitorIntentService extends IntentService implements SensorEventListener{
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.worawat.smartphoneaddictionrecognization.action.FOO";
    private static final String ACTION_BAZ = "com.example.worawat.smartphoneaddictionrecognization.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.worawat.smartphoneaddictionrecognization.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.worawat.smartphoneaddictionrecognization.extra.PARAM2";

    private UserPresentBroadcastReceiver responses;
    private int unlockCounter;
    private Timer timer;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;

    public MonitorIntentService() {
        super("MonitorIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MonitorIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MonitorIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }

        //register IntentFilter for Global Broadcast Receiver
        IntentFilter mStatusIntentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        responses=new UserPresentBroadcastReceiver();
        registerReceiver(responses, mStatusIntentFilter);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        mSensorManager.registerListener(this,mStepCounterSensor,SensorManager.SENSOR_DELAY_FASTEST);

        Log.i("Service Initiate","True");
        timer = new Timer();
        unlockCounter=0;


        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                unlockCounter++;
                Log.i("Unlock Counter",""+unlockCounter);
                Intent localIntent = new Intent(Constants.BROADCAST_ACTION).putExtra(Constants.EXTENDED_DATA_STATUS, ""+unlockCounter);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);

            }
        },0,1000);*/


    }
    public void onDestroy() {
        unregisterReceiver(responses);

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float step =event.values[0];
        Log.i("Unlock Counter",unlockCounter+"");
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION).putExtra(Constants.COUNTER, ""+step);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class UserPresentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {

        /*Sent when the user is present after
         * device wakes up (e.g when the keyguard is gone)
         * */
            unlockCounter++;
            Log.i("Unlock Counter",unlockCounter+"");
            Intent localIntent = new Intent(Constants.BROADCAST_ACTION).putExtra(Constants.EXTENDED_DATA_STATUS, ""+unlockCounter);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
        /*Device is shutting down. This is broadcast when the device
         * is being shut down (completely turned off, not sleeping)
         * */

        }

    }
}
