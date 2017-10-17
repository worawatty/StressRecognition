package com.database;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.models.nosql.ActivityDO;
import com.amazonaws.models.nosql.UserDO;

import java.util.ArrayList;

/**
 * Created by Worawat on 11/29/2016.
 */
public class CreateUserAWS extends AsyncTask <UserDO, Void, Void> {
    public interface AsyncResponseCreateUser {
        void processFinish();
    }

    public AsyncResponseCreateUser delegate = null;

    public CreateUserAWS(AsyncResponseCreateUser delegate){
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(UserDO... params) {

        try {
            UserDO insertActivity = params[0];

            final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();


            dynamoDBMapper.save(insertActivity);

        } catch (final AmazonClientException ex) {
            Log.e("Test", "Failed saving item : " + ex.getMessage(), ex);

        }


        return null;
    }

    @Override
    protected void onPostExecute(Void v) {

        delegate.processFinish();

    }
}
