package com.example.facerecognition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

    private lateinit var userMetadata: UserMetadata

    override fun onCreate(savedInstanceState: Bundle?) {

        userDatabase = Firebase.database(DATABASE_URL).reference

        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            // Set up the listeners for start rec buttons
            viewBinding.authorizationButton.setOnClickListener { startRecognition() }
            viewBinding.registrationButton.setOnClickListener { startRecognition() }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

    }

    private fun testDatabase()
    {
        val testUser = UserMetadata("login1")

        userDatabase.child(USER_DIR).child(testUser.userLogin).setValue(testUser)

        getUserByLoginFromDB(testUser.userLogin)
    }

    private fun getUserByLoginFromDB(userLogin : String)
    {
        userDatabase.child(USER_DIR).child(userLogin).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            userMetadata = UserMetadata(it.child(UserMetadata.USER_LOGIN).value as String?)
            Log.i("Firebase user", "\nUser login:  ${userMetadata.userLogin}")

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewBinding.authorizationButton.setOnClickListener { startRecognition() }
                viewBinding.registrationButton.setOnClickListener { startRecognition() }
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startRecognition() {
        val intent = Intent(this, RecognizeActivity::class.java)
        startActivity(intent)
    }


    companion object {
        private const val TAG = "CameraXApp"
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