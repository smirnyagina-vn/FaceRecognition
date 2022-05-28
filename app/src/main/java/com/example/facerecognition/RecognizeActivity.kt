package com.example.facerecognition

import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.facerecognition.databinding.ActivityRecognizeBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
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
        val thread = Thread(
            null, doBackgroundThreadProcessing,
            "Background"
        )
        thread.start()
    }

    private val doBackgroundThreadProcessing = Runnable {
        Thread.sleep(6000)
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
                .setTargetResolution(Size(420, 888))
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

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            //.setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        // Get instance of face detector
        val detector = FaceDetection.getClient(options)


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

                /*val options = FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    //.setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                    .build()

                // Get instance of face detector
                val detector = FaceDetection.getClient(options)*/

                val result = detector.process(image)
                    .addOnSuccessListener { faces ->
                        // Task completed successfully
                        println("Task completed successfully")

                        for (face in faces) {
                            val bounds = face.boundingBox
                            val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                            val rotX = face.headEulerAngleX // Head is tilted sideways rotX degrees
                            val rotZ = face.headEulerAngleZ

                            val rotPos = PointF(rotX, rotY)
                            var smilingProbability = PointF(0.0F, 0.0F)
                            var eyeProbability = PointF(0.0F, 0.0F)
                            var leftEarPos = PointF(0.0F, 0.0F)
                            var leftCheekPos = PointF(0.0F, 0.0F)
                            var leftEyePos = PointF(0.0F, 0.0F)
                            var mouthLeftPos = PointF(0.0F, 0.0F)
                            var rightEarPos = PointF(0.0F, 0.0F)
                            var rightEyePos = PointF(0.0F, 0.0F)
                            var rightCheekPos = PointF(0.0F, 0.0F)
                            var mouthRightPos = PointF(0.0F, 0.0F)
                            var mouthBottomPos = PointF(0.0F, 0.0F)
                            var noseBasePos = PointF(0.0F, 0.0F)

                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                            // nose available):
                            val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                            leftEar?.let {
                                leftEarPos = leftEar.position
                            }

                            val leftCheek = face.getLandmark(FaceLandmark.LEFT_CHEEK)
                            leftCheek?.let {
                                leftCheekPos = leftCheek.position
                            }

                            val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
                            leftEye?.let {
                                leftEyePos = leftEye.position
                            }

                            val mouthLeft = face.getLandmark(FaceLandmark.MOUTH_LEFT)
                            mouthLeft?.let {
                                mouthLeftPos = mouthLeft.position
                            }

                            val rightEar = face.getLandmark(FaceLandmark.RIGHT_EAR)
                            rightEar?.let {
                                rightEarPos = rightEar.position
                            }

                            val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)
                            rightEye?.let {
                                rightEyePos = rightEye.position
                            }

                            val rightCheek = face.getLandmark(FaceLandmark.RIGHT_CHEEK)
                            rightCheek?.let {
                                rightCheekPos = rightCheek.position
                            }

                            val mouthRight = face.getLandmark(FaceLandmark.MOUTH_RIGHT)
                            mouthRight?.let {
                                mouthRightPos = mouthRight.position
                            }

                            val mouthBottom = face.getLandmark(FaceLandmark.MOUTH_BOTTOM)
                            mouthBottom?.let {
                                mouthBottomPos = mouthBottom.position
                            }

                            val noseBase = face.getLandmark(FaceLandmark.NOSE_BASE)
                            noseBase?.let {
                                noseBasePos = noseBase.position
                            }

                            // If classification was enabled:
                            if (face.smilingProbability != null) {
                                smilingProbability.set(face.smilingProbability!!, 0.0F)
                            }

                            if (face.rightEyeOpenProbability != null &&
                                    face.leftEyeOpenProbability != null){
                                eyeProbability.set(
                                    face.rightEyeOpenProbability!!,
                                    face.leftEyeOpenProbability!!
                                )
                            }


                            val userFace =
                                UserMetadata(
                                    smilingProbability,
                                    eyeProbability,
                                    rotPos,
                                    leftEarPos,
                                    leftCheekPos,
                                    leftEyePos,
                                    mouthLeftPos,
                                    rightEarPos,
                                    rightEyePos,
                                    rightCheekPos,
                                    mouthRightPos,
                                    mouthBottomPos,
                                    noseBasePos
                                )

                            // If contour detection was enabled
                            /*val userFace = UserMetadata(
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
                            )*/

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