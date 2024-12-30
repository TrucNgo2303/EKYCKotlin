package com.example.ekyc.ui.document

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentUnverifiedImageBinding
import com.example.ekyc.ui.face.CameraFaceConfirmFragment
import com.example.ekyc.ui.front.CameraFrontFragment
import com.example.ekyc.utils.extension.addFragment

internal class UnverifiedImageFragment : BaseDataBindingFragment<FragmentUnverifiedImageBinding, UnverifiedViewModel>() {

    private lateinit var sdkViewModel: SDKMainViewModel

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
        // Truy cập ViewModel từ Activity
        sdkViewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]
        allImage()
        //Lấy response
        warningImage()
        clickBtn()
    }
    private fun allImage(){
        // Lấy ảnh mặt trước căn cước
//        sdkViewModel.frontImage.observe(viewLifecycleOwner) { bitmap ->
//            // Xử lý ảnh ở đây khi LiveData thay đổi
//            if (bitmap != null) {
//                // Sử dụng bitmap ở đây
//                mBinding.imgFrontSide.setImageBitmap(bitmap)
//            }
//        }
        sdkViewModel.frontImage?.let { bitmap ->
            mBinding.imgFrontSide.setImageBitmap(bitmap)
        }

        // Lấy ảnh mặt sau căn cước
        sdkViewModel.backImage?.let { bitmap ->
            mBinding.imgBackSide.setImageBitmap(bitmap)
        }

        sdkViewModel.portraitImage?.let { bitmap ->
            mBinding.imgWithPn.setImageBitmap(bitmap)
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
            parentFragmentManager.addFragment(fragment = CameraFaceConfirmFragment.newInstance())
        }
        mBinding.btnContinueTrue.setOnClickListener {
            parentFragmentManager.addFragment(fragment = RegisterInfoFragment.newInstance())
        }
    }

    private fun warningImage(){
        var gwMessFront = sdkViewModel.gwMessFront
        var gwMessBack = sdkViewModel.gwMessBack
        var gwMessPortrait = sdkViewModel.gwMessPortrait

        val isAnyFailed = gwMessFront.trim() != "Success" ||
                gwMessBack.trim() != "Success" ||
                gwMessPortrait.trim() != "Success"

        if (isAnyFailed) {
            // Nếu một trong ba giá trị không phải "Success"
            mBinding.icFailFrontSide.visibility = if (gwMessFront.trim() != "Success") View.VISIBLE else View.GONE
            mBinding.icFailBackSide.visibility = if (gwMessBack.trim() != "Success") View.VISIBLE else View.GONE
            mBinding.icFailPnSide.visibility = if (gwMessPortrait.trim() != "Success") View.VISIBLE else View.GONE

            mBinding.btnContinueFail.visibility = View.VISIBLE
            mBinding.btnRetryAll.visibility = View.VISIBLE
            mBinding.tvError.visibility = View.VISIBLE
            mBinding.btnContinueTrue.visibility = View.GONE

            mBinding.tvFrontSide.setTextColor(ContextCompat.getColor(requireContext(), if (gwMessFront.trim() != "Success") R.color.red else R.color.green))
            mBinding.tvBackSide.setTextColor(ContextCompat.getColor(requireContext(), if (gwMessBack.trim() != "Success") R.color.red else R.color.green))
            mBinding.tvWithPn.setTextColor(ContextCompat.getColor(requireContext(), if (gwMessPortrait.trim() != "Success") R.color.red else R.color.green))
        } else {
            // Nếu tất cả đều là "Success"
            mBinding.icGoodFrontSide.visibility = View.VISIBLE
            mBinding.icGoodBackSide.visibility = View.VISIBLE
            mBinding.icGoodPnSide.visibility = View.VISIBLE

            mBinding.btnContinueFail.visibility = View.GONE
            mBinding.btnRetryAll.visibility = View.GONE
            mBinding.tvError.visibility = View.GONE
            mBinding.btnContinueTrue.visibility = View.VISIBLE

            mBinding.tvFrontSide.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            mBinding.tvBackSide.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            mBinding.tvWithPn.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

    }
}