package com.example.ekyc.ui.front

import android.os.Bundle
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.CameraXManager
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraFrontBinding
import com.example.ekyc.utils.extension.addFragment
import com.example.ekyc.utils.extension.addFragmentWithAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class CameraFrontFragment :BaseDataBindingFragment<FragmentCameraFrontBinding, CameraFrontViewModel>() {

    lateinit var cameraXManager: CameraXManager
    lateinit var viewModel: SDKMainViewModel
    lateinit var preview: PreviewView

    override fun layoutResId(): Int = R.layout.fragment_camera_front
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
        //Khởi tạo CameraXManager
        cameraXManager = CameraXManager(requireContext(),preview,this,true,false)

        mBinding.btnCamera.setOnClickListener {
//            cameraXManager.takePicture { bitmap ->
//                viewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]
//                bitmap?.let { capturedBitmap ->
//                    viewModel.frontImage = capturedBitmap
//                    Log.d("Bitmap", "Bitmap saved successfully: ${viewModel.frontImage}")
//
//                    // Chuyển đến fragment xác nhận sau khi lưu ảnh thành công
//                    parentFragmentManager.addFragment(fragment = CameraConfirmFrontFragment.newInstance())
//                }
//
//            }
            cameraXManager.takePicture { bitmap ->
                try {
                    viewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]
                    bitmap?.let { capturedBitmap ->
                        // Sử dụng coroutine để xử lý bitmap ở luồng IO
                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                viewModel.frontImage = capturedBitmap
                                Log.d("Bitmap", "Bitmap saved successfully: ${viewModel.frontImage}")

                                // Chuyển đến fragment xác nhận trên luồng chính
                                withContext(Dispatchers.Main) {
                                    parentFragmentManager.addFragment(fragment = CameraConfirmFrontFragment.newInstance())
                                }
                            } catch (e: Exception) {
                                Log.e("Bitmap", "Error saving bitmap: ${e.message}")
                            }
                        }
                    } ?: run {
                        Log.e("CameraX", "Bitmap is null")
                    }
                } catch (e: Exception) {
                    Log.e("CameraX", "Error capturing image: ${e.message}")
                }
            }

        }

        //Click View Guide
        mBinding.btnViewGuide.setOnClickListener {
            parentFragmentManager.addFragmentWithAnimation(fragment = ViewGuideFrontFragment.newInstance())
        }

    }


    companion object {
        fun newInstance() =
            CameraFrontFragment().apply {
                arguments = Bundle()
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        cameraXManager.stopCamera()
    }


}