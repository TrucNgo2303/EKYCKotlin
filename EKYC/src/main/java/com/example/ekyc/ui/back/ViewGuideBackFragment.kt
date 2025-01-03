package com.example.ekyc.ui.back

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.databinding.FragmentViewGuideBackBinding
import com.example.ekyc.utils.extension.addFragment

internal class ViewGuideBackFragment : BaseDataBindingFragment<FragmentViewGuideBackBinding, ViewGuideBackViewModel>() {


    companion object {

        fun newInstance() =
            ViewGuideBackFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_view_guide_back

    override fun onBackFragmentPressed() {

    }

    override fun onLeftIconClick() {

    }

    override fun initialize() {
        mBinding.btnGotIt.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}