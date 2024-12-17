package com.example.ekyc.ui.face

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.databinding.FragmentCameraFaceConfirmBinding


internal class CameraFaceConfirmFragment : BaseDataBindingFragment<FragmentCameraFaceConfirmBinding, FaceConfirmViewModel>() {


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
        TODO("Not yet implemented")
    }

    override fun initialize() {
        TODO("Not yet implemented")
    }
}