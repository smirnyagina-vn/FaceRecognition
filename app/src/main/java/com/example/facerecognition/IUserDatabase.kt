package com.example.facerecognition

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue

interface IUserDatabase {

    var DATABASE_URL: String
        get() = "https://faceauth-007-default-rtdb.europe-west1.firebasedatabase.app/"
        set(value) = TODO()
    var USERS_DIR: String
        get() = "Users"
        set(value) = TODO()

    fun setUserProfileToDB(userDatabase : DatabaseReference, userProfile: UserProfile) {
        userDatabase.child(USERS_DIR).child(userProfile.userLogin).setValue(userProfile)
    }

}