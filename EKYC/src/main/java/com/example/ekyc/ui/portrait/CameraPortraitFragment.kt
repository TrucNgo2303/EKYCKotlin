package com.example.ekyc.ui.portrait

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.CameraXManager
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraPortraitBinding
import com.example.ekyc.ui.front.CameraConfirmFrontFragment
import com.example.ekyc.ui.front.ViewGuideFrontFragment
import com.example.ekyc.utils.extension.addFragment

internal class CameraPortraitFragment : BaseDataBindingFragment<FragmentCameraPortraitBinding, CameraPortraitViewModel>() {

    lateinit var cameraXManager: CameraXManager
    lateinit var viewModel: SDKMainViewModel
    lateinit var preview: PreviewView

    companion object {
        fun newInstance() =
            CameraPortraitFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_camera_portrait

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
        //Khởi tạo CameraXManager
        cameraXManager = CameraXManager(requireContext(),preview,this,true,false)

        mBinding.btnCamera.setOnClickListener {
            cameraXManager.takePicture { bitmap ->

                viewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]
                bitmap?.let { capturedBitmap ->
                    viewModel.portraitImage = capturedBitmap
                    Log.d("Bitmap", "Bitmap saved successfully: ${viewModel.frontImage}")

                    // Chuyển đến fragment xác nhận sau khi lưu ảnh thành công
                    parentFragmentManager.addFragment(fragment = CameraConfirmPortraitFragment.newInstance())
                }

            }
        }

        //Click View Guide
        mBinding.btnViewGuide.setOnClickListener {
            parentFragmentManager.addFragment(fragment = ViewGuidePortraitFragment.newInstance())
        }
    }
}