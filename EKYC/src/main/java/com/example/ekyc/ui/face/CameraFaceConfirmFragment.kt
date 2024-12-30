package com.example.ekyc.ui.face

import android.content.res.Resources
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
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.CameraXManager
import com.example.ekyc.databinding.FragmentCameraFaceConfirmBinding
import kotlin.math.abs


internal class CameraFaceConfirmFragment : BaseDataBindingFragment<FragmentCameraFaceConfirmBinding, FaceConfirmViewModel>() {

    private var isFaceDetected = false
    lateinit var cameraXManager: CameraXManager
    lateinit var preview : PreviewView

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
//            Log.d("handleFaceDetected", "handleFaceDetected")
//            handleFaceDetected(face, boundingBox)
            Log.d("handleFaceDetected", "handleFaceDetected")

            // Kiểm tra nếu khuôn mặt chưa được xử lý
            if (!isFaceDetected) {
                isFaceDetected = true  // Đánh dấu rằng khuôn mặt đã được xử lý
                handleFaceDetected(face, boundingBox)

                // Đặt lại flag sau một khoảng thời gian (ví dụ: 1 giây)
                Handler(Looper.getMainLooper()).postDelayed({
                    isFaceDetected = false
                }, 10000)
            }
        }
    }
        private fun handleFaceDetected(face: com.google.mlkit.vision.face.Face, boundingBox: RectF) {
            val eulerAngleX = face.headEulerAngleX  // Góc quay theo trục X
            val eulerAngleY = face.headEulerAngleY  // Góc quay theo trục Y

            // Kiểm tra nếu khuôn mặt ở giữa
            if (handleFaceCenter(eulerAngleX, eulerAngleY, boundingBox)) {
                // Nếu khuôn mặt đã ở giữa, kiểm tra tiếp khuôn mặt bên trái
                if (handleFaceLeft(eulerAngleX, eulerAngleY, boundingBox)) {
                    // Nếu khuôn mặt ở bên trái, kiểm tra tiếp khuôn mặt bên phải
                    handleFaceRight(eulerAngleX, eulerAngleY, boundingBox)
                }
            }
        }

    // Hàm kiểm tra nếu khuôn mặt ở giữa
    private fun handleFaceCenter(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        if (isFaceCentered(eulerAngleX, eulerAngleY, boundingBox)) {
            mBinding.tvTextTop.text = getString(R.string.left_face)
            mBinding.vCircle1.visibility = View.VISIBLE
            Log.d("CameraFragment", "Face centered at: $boundingBox")
            return true
        } else {
            Toast.makeText(requireContext(), "Khuôn mặt đang không ở giữa, yêu cầu quét lại", Toast.LENGTH_SHORT).show()

            // Đợi 1 giây và thử lại mà không dừng camera
            Handler(Looper.getMainLooper()).postDelayed({
                handleFaceCenter(eulerAngleX, eulerAngleY, boundingBox)
            }, 1000) // Delay 1 giây
        }
        return false
    }

    // Hàm kiểm tra nếu khuôn mặt ở bên trái
    private fun handleFaceLeft(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        if (isFaceOnLeft(eulerAngleX, eulerAngleY, boundingBox)) {
            mBinding.vCircle2.visibility = View.VISIBLE
            mBinding.tvTextTop.text = getString(R.string.right_face)
            Log.d("CameraFragment", "Face on left at: $boundingBox")
            return true
        } else {
            Toast.makeText(requireContext(), "Khuôn mặt đang không ở bên trái, yêu cầu quét lại", Toast.LENGTH_SHORT).show()

            // Đợi 1 giây và thử lại mà không dừng camera
            Handler(Looper.getMainLooper()).postDelayed({
                handleFaceLeft(eulerAngleX, eulerAngleY, boundingBox)
            }, 1000) // Delay 1 giây
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

            Handler(Looper.getMainLooper()).postDelayed({
                handleFaceRight(eulerAngleX, eulerAngleY, boundingBox)
            }, 1000) // Delay 1 giây
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
        val leftThreshold = 0.3f

        // Kiểm tra góc quay và vị trí mặt bên trái
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val faceCenterX = (boundingBox.left + boundingBox.right) / 2

        val isLeftByAngles = eulerAngleY > -angleThreshold && eulerAngleY < angleThreshold
        val isLeftByPosition = faceCenterX < screenWidth / 2 - screenWidth * leftThreshold

        return isLeftByAngles && isLeftByPosition
    }

    private fun isFaceOnRight(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        val angleThreshold = 10
        val rightThreshold = 0.3f

        // Kiểm tra góc quay và vị trí mặt bên phải
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val faceCenterX = (boundingBox.left + boundingBox.right) / 2

        val isRightByAngles = eulerAngleY > -angleThreshold && eulerAngleY < angleThreshold
        val isRightByPosition = faceCenterX > screenWidth / 2 + screenWidth * rightThreshold

        return isRightByAngles && isRightByPosition
    }


    override fun onDestroyView() {
        super.onDestroyView()
        cameraXManager.stopCamera()
    }
}