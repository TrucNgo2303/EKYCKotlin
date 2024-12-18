package com.example.ekyc.ui.back

import android.os.Bundle
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
import com.example.ekyc.databinding.FragmentCameraBackBinding
import com.example.ekyc.utils.extension.addFragment


internal class CameraBackFragment : BaseDataBindingFragment<FragmentCameraBackBinding, CameraBackViewModel>() {

    lateinit var cameraXManager: CameraXManager
    lateinit var viewModel: SDKMainViewModel
    lateinit var preview: PreviewView


    companion object {

        fun newInstance() =
            CameraBackFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_camera_back

    override fun onBackFragmentPressed() {

    }

    override fun onLeftIconClick() {
        mBinding.btnClose.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initialize() {
        onLeftIconClick()
        mBinding.btnViewGuide.setOnClickListener {
            parentFragmentManager.addFragment(fragment = ViewGuideBackFragment.newInstance())
        }

        preview = mBinding.previewView
        //Khởi tạo CameraXManager
        cameraXManager = CameraXManager(requireContext(),preview,this,true,false)


        mBinding.btnCamera.setOnClickListener {
            cameraXManager.takePicture { bitmap ->

                viewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]

                Toast.makeText(requireContext(),"Ảnh đã được chụp", Toast.LENGTH_SHORT).show()
                // Lưu ảnh vào ViewModel
                bitmap?.let {
                    viewModel.saveBackImage(it)
                }
            }
            parentFragmentManager.addFragment(fragment = CameraConfirmBackFragment.newInstance())
        }
    }
}