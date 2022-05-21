package com.example.facerecognition

import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.facerecognition.databinding.ActivityRecognizeBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias FaceListener = (face: UserMetadata) -> Unit

class RecognizeActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityRecognizeBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var userMove: java.util.ArrayList<UserMetadata>

    private lateinit var userLogin : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        viewBinding = ActivityRecognizeBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        userMove = ArrayList()

        userLogin = intent.getStringExtra(UserProfile.USER_LOGIN).toString()

        startCamera()

        // Set up the listeners for stop rec buttons
        viewBinding.recognizeStopButton.setOnClickListener { stopRecognition() }

        cameraExecutor = Executors.newSingleThreadExecutor()

        //5 sec for facial expressions recognition
        /*val thread = Thread(
            null, doBackgroundThreadProcessing,
            "Background"
        )
        thread.start()*/
    }

    private val doBackgroundThreadProcessing = Runnable {
        Thread.sleep(5000)
        stopRecognition()
    }

    private fun stopRecognition() {
        //Stop the activity
        val extraData = UserProfile(this.userLogin, this.userMove)
        intent.putExtra("UserMove", extraData)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()


            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor,
                        FaceAnalyzer{ face ->
                            userMove.add(face)
                        }
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer, imageCapture)

            } catch(exc: Exception) {
                Log.e("Log Info: ", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private class FaceAnalyzer(private val listener: FaceListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy) {

            println("Analyze log message")

            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // Pass image to an ML Kit Vision API

                val options = FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                    .build()

                // Get instance of face detector
                val detector = FaceDetection.getClient(options)

                val result = detector.process(image)
                    .addOnSuccessListener { faces ->
                        // Task completed successfully
                        println("Task completed successfully")

                        for (face in faces) {
                            val bounds = face.boundingBox
                            val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                            val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                            // If contour detection was enabled:
                            /*val faceContour = face.getContour(FaceContour.FACE)?.points
                            val leftEyeBrowTop = face.getContour(FaceContour.LEFT_EYEBROW_TOP)?.points
                            val leftEyeBrowBottom = face.getContour(FaceContour.LEFT_EYEBROW_BOTTOM)?.points
                            val rightEyeBrowTop = face.getContour(FaceContour.RIGHT_EYEBROW_TOP)?.points
                            val rightEyeBrowBottom = face.getContour(FaceContour.RIGHT_EYEBROW_BOTTOM)?.points
                            val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                            val rightEyeContour = face.getContour(FaceContour.RIGHT_EYE)?.points
                            val leftCheekCenter = face.getContour(FaceContour.LEFT_CHEEK)?.points
                            val rightCheekCenter = face.getContour(FaceContour.RIGHT_CHEEK)?.points
                            val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points
                            val upperLipTopContour = face.getContour(FaceContour.UPPER_LIP_TOP)?.points
                            val lowerLipBottomContour = face.getContour(FaceContour.LOWER_LIP_BOTTOM)?.points
                            val lowerLipTopContour = face.getContour(FaceContour.LOWER_LIP_TOP)?.points
                            val noseBridge = face.getContour(FaceContour.NOSE_BRIDGE)?.points
                            val noseBottom = face.getContour(FaceContour.NOSE_BOTTOM)?.points
*/

                            val userFace = UserMetadata(
                                face.getContour(FaceContour.FACE)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.LEFT_EYEBROW_TOP)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.LEFT_EYEBROW_BOTTOM)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.RIGHT_EYEBROW_TOP)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.RIGHT_EYEBROW_BOTTOM)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.LEFT_EYE)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.RIGHT_EYE)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.LEFT_CHEEK)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.RIGHT_CHEEK)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.UPPER_LIP_TOP)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.LOWER_LIP_BOTTOM)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.LOWER_LIP_TOP)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.NOSE_BRIDGE)?.points as ArrayList<PointF>?,
                                face.getContour(FaceContour.NOSE_BOTTOM)?.points as ArrayList<PointF>?
                            )

                            if (face.allContours != null)
                            {
                                val allContours = face.allContours
                                println("\n allContours:$allContours")
                            }

                            listener(userFace)

                        }
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        println("Task failed with an exception")
                    }
                    .addOnCompleteListener {
                        mediaImage?.close()
                        imageProxy.close() }


            }

            imageProxy.close()
            mediaImage?.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


}