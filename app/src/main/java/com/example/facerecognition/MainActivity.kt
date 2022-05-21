package com.example.facerecognition

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.facerecognition.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


const val DATABASE_URL = "https://faceauth-007-default-rtdb.europe-west1.firebasedatabase.app/"
const val USER_DIR = "Users"

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var userDatabase: DatabaseReference

    private val registration : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {

        userDatabase = Firebase.database(DATABASE_URL).reference

        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            // Set up the listeners for start rec button
            viewBinding.authorizationButton.setOnClickListener { startRecognition() }
            viewBinding.registrationButton.setOnClickListener { startRecognition() }

        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

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

    private fun setUserProfileToDB(userProfile: UserProfile)
    {
        userDatabase.child(USER_DIR).child(userProfile.userLogin).setValue(userProfile)
    }


    private fun getUserByLoginFromDB(userLogin : String)
    {
        userDatabase.child(USER_DIR).child(userLogin).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val userProfile = UserProfile(it.child(UserProfile.USER_LOGIN).value as String?)
            Log.i("Firebase user", "\nUser login:  ${userProfile.userLogin}")

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun startRecognition() {
        val login = getUserLogin()
        activityLauncher.launch(login)
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

    private val activityLauncher = registerForActivityResult(RecognizeActivityContract()) {
            result ->
        if (result != null) {
            if (this.registration)
                setUserProfileToDB(result)
            else
                userAuthorization(result)
        }

    }

    private fun userAuthorization(userProfile: UserProfile)
    {

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