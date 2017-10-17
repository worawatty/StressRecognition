package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "smartphoneaddictionr-mobilehub-449133734-sasScore")

public class SasScoreDO {
    private String _scoreId;
    private String _userId;
    private String _sasScore;

    @DynamoDBHashKey(attributeName = "scoreId")
    @DynamoDBAttribute(attributeName = "scoreId")
    public String getScoreId() {
        return _scoreId;
    }

    public void setScoreId(final String _scoreId) {
        this._scoreId = _scoreId;
    }
    @DynamoDBRangeKey(attributeName = "userId")
    @DynamoDBIndexHashKey(attributeName = "userId", globalSecondaryIndexName = "user")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "sasScore")
    public String getSasScore() {
        return _sasScore;
    }

    public void setSasScore(final String _sasScore) {
        this._sasScore = _sasScore;
    }

}
