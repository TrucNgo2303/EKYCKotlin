package com.example.ekyc.ui.front

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.camera.view.PreviewView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.CameraXManager
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraFrontBinding
import com.example.ekyc.ui.main.EKYCMainActivity
import com.example.ekyc.utils.extension.addFragment

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

                Toast.makeText(requireContext(),"Ảnh đã được chụp",Toast.LENGTH_SHORT).show()
                // Lưu ảnh vào ViewModel
                bitmap?.let {
                    viewModel.saveFrontImage(it)
                }
            }
            parentFragmentManager.addFragment(fragment = CameraConfirmFrontFragment.newInstance())
        }

        //Click View Guide
        mBinding.btnViewGuide.setOnClickListener {
            parentFragmentManager.addFragment(fragment = ViewGuideFrontFragment.newInstance())
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
//    private fun saveImageAfterCapture(bitmap: Bitmap) {
//        viewModel.saveFrontImage(bitmap)
//    }
}