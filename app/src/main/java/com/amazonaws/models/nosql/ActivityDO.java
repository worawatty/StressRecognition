package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "smartphoneaddictionr-mobilehub-449133734-activity")

public class ActivityDO {
    private String _activityId;
    private String _timePeriod;
    private Double _averageUsageTime;
    private Double _maxUsageTime;
    private Double _minUsageTime;
    private Double _stepCounter;
    private Double _timestamp;
    private Double _unlockCounter;
    private Double _usageTime;
    private String _userId;

    @DynamoDBHashKey(attributeName = "activityId")
    @DynamoDBAttribute(attributeName = "activityId")
    public String getActivityId() {
        return _activityId;
    }

    public void setActivityId(final String _activityId) {
        this._activityId = _activityId;
    }
    @DynamoDBRangeKey(attributeName = "timePeriod")
    @DynamoDBAttribute(attributeName = "timePeriod")
    public String getTimePeriod() {
        return _timePeriod;
    }

    public void setTimePeriod(final String _timePeriod) {
        this._timePeriod = _timePeriod;
    }
    @DynamoDBAttribute(attributeName = "averageUsageTime")
    public Double getAverageUsageTime() {
        return _averageUsageTime;
    }

    public void setAverageUsageTime(final Double _averageUsageTime) {
        this._averageUsageTime = _averageUsageTime;
    }
    @DynamoDBAttribute(attributeName = "maxUsageTime")
    public Double getMaxUsageTime() {
        return _maxUsageTime;
    }

    public void setMaxUsageTime(final Double _maxUsageTime) {
        this._maxUsageTime = _maxUsageTime;
    }
    @DynamoDBAttribute(attributeName = "minUsageTime")
    public Double getMinUsageTime() {
        return _minUsageTime;
    }

    public void setMinUsageTime(final Double _minUsageTime) {
        this._minUsageTime = _minUsageTime;
    }
    @DynamoDBAttribute(attributeName = "stepCounter")
    public Double getStepCounter() {
        return _stepCounter;
    }

    public void setStepCounter(final Double _stepCounter) {
        this._stepCounter = _stepCounter;
    }
    @DynamoDBAttribute(attributeName = "timestamp")
    public Double getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(final Double _timestamp) {
        this._timestamp = _timestamp;
    }
    @DynamoDBAttribute(attributeName = "unlockCounter")
    public Double getUnlockCounter() {
        return _unlockCounter;
    }

    public void setUnlockCounter(final Double _unlockCounter) {
        this._unlockCounter = _unlockCounter;
    }
    @DynamoDBAttribute(attributeName = "usageTime")
    public Double getUsageTime() {
        return _usageTime;
    }

    public void setUsageTime(final Double _usageTime) {
        this._usageTime = _usageTime;
    }
    @DynamoDBIndexHashKey(attributeName = "userId", globalSecondaryIndexName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

}
