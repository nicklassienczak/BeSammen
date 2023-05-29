package com.example.besammen;

public class userProfileData {

    //This is a model class for the user profile data

    public String userName, userUID, userDiagnose;
    public int userAge;

    public userProfileData() {

    }

    //Constructor to assign the parameters to the right values
    public userProfileData(String userName, String userUID, String userDiagnose, int userAge) {
        this.userName = userName;
        this.userUID = userUID;
        this.userDiagnose = userDiagnose;
        this.userAge = userAge;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserDiagnose() {
        return userDiagnose;
    }

    public void setUserDiagnose(String userDiagnose) {
        this.userDiagnose = userDiagnose;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }
}
