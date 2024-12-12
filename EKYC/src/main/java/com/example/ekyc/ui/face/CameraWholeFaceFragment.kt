package com.example.ekyc.ui.face

import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import androidx.camera.view.PreviewView
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.CameraXManager
import com.example.ekyc.databinding.FragmentCameraWholeFaceBinding
import com.example.ekyc.utils.extension.addFragment

internal class CameraWholeFaceFragment : BaseDataBindingFragment<FragmentCameraWholeFaceBinding, CameraWholeViewModel>() {

    lateinit var cameraXManager: CameraXManager
    lateinit var preview : PreviewView

    companion object {
        fun newInstance() =
            CameraWholeFaceFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int =R.layout.fragment_camera_whole_face

    override fun onBackFragmentPressed() {

    }

    override fun onLeftIconClick() {
        mBinding.btnClose.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initialize() {
        onLeftIconClick()

        preview = mBinding.previewView

        cameraXManager = CameraXManager(requireContext(),preview,this,true,true)

        // Set up face detection callback
        cameraXManager.processFaceDetect = { face, boundingBox ->
            handleFaceDetected(face, boundingBox)
        }
    }
    private fun handleFaceDetected(face: com.google.mlkit.vision.face.Face, boundingBox: RectF) {
        // Xác định trung tâm khuôn mặt
        val centerX = boundingBox.centerX()
        val centerY = boundingBox.centerY()

        // Log thông tin khuôn mặt
        Log.d("CameraFragment", "Detected face at: $boundingBox")

        // Kiểm tra nếu quét đúng khuôn mặt (bạn có thể thêm điều kiện khác nếu cần)
        if (isFaceCentered(centerX, centerY, boundingBox)) {
            parentFragmentManager.addFragment(fragment = CameraStraightFaceFragment.newInstance())
            Log.d("CameraFragment", "Navigating to CameraRightFaceFragment")
        }
    }

    // Hàm kiểm tra khuôn mặt có ở giữa hay không
    private fun isFaceCentered(centerX: Float, centerY: Float, boundingBox: RectF): Boolean {
        // Điều kiện mẫu để xác định khuôn mặt nằm ở giữa (có thể điều chỉnh)
        val boxWidth = boundingBox.width()
        val boxHeight = boundingBox.height()
        return centerX > boxWidth * 0.3f && centerX < boxWidth * 0.7f &&
                centerY > boxHeight * 0.3f && centerY < boxHeight * 0.7f
    }
    override fun onDestroyView() {
        super.onDestroyView()
        cameraXManager.stopCamera()
    }
}