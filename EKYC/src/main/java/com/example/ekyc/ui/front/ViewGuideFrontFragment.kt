package com.example.ekyc.ui.front

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.databinding.FragmentViewGuideFrontBinding
import com.example.ekyc.ui.back.CameraBackFragment
import com.example.ekyc.utils.extension.addFragment

internal class ViewGuideFrontFragment : BaseDataBindingFragment<FragmentViewGuideFrontBinding, ViewGuideFrontViewModel>() {


    override fun layoutResId(): Int = R.layout.fragment_view_guide_front

    override fun onBackFragmentPressed() {

    }

    override fun onLeftIconClick() {

    }

    override fun initialize() {
        mBinding.btnGotIt.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraFrontFragment.newInstance())
        }
    }


    companion object {

        fun newInstance() =
            ViewGuideFrontFragment().apply {
                arguments = Bundle()
            }
    }
}