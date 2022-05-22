package com.example.facerecognition;

import android.graphics.PointF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class UserProfile implements Parcelable {

    static final String USER_LOGIN = "userLogin";

    private String userLogin;
    private ArrayList<UserMetadata> userMove;

    public UserProfile()
    {
        this.userLogin = "";
        this.userMove = new ArrayList<UserMetadata>();
    }

    public UserProfile(String userLogin) {
        this.userLogin = userLogin;
        this.userMove = new ArrayList<UserMetadata>();
    }

    public UserProfile(String userLogin, ArrayList<UserMetadata> userMove) {
        this.userLogin = userLogin;
        this.userMove = userMove;
    }

    public UserProfile(UserProfile userProfile) {
        this.userLogin = userProfile.userLogin;
        this.userMove = userProfile.userMove;
    }

    protected UserProfile(Parcel in) {
        userLogin = in.readString();
        userMove = in.createTypedArrayList(UserMetadata.CREATOR);
    }



    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public ArrayList<UserMetadata> getUserMove() {
        return userMove;
    }

    public void setUserMove(ArrayList<UserMetadata> userMove) {
        this.userMove = userMove;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userLogin);
        dest.writeTypedList(userMove);
    }
}
