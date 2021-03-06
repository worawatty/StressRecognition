package com.database;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAutoGeneratedKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.models.nosql.ActivityDO;
import com.amazonaws.models.nosql.UserDO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.ArrayList;


/**
 * Created by Worawat on 11/28/2016.
 */
public class AWSDatabaseManagement implements GetFromAws.AsyncResponse{


    ActivityDO insertActivity;


    public void creatUserAWS(UserDO userDO){
        InsertUserToAws user = new InsertUserToAws();
        user.execute(userDO);
    }
    public void insertData(ActivityDO activityDO) {

        /*insertActivity=activityDO;
        getActivityIdFromAWS();*/
        InsertToAws a =  new InsertToAws();
        a.execute(activityDO);


    }
    public String getActivityIdFromAWS(){
        String id ="";

        GetFromAws asyncTask =new GetFromAws(this);
        asyncTask.delegate = this;
        asyncTask.execute();


        return id;

    }

    @Override
    public void processFinish(ArrayList<ActivityDO> output) {


        if (output.size()==0){
            insertActivity.setActivityId(insertActivity.getTimestamp()+"");
        }else{
            int maxId=0;
            for(int i=0; i<output.size(); i++){
                int currentId = Integer.parseInt(output.get(i).getActivityId());
                if(currentId>maxId){
                    maxId=currentId;
                }
            }
            int newId=maxId+1;
            insertActivity.setActivityId(newId+"");
            Log.i("Current Activity Id: ",newId+"");
            Log.i("Current Id After Set: ",insertActivity.getActivityId());
            InsertToAws a =  new InsertToAws();
            a.execute(insertActivity);
        }
    }


    class InsertToAws extends AsyncTask<ActivityDO, Void, Void> {
        @Override
        protected Void doInBackground(ActivityDO... params) {
            try {
                ActivityDO insertActivity = params[0];

                final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

                dynamoDBMapper.save(insertActivity);
            } catch (final AmazonClientException ex) {
                Log.e("Test", "Failed saving item : " + ex.getMessage(), ex);

            }


            return  null;
        }
        //ArrayList<NavgationItem> navLists=new ArrayList<>();



    }
    class InsertUserToAws extends AsyncTask<UserDO, Void, Void> {
        @Override
        protected Void doInBackground(UserDO... params) {
            try {
                UserDO insertActivity = params[0];
                final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

                dynamoDBMapper.save(insertActivity);
            } catch (final AmazonClientException ex) {
                Log.e("Test", "Failed saving item : " + ex.getMessage(), ex);

            }


            return  null;
        }
        //ArrayList<NavgationItem> navLists=new ArrayList<>();



    }

}
