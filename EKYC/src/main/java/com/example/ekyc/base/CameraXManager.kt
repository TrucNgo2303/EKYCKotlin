package com.example.ekyc.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class CameraXManager(
    private val context: Context,
    private val previewView: PreviewView,
    private val lifecycleOwner: LifecycleOwner,
    private val useBackCamera: Boolean = true,
    private val useDetectFace: Boolean = false,
) {

    private var detector: FaceDetector? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    var processFaceDetect: ((Face, RectF) -> Unit)? = null

    var backCamera = useBackCamera

    init {
        startCamera()
        if (useDetectFace) {
            setupDetector()
        }
    }


    private fun setupDetector() {
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.30f)
            .build()

        detector = FaceDetection.getClient(realTimeOpts)
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
    }

    fun toggleFlash(torch: Boolean) {
        camera?.cameraControl?.enableTorch(torch)
    }

    // Method to restart the camera (bind use cases again)
    fun restartCamera(useBackCamera: Boolean = true) {
        backCamera = useBackCamera
        bindCameraUseCases()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCameraUseCases() {
        // Unbind previous use cases
        cameraProvider?.unbindAll()

        // Create Preview Use Case
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        // Create ImageCapture Use Case
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setJpegQuality(85)
            .setFlashMode(FLASH_MODE_OFF)
            .build()

        // Create ImageAnalysis Use Case
        imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    processImage(imageProxy)
                }
            }// Select camera (back-facing)
        val newLensFacing =
            if (backCamera) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
        val newCameraSelector = CameraSelector.Builder()
            .requireLensFacing(newLensFacing)
            .build()
        try {
            // Bind use cases to camera
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner, newCameraSelector, preview, imageCapture, imageAnalyzer
            )
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    fun takePicture(onBitmapCapture: (Bitmap?) -> Unit) {
        imageCapture?.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    // Chuyển đổi ImageProxy sang Bitmap
                    val bitmap = imageProxyToBitmap(imageProxy)
                    val finalBitmap = cropToPreviewViewSize(bitmap, previewView)
                    if (backCamera) {
                        onBitmapCapture(finalBitmap)
                    } else {
                        onBitmapCapture(finalBitmap)
                    }
                    // Xử lý ảnh Bitmap tại đây (hiển thị, xử lý, v.v.)

                    imageProxy.close() // Đừng quên đóng imageProxy sau khi sử dụng xong
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("TAG", "Capture failed: ${exception.message}", exception)
                }
            })
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        // Tạo bitmap từ mảng byte
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        // Lấy giá trị xoay từ ImageProxy
        val rotationDegrees = image.imageInfo.rotationDegrees

        // Xoay bitmap theo hướng rotationDegrees
        return if (backCamera) rotateBitmap(bitmap, rotationDegrees) else rotateAndFlipBitmap(
            bitmap,
            rotationDegrees
        )
    }

    private fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    private fun rotateAndFlipBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix().apply {
            // Xoay ảnh dựa trên độ xoay của máy ảnh
            when (degrees) {
                0 -> postRotate(0f)
                90 -> postRotate(90f)
                180 -> postRotate(180f)
                270 -> postRotate(270f)
            }
            // Lật ảnh theo chiều ngang (Mirror effect)
            postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun cropToPreviewViewSize(bitmap: Bitmap, previewView: PreviewView): Bitmap {
        val viewWidth = previewView.width
        val viewHeight = previewView.height

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        // Tính tỷ lệ khung hình của PreviewView và Bitmap
        val viewRatio = viewWidth.toFloat() / viewHeight.toFloat()
        val bitmapRatio = bitmapWidth.toFloat() / bitmapHeight.toFloat()// Cắt ảnh theo tỷ lệ của PreviewView
        return if (bitmapRatio > viewRatio) {
            // Cắt chiều rộng của ảnh để khớp với tỷ lệ của PreviewView
            val newWidth = (bitmapHeight * viewRatio).toInt()
            Bitmap.createBitmap(bitmap, (bitmapWidth - newWidth) / 2, 0, newWidth, bitmapHeight)
        } else {
            // Cắt chiều cao của ảnh để khớp với tỷ lệ của PreviewView
            val newHeight = (bitmapWidth / viewRatio).toInt()
            Bitmap.createBitmap(bitmap, 0, (bitmapHeight - newHeight) / 2, bitmapWidth, newHeight)
        }
    }

    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val imageRotation = imageProxy.imageInfo.rotationDegrees
            val inputImage = InputImage.fromMediaImage(mediaImage, imageRotation)

            detector?.let {
                it.process(inputImage)
                    .addOnSuccessListener { faces ->
                        if (faces.size > 0) {
                            val face = faces.first()
                            val boundingBox = face.boundingBox
                            val previewWidth = previewView.width.toFloat()
                            val previewHeight = previewView.height.toFloat()
                            val imageWidth = imageProxy.width.toFloat()
                            val imageHeight = imageProxy.height.toFloat()
                            val scaleX = previewWidth / imageWidth
                            val scaleY = previewHeight / imageHeight
                            val mappedBoundingBox = RectF(
                                boundingBox.left * scaleX,
                                boundingBox.top * scaleY,
                                boundingBox.right * scaleX,
                                boundingBox.bottom * scaleY
                            )
                            processFaceDetect?.invoke(face, mappedBoundingBox)
                        }

                    }
                    .addOnFailureListener { e ->
                        Log.e("FaceDetection", "Face detection failed", e)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }
        }
    }
}