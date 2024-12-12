package com.example.ekyc.ui.face

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.view.PreviewView
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.CameraXManager
import com.example.ekyc.databinding.FragmentCameraWholeFaceBinding

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

        cameraXManager = CameraXManager(requireContext(),preview,this,true,true)

        cameraXManager.takePicture {  }


    }
}