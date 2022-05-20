package com.example.facerecognition;

import java.util.ArrayList;

public class UserProfile {

    static final String USER_LOGIN = "userLogin";

    private String userLogin;
    private String personalData;
    private ArrayList<UserMetadata> userMove;


    public UserProfile(String userLogin) {
        this.userLogin = userLogin;
        this.personalData = "No data yet";
    }

    public String getPersonalData() {
        return personalData;
    }

    public void setPersonalData(String personalData) {
        this.personalData = personalData;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
