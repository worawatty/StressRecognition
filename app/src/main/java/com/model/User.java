package com.model;

import com.amazonaws.mobile.util.StringFormatUtils;

/**
 * Created by woraw on 10/19/2017.
 */

public class User {
    private String username;
    private int userId;
    private String firstName;
    private String lastName;
    private int userAge;
    private char userGender;
    private int userHeght;
    private int userWeight;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public char getUserGender() {
        return userGender;
    }

    public void setUserGender(char userGender) {
        this.userGender = userGender;
    }

    public int getUserHeght() {
        return userHeght;
    }

    public void setUserHeght(int userHeght) {
        this.userHeght = userHeght;
    }

    public int getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(int userWeight) {
        this.userWeight = userWeight;
    }


}
