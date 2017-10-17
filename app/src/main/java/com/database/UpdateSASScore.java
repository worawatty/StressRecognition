package com.database;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.SasScoreDO;
import com.amazonaws.models.nosql.UserDO;

/**
 * Created by Worawat on 11/29/2016.
 */
public class UpdateSASScore extends AsyncTask<SasScoreDO, Void, Void> {

    public interface AsyncResponseScore {
        void processFinish();
    }

    public AsyncResponseScore delegate = null;

    public UpdateSASScore(AsyncResponseScore delegate){
        this.delegate = delegate;
    }
    @Override
    protected Void doInBackground(SasScoreDO... params) {
        try {
            SasScoreDO insertActivity = params[0];

            final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();


            dynamoDBMapper.save(insertActivity);

        } catch (final AmazonClientException ex) {
            Log.e("Test", "Failed saving item : " + ex.getMessage(), ex);

        }
        return null;
    }
    protected void onPostExecute(Void v) {

        delegate.processFinish();

    }
}
