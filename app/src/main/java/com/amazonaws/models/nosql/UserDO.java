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

@DynamoDBTable(tableName = "smartphoneaddictionr-mobilehub-449133734-user")

public class UserDO {
    private String _phoneMAC;
    private Double _age;
    private String _gender;
    private String _username;

    @DynamoDBHashKey(attributeName = "phoneMAC")
    @DynamoDBAttribute(attributeName = "phoneMAC")
    public String getPhoneMAC() {
        return _phoneMAC;
    }

    public void setPhoneMAC(final String _phoneMAC) {
        this._phoneMAC = _phoneMAC;
    }
    @DynamoDBIndexHashKey(attributeName = "age", globalSecondaryIndexName = "age")
    public Double getAge() {
        return _age;
    }

    public void setAge(final Double _age) {
        this._age = _age;
    }
    @DynamoDBIndexHashKey(attributeName = "gender", globalSecondaryIndexName = "gender")
    public String getGender() {
        return _gender;
    }

    public void setGender(final String _gender) {
        this._gender = _gender;
    }
    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return _username;
    }

    public void setUsername(final String _username) {
        this._username = _username;
    }

}
