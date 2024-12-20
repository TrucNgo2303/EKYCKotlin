package com.example.ekyc.ui.document

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentUnverifiedImageBinding
import com.example.ekyc.ui.back.CameraBackFragment
import com.example.ekyc.ui.front.CameraFrontFragment
import com.example.ekyc.ui.portrait.CameraPortraitFragment
import com.example.ekyc.utils.extension.addFragment

internal class UnverifiedImageFragment : BaseDataBindingFragment<FragmentUnverifiedImageBinding, UnverifiedViewModel>() {

    private lateinit var sdkViewModel: SDKMainViewModel
    private lateinit var viewModel: UnverifiedViewModel

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

        allImage()
        clickBtn()
    }
    private fun allImage(){
        // Truy cập ViewModel từ Activity
        sdkViewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]

        // Lấy ảnh mặt trước căn cước
        sdkViewModel.frontImage.observe(viewLifecycleOwner) { bitmap ->
            // Xử lý ảnh ở đây khi LiveData thay đổi
            if (bitmap != null) {
                // Sử dụng bitmap ở đây
                mBinding.imgFrontSide.setImageBitmap(bitmap)
            }
        }

        // Lấy ảnh mặt sau căn cước
        sdkViewModel.backImage.observe(viewLifecycleOwner) { bitmap ->
            // Xử lý ảnh ở đây khi LiveData thay đổi
            if (bitmap != null) {
                // Sử dụng bitmap ở đây
                mBinding.imgBackSide.setImageBitmap(bitmap)
            }
        }

        // Lấy ảnh chính giữa với số điện thoại
        sdkViewModel.portraitImage.observe(viewLifecycleOwner) { bitmap ->
            // Xử lý ảnh ở đây khi LiveData thay đổi
            if (bitmap != null) {
                // Sử dụng bitmap ở đây
                mBinding.imgWithPn.setImageBitmap(bitmap)
            }
        }
    }
    private fun clickBtn(){
        mBinding.btnContinueFail.setOnClickListener {
            activity?.finish()
        }
        mBinding.btnRetryAll.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraFrontFragment.newInstance())
        }
        mBinding.btnRetakePortrait.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraPortraitFragment.newInstance())
        }
    }
}