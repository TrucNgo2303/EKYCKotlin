package com.example.ekyc.ui.front

import android.os.Bundle
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.CameraXManager
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraFrontBinding
import com.example.ekyc.utils.extension.addFragment
import com.example.ekyc.utils.extension.addFragmentWithAnimation

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
            cameraXManager.takePicture { bitmap ->

                viewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]
                // Lưu ảnh vào ViewModel
                bitmap?.let {
                    viewModel.saveFrontImage(it)
                }
            }
            parentFragmentManager.addFragment(fragment = CameraConfirmFrontFragment.newInstance())
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