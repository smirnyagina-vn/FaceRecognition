package com.example.facerecognition

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import com.example.facerecognition.databinding.ActivityRecognizeBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class RecognizeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityRecognizeBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRecognizeBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        startCamera()

        // Set up the listeners for stop rec buttons
        viewBinding.recognizeStopButton.setOnClickListener { stopRecognition() }

        cameraExecutor = Executors.newSingleThreadExecutor()

        //5 sec for facial expressions recognition
        val thread = Thread(
            null, doBackgroundThreadProcessing,
            "Background"
        )
        thread.start()
    }

    private val doBackgroundThreadProcessing = Runnable {
        Thread.sleep(5000)
        stopRecognition()
    }

    private fun stopRecognition() {
        //Stop the activity
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
                        FaceAnalyzer()
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

    private class FaceAnalyzer : ImageAnalysis.Analyzer {

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

                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                            // nose available):
                            val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                            leftEar?.let {
                                val leftEarPos = leftEar.position
                                println("\n leftEarPos:$leftEarPos")
                            }

                            // If contour detection was enabled:
                            val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                            val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                            // If classification was enabled:
                            if (face.smilingProbability != null) {
                                val smileProb = face.smilingProbability
                                println("\n smileProb:$smileProb")
                            }
                            if (face.rightEyeOpenProbability != null) {
                                val rightEyeOpenProb = face.rightEyeOpenProbability
                                println("\n rightEyeOpenProb:$rightEyeOpenProb")
                            }

                            // If face tracking was enabled:
                            if (face.trackingId != null) {
                                val id = face.trackingId
                            }

                            if (face.allContours != null)
                            {
                                val allContours = face.allContours
                                println("\n allContours:$allContours")
                            }
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