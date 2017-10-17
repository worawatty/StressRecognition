package com.example.worawat.stressrecognition;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;

public class StartService extends AppCompatActivity {
    Button startService;
    Button viewResult;
    public ResponseReceiver responses;
    TextView responcseText;
    TextView serviceStatusText;
    SharedPreferences sharedPreferences;
    String stepString;
    String unlockString;
    String usageTimeString;
    String maxUsageTimeString;
    String minUsageTimeString;
    String avgUsageTImeString;
    String displayText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());
        startService= (Button)findViewById(R.id.start_service);

        if(isMyServiceRunning(MonitorService.class)){
            Log.i("Service Running: ",""+true);
        }
         responcseText = (TextView)findViewById(R.id.responseText);
        serviceStatusText = (TextView)findViewById(R.id.servicestatus);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent serviceIntent = new Intent(getApplicationContext(),MonitorService.class);
                getApplicationContext().startService(serviceIntent);


            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        responses=new ResponseReceiver();
        Log.i("Intent", mStatusIntentFilter.getAction(0)+"");
        registerReceiver(responses, mStatusIntentFilter);
        if(isMyServiceRunning(MonitorService.class)){
            serviceStatusText.setText("Current Status\nService Running");
            startService.setEnabled(false);
            startService.setText("Service Running");
        }else {
            serviceStatusText.setText("Current Status\nService NOT Running\n Please press Start Service");
            startService.setEnabled(true);
            startService.setText("Start Service");
            startService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent serviceIntent = new Intent(getApplicationContext(),MonitorService.class);
                    serviceStatusText.setText("Current Status\nService Running");
                    startService.setEnabled(false);
                    startService.setText("Service Running");
                    getApplicationContext().startService(serviceIntent);

                }
            });
        }
        //read shared pref
        sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name),Context.MODE_PRIVATE);

        stepString = sharedPreferences.getInt(getString(R.string.step_counter),0)+"";
        unlockString = sharedPreferences.getInt(getString(R.string.unlock_counter),0)+"";
        usageTimeString = sharedPreferences.getInt(getString(R.string.usage_time),0)+"";
        minUsageTimeString = sharedPreferences.getInt(getString(R.string.min_usage_time),0)+"";
        maxUsageTimeString = sharedPreferences.getInt(getString(R.string.max_usage_time),0)+"";
        avgUsageTImeString = sharedPreferences.getInt(getString(R.string.average_usage_time),0)+"";

        Log.i("share pref Value:", "Step Count: "+stepString+
                "  \nUnlock Count: "+unlockString+
                "  \nUsage Tme: "+usageTimeString+
                "  \nMin Usage Time: "+minUsageTimeString+
                "  \nMax Usage Time: "+maxUsageTimeString+
                "  \nAverage Usage Time: "+avgUsageTImeString);

        if(!(stepString.equals("0")&&unlockString.equals("0")&&usageTimeString.equals("0"))){
            displayText="Step: "+stepString+
                    "\nUnlock: "+unlockString+
                    "\nUsage Time: "+usageTimeString+
                    "\nMin Usage Time: "+minUsageTimeString+
                    "\nMax Usage Time: "+maxUsageTimeString+
                    "\nAverage Usage Time: "+avgUsageTImeString;
            responcseText.setText(displayText);
           // Log.i("share pref Value:", "step: "+stepString+"  unlock"+unlockString);
        }

    }




    private class ResponseReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.BROADCAST_ACTION)) {
                stepString = intent.getStringExtra(Constants.COUNTER) + "";
                unlockString = intent.getStringExtra(Constants.EXTENDED_DATA_STATUS) + "";
                usageTimeString = intent.getStringExtra(Constants.USAGE_TIME)+"";
                maxUsageTimeString = intent.getStringExtra(Constants.MAX_USAGE_TIME)+"";
                minUsageTimeString = intent.getStringExtra(Constants.MIN_USAGE_TIME)+"";
                avgUsageTImeString = intent.getStringExtra(Constants.AVG_USAGE_TIME)+"";


//            Log.i("unlockString",intent.getStringExtra(Constants.EXTENDED_DATA_STATUS));
                displayText="Step: "+stepString+
                        "\nUnlock: "+unlockString+
                        "\nUsage Time: "+usageTimeString+
                        "\nMin Usage Time: "+minUsageTimeString+
                        "\nMax Usage Time: "+maxUsageTimeString+
                        "\nAverage Usage Time: "+avgUsageTImeString;
                responcseText.setText(displayText);
                Log.i("Text", stepString + "/" + unlockString);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(responses);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }
    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
