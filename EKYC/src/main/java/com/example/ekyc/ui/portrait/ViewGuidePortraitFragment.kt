package com.example.ekyc.ui.portrait

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.BaseViewModel
import com.example.ekyc.databinding.FragmentViewGuidePortraitBinding
import com.example.ekyc.utils.extension.addFragment

internal class ViewGuidePortraitFragment : BaseDataBindingFragment<FragmentViewGuidePortraitBinding, ViewGuidePortraitViewModel>() {


    companion object {

        fun newInstance() =
            ViewGuidePortraitFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_view_guide_portrait

    override fun onBackFragmentPressed() {
        TODO("Not yet implemented")
    }

    override fun onLeftIconClick() {
        TODO("Not yet implemented")
    }

    override fun initialize() {
        mBinding.btnGotIt.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}