package com.example.ekyc.ui.face

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.api.ApiService
import com.example.ekyc.api.RetrofitClient
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.CameraXManager
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraFaceConfirmBinding
import com.example.ekyc.ui.back.CameraConfirmBackFragment
import com.example.ekyc.ui.document.UnverifiedImageFragment
import com.example.ekyc.utils.extension.addFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


internal class CameraFaceConfirmFragment : BaseDataBindingFragment<FragmentCameraFaceConfirmBinding, FaceConfirmViewModel>() {

    private var isFaceDetected = false
    lateinit var cameraXManager: CameraXManager
    lateinit var preview : PreviewView
    private var currentStep = 0 // 0: center, 1: left, 2: right
    private var isProcessing = false
    lateinit var viewModel: SDKMainViewModel

    companion object {
        fun newInstance() =
            CameraFaceConfirmFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_camera_face_confirm

    override fun onBackFragmentPressed() {
        TODO("Not yet implemented")
    }

    override fun onLeftIconClick() {
        mBinding.btnClose.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initialize() {
        onLeftIconClick()

        preview = mBinding.previewView

        cameraXManager = CameraXManager(requireContext(),preview,this,false,true)

        // Set up face detection callback
        cameraXManager.processFaceDetect = { face, boundingBox ->
            Log.d("CameraFragment", "handleFaceDetected")

            if (!isFaceDetected && !isProcessing) {
                isFaceDetected = true
                isProcessing = true

                when (currentStep) {
                    0 -> {
                        if (handleFaceCenter(face.headEulerAngleX, face.headEulerAngleY, boundingBox)) {
                            cameraXManager.takePicture { bitmap ->
                                viewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]
                                bitmap?.let { capturedBitmap ->
                                    viewModel.faceImage = capturedBitmap
                                }

                            }
                            currentStep = 1
                        }
                    }
                    1 -> {
                        if (handleFaceLeft(face.headEulerAngleX, face.headEulerAngleY, boundingBox)) {
                            currentStep = 2
                        }
                    }
                    2 -> {
                        if (handleFaceRight(face.headEulerAngleX, face.headEulerAngleY, boundingBox)) {
                            // Kết thúc processFaceDetect
                            cameraXManager.processFaceDetect = null
                            cameraXManager.stopCamera()

                            callAPI(
                                onApiSuccess = { gwMessage ->
                                    // Xử lý sau khi API thành công, ví dụ chuyển fragment
                                    parentFragmentManager.addFragment(fragment = ImageFaceFragment.newInstance())
                                    // Bạn có thể truy cập gwMessage tại đây
                                },
                                onApiError = { errorMessage ->
                                    // Xử lý khi có lỗi xảy ra, ví dụ: hiển thị thông báo lỗi
                                    Toast.makeText(context, "Lỗi: $errorMessage", Toast.LENGTH_SHORT).show()
                                }
                            )

                            // Chuyển sang ImageFaceFragment
                            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.frameMain, ImageFaceFragment())
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }

                    }
                }

                isProcessing = false

                // Reset isFaceDetected sau 1 khoảng thời gian
                Handler(Looper.getMainLooper()).postDelayed({
                    isFaceDetected = false
                }, 1000)
            }

        }

    }
    private fun handleFaceCenter(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        if (isFaceCentered(eulerAngleX, eulerAngleY, boundingBox)) {
            mBinding.tvTextTop.text = getString(R.string.left_face)
            mBinding.vCircle1.visibility = View.VISIBLE
            Log.d("CameraFragment", "Face centered at: $boundingBox")
            return true
        } else {
            Toast.makeText(requireContext(), "Khuôn mặt đang không ở giữa, yêu cầu quét lại", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    // Hàm kiểm tra nếu khuôn mặt ở bên trái
    private fun handleFaceLeft(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        Log.d("CameraFragment","${isFaceOnLeft(eulerAngleX, eulerAngleY, boundingBox)}")
        if (isFaceOnLeft(eulerAngleX, eulerAngleY, boundingBox)) {
            mBinding.vCircle2.visibility = View.VISIBLE
            mBinding.tvTextTop.text = getString(R.string.right_face)
            Log.d("CameraFragment", "Face on left at: $boundingBox")
            return true
        } else {
            Toast.makeText(requireContext(), "Khuôn mặt đang không ở bên trái, yêu cầu quét lại", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    // Hàm kiểm tra nếu khuôn mặt ở bên phải
    private fun handleFaceRight(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        if (isFaceOnRight(eulerAngleX, eulerAngleY, boundingBox)) {
            mBinding.vCircle3.visibility = View.VISIBLE
            mBinding.tvTextTop.text = getString(R.string.right_face)
            Log.d("CameraFragment", "Face on right at: $boundingBox")
            return true
        } else {
            Toast.makeText(requireContext(), "Khuôn mặt đang không ở bên phải, yêu cầu quét lại", Toast.LENGTH_SHORT).show()
        }
        return false
    }


    private fun isFaceCentered(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        val angleThreshold = 10
        val centerThreshold = 0.3f

        // Kiểm tra góc quay và vị trí mặt
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val faceCenterX = (boundingBox.left + boundingBox.right) / 2
        val faceCenterY = (boundingBox.top + boundingBox.bottom) / 2

        val isCenteredByAngles = abs(eulerAngleX) < angleThreshold && abs(eulerAngleY) < angleThreshold
        val isCenteredByPosition = abs(faceCenterX - screenWidth / 2) < screenWidth * centerThreshold &&
                abs(faceCenterY - screenHeight / 2) < screenHeight * centerThreshold

        return isCenteredByAngles && isCenteredByPosition
    }


    private fun isFaceOnLeft(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        val angleThreshold = 10
        val leftThreshold = 0.15f

        // Kiểm tra góc quay và vị trí mặt bên trái
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val faceCenterX = (boundingBox.left + boundingBox.right) / 2

        val isLeftByAngles = eulerAngleY > angleThreshold
        val isLeftByPosition = faceCenterX < screenWidth / 2 - screenWidth * leftThreshold

        Log.d("CameraFragment", """
        eulerAngleY: $eulerAngleY
        faceCenterX: $faceCenterX
        screenWidth: $screenWidth
        isLeftByAngles: $isLeftByAngles
        isLeftByPosition: $isLeftByPosition
    """.trimIndent())

        return isLeftByAngles && isLeftByPosition
    }

    private fun isFaceOnRight(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        val angleThreshold = 10
        val rightThreshold = 0.15f

        // Kiểm tra góc quay và vị trí mặt bên phải
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val faceCenterX = (boundingBox.left + boundingBox.right) / 2

        val isRightByAngles = eulerAngleY < -angleThreshold
        val isRightByPosition = faceCenterX < screenWidth / 2 + screenWidth * rightThreshold

        Log.d("CameraFragment", """
        eulerAngleY: $eulerAngleY
        faceCenterX: $faceCenterX
        screenWidth: $screenWidth
        isRightByAngles: $isRightByAngles
        isRightByPosition: $isRightByPosition
    """.trimIndent())


        return isRightByAngles && isRightByPosition
    }

    private fun callAPI(onApiSuccess: (String) -> Unit, onApiError: (String) -> Unit) {
        // Lấy thời gian hiện tại
        val currentTimestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(
            Date()
        )
        // Chuyển currentTimestamp thành RequestBody
        val transId = currentTimestamp.toRequestBody("text/plain".toMediaTypeOrNull())

        // Tiến hành gọi API với transId
        viewModel.faceImage?.let { bitmap ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            val requestFile = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image_selfie_path", "$transId.jpg", requestFile)

            //Tạo token
            //RetrofitClient.setToken("818f47f73f024b26abb11b78e05e1349")

            // Tạo Retrofit API service
            val apiService = RetrofitClient.apiClient.create(ApiService::class.java)

            val pathImage = viewModel.pathImage.toRequestBody("text/plain".toMediaTypeOrNull())

            Log.d("Api","Path Image: ${viewModel.pathImage}")

            // Gửi yêu cầu API với các tham số bổ sung
            apiService.uploadFace(
                transId,
                pathImage,
                multipartBody
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val gwMessage = response.gw_message
                    viewModel.gwMessFace = gwMessage
                    Log.d("Api", "API Success: ${viewModel.gwMessFace}")
                    Log.d("Api", "API Success: $response")
                    onApiSuccess(gwMessage)
                }, { throwable ->
                    Log.d("Api", "Error: ${throwable.message}")
                    onApiError(throwable.message ?: "Unknown error")
                })
        } ?: run {
            Log.d("Api", "Image not available")
            onApiError("Image not available") // Gọi callback lỗi nếu không có hình ảnh
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        cameraXManager.stopCamera()
        Log.d("TAG", "onDestroyViewxxx")
    }
}