package com.database;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.models.nosql.ActivityDO;

import java.util.ArrayList;

/**
 * Created by Worawat on 11/29/2016.
 */
public class GetFromAws extends AsyncTask <Void, Void, ArrayList<ActivityDO>> {
    public interface AsyncResponse {
        void processFinish(ArrayList<ActivityDO> output);
    }

    public AsyncResponse delegate = null;

    public GetFromAws(AsyncResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected ArrayList<ActivityDO> doInBackground(Void... params) {
        ArrayList<ActivityDO> resultActivities = new ArrayList<>();
        try {
            final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();


            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<ActivityDO> result = dynamoDBMapper.scan(ActivityDO.class, scanExpression);
            ActivityDO resultActivity ;
            if(result.size()>0){
                for(int i=0; i<result.size(); i++){
                    resultActivity = result.get(i);
                    resultActivities.add(resultActivity);
                }
            }
            Log.i("result ID: ", result.size()+"");
        } catch (final AmazonClientException ex) {
            Log.e("Test", "Failed saving item : " + ex.getMessage(), ex);

        }


        return  resultActivities;
    }
    @Override
    protected void onPostExecute(ArrayList<ActivityDO> result) {

        delegate.processFinish(result);

    }

}
