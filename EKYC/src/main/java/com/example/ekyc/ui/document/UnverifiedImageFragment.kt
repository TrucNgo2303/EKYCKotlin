package com.example.ekyc.ui.document

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.databinding.FragmentUnverifiedImageBinding
import com.example.ekyc.ui.back.CameraBackFragment
import com.example.ekyc.ui.front.CameraFrontFragment
import com.example.ekyc.ui.portrait.CameraPortraitFragment
import com.example.ekyc.utils.extension.addFragment

internal class UnverifiedImageFragment : BaseDataBindingFragment<FragmentUnverifiedImageBinding, UnverifiedViewModel>() {


    companion object {
        fun newInstance() =
            UnverifiedImageFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_unverified_image

    override fun onBackFragmentPressed() {
        TODO("Not yet implemented")
    }

    override fun onLeftIconClick() {
        mBinding.imgArrowBack.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraFrontFragment.newInstance())
        }
    }

    override fun initialize() {
        onLeftIconClick()
        mBinding.btnContinue.setOnClickListener {
            activity?.finish()
        }
        mBinding.btnRetryAll.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraFrontFragment.newInstance())
        }
        mBinding.btnRetakeFront.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraFrontFragment.newInstance())
        }
        mBinding.btnRetakeBack.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraBackFragment.newInstance())
        }
        mBinding.btnRetakePortrait.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraPortraitFragment.newInstance())
        }
        mBinding.btnRetakeWithPn.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraPortraitFragment.newInstance())
        }
    }
}