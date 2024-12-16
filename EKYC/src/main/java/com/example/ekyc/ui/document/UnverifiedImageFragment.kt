package com.example.ekyc.ui.document

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.databinding.FragmentUnverifiedImageBinding

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
        TODO("Not yet implemented")
    }

    override fun initialize() {
        TODO("Not yet implemented")
    }
}