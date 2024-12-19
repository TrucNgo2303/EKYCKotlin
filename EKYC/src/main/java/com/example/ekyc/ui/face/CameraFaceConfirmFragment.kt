package com.example.ekyc.ui.face

import android.content.res.Resources
import android.graphics.RectF
import android.os.Bundle
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


internal class CameraFaceConfirmFragment : BaseDataBindingFragment<FragmentCameraFaceConfirmBinding, FaceConfirmViewModel>() {

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
            Log.d("handleFaceDetected", "handleFaceDetected")
            handleFaceDetected(face, boundingBox)
        }
    }
    private fun handleFaceDetected(face: com.google.mlkit.vision.face.Face, boundingBox: RectF) {
        val eulerAngleX = face.headEulerAngleX  // Góc quay theo trục X
        val eulerAngleY = face.headEulerAngleY  // Góc quay theo trục Y

        // Log thông tin khuôn mặt
        Log.d("CameraFragment", "Detected face at: $boundingBox")

        // Kiểm tra vị trí của mặt
        if(isFaceCentered(eulerAngleX,eulerAngleY,boundingBox)){
            mBinding.tvTextTop.text = getString(R.string.face_straight)
            mBinding.vCircle1.visibility = View.VISIBLE
            if(isFaceOnLeft(eulerAngleX,eulerAngleY,boundingBox)){
                mBinding.vCircle2.visibility = View.VISIBLE
                mBinding.tvTextTop.text = getString(R.string.left_face)
                if(isFaceOnRight(eulerAngleX,eulerAngleY,boundingBox)){
                    mBinding.vCircle3.visibility = View.VISIBLE
                    mBinding.tvTextTop.text = getString(R.string.right_face)
                }else{
                    Toast.makeText(requireContext(),"Khuôn mặt đang không ở bên phải, yêu cầu quét lại",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(),"Khuôn mặt đang không ở bên trái, yêu cầu quét lại",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(),"Khuôn mặt đang không ở giữa, yêu cầu quét lại",Toast.LENGTH_SHORT).show()
        }


    }

    private fun isFaceCentered(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        val angleThreshold = 10  // Ngưỡng góc quay
        val centerThreshold = 0.2f  // Ngưỡng tỷ lệ khuôn mặt ở giữa màn hình

        // Kiểm tra góc quay và vị trí mặt
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val faceCenterX = (boundingBox.left + boundingBox.right) / 2
        val faceCenterY = (boundingBox.top + boundingBox.bottom) / 2

        val isCenteredByAngles = Math.abs(eulerAngleX) < angleThreshold && Math.abs(eulerAngleY) < angleThreshold
        val isCenteredByPosition = Math.abs(faceCenterX - screenWidth / 2) < screenWidth * centerThreshold &&
                Math.abs(faceCenterY - screenHeight / 2) < screenHeight * centerThreshold

        return isCenteredByAngles && isCenteredByPosition
    }

    private fun isFaceOnLeft(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        val angleThreshold = 10  // Ngưỡng góc quay
        val leftThreshold = 0.2f  // Ngưỡng tỷ lệ khuôn mặt bên trái màn hình

        // Kiểm tra góc quay và vị trí mặt bên trái
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val faceCenterX = (boundingBox.left + boundingBox.right) / 2

        val isLeftByAngles = eulerAngleY > -angleThreshold && eulerAngleY < angleThreshold
        val isLeftByPosition = faceCenterX < screenWidth / 2 - screenWidth * leftThreshold

        return isLeftByAngles && isLeftByPosition
    }

    private fun isFaceOnRight(eulerAngleX: Float, eulerAngleY: Float, boundingBox: RectF): Boolean {
        val angleThreshold = 10  // Ngưỡng góc quay
        val rightThreshold = 0.2f  // Ngưỡng tỷ lệ khuôn mặt bên phải màn hình

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