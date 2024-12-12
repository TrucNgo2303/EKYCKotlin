package com.example.ekyc.ui.face

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.databinding.FragmentCameraStraightFaceBinding

internal class CameraStraightFaceFragment : BaseDataBindingFragment<FragmentCameraStraightFaceBinding, CameraStraightViewModel>() {

    override fun layoutResId(): Int = R.layout.fragment_camera_straight_face

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

    }


    companion object {
        fun newInstance() =
            CameraStraightFaceFragment().apply {
                arguments = Bundle()
            }
    }
}