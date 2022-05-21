package com.example.facerecognition

import android.util.Log
import com.google.firebase.database.DatabaseReference

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

    private fun getUserByLoginFromDB(userDatabase : DatabaseReference, userLogin : String) : UserProfile
    {
        var userProfile = UserProfile("")
        userDatabase.child(USERS_DIR).child(userLogin).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            userProfile = UserProfile(it.child(UserProfile.USER_LOGIN).value as UserProfile?)
            Log.i("Firebase user", "\nUser login:  ${userProfile.userLogin}")

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        return userProfile
    }

}