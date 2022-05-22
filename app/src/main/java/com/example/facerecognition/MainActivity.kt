package com.example.facerecognition

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.facerecognition.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

typealias CustomListener = (profile: UserProfile) -> Unit


class MainActivity : AppCompatActivity(), IUserDatabase {

    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var userDatabase: DatabaseReference

    private var registrationMode : Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {

        userDatabase = Firebase.database(DATABASE_URL).reference

        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            // Set up the listeners for start rec button
            viewBinding.authorizationButton.setOnClickListener { startRecognitionForAuthorization() }
            viewBinding.registrationButton.setOnClickListener { startRecognitionForRegistration() }

        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

    }


    private fun startRecognitionForAuthorization() {
        this.registrationMode = false
        val login = getUserLogin()
        activityLauncher.launch(login)
    }


    private fun startRecognitionForRegistration()
    {
        this.registrationMode = true
        val login = getUserLogin()
        activityLauncher.launch(login)
    }


    private val activityLauncher = registerForActivityResult(RecognizeActivityContract()) {
            result ->
        if (result != null) {
            if (this.registrationMode)
                setUserProfileToDB(this.userDatabase, result)
            else {
                //var test = result.userMove[0].allFaceContours
                getUserByLoginFromDB(this.userDatabase, result.userLogin, result)
            }
        }
    }


    class RecognizeActivityContract : ActivityResultContract<String, UserProfile?>() {

        override fun createIntent(context: Context, input: String?): Intent {
            return Intent(context, RecognizeActivity::class.java)
                .putExtra(UserProfile.USER_LOGIN, input)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): UserProfile? = when {
            resultCode != Activity.RESULT_OK -> null
            //else -> intent?.getSerializableExtra("UserMove") as UserProfile?
            else -> intent?.getParcelableExtra("UserMove") as UserProfile?
        }

    }



    fun getUserByLoginFromDB(userDatabase : DatabaseReference, userLogin : String, userProfile : UserProfile)
    {
        userDatabase.child(USERS_DIR).child(userLogin).get().addOnSuccessListener {
            Log.i("Firebase", "Got value ${it.value}")
            var userPattern = it.getValue<UserProfile>()!!
            Log.i("Firebase", "User Pattern $userPattern")
            if (userPattern != null) {
                userAuthorization(userPattern, userProfile)
            }

        }.addOnFailureListener{
            Log.e("Firebase", "Error getting data", it)
        }
    }

    private fun userAuthorization(userPattern: UserProfile, userProfile: UserProfile)
    {
        //Comparing 2 profiles with cosine
        val result = comparingProfiles(userPattern, userProfile)

        if (result)
            Toast.makeText(applicationContext, "Авторизация успешна", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(applicationContext, "Авторизация провалена", Toast.LENGTH_SHORT).show()

    }


    private fun comparingProfiles(first : UserProfile, second : UserProfile) : Boolean
    {


        return true
    }

    private fun cosineSimilarity()
    {

    }


    private fun getUserLogin() : String
    {
        val editTextHello = findViewById<EditText>(R.id.login_edit_text)
        val login = editTextHello.text.toString()
        Log.i("EditText: ", "User login: $login")
        return if (login == "")
            "No login"
        else login
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private const val TAG = "FaceRecognitionApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

}