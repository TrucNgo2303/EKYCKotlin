package com.example.ekyc.ui.document

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentUnverifiedImageBinding
import com.example.ekyc.ui.back.CameraBackFragment
import com.example.ekyc.ui.back.CameraConfirmBackViewModel
import com.example.ekyc.ui.face.CameraFaceConfirmFragment
import com.example.ekyc.ui.front.CameraConfirmFrontViewModel
import com.example.ekyc.ui.front.CameraFrontFragment
import com.example.ekyc.ui.portrait.CameraPortraitFragment
import com.example.ekyc.utils.extension.addFragment
import org.json.JSONException
import org.json.JSONObject

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

        //Lấy response
        sdkViewModel.responseFrontLiveData.observe(viewLifecycleOwner) { gwMessage ->
            if(gwMessage != "Success"){
                mBinding.icFailFrontSide.visibility = View.VISIBLE
                mBinding.btnContinueFail.visibility = View.VISIBLE
                mBinding.btnRetryAll.visibility = View.VISIBLE
                mBinding.tvError.visibility = View.VISIBLE
                mBinding.btnContinueTrue.visibility = View.GONE
                mBinding.tvFrontSide.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }else{
                mBinding.icGoodFrontSide.visibility = View.VISIBLE

            }
        }
        sdkViewModel.responseBackLiveData.observe(viewLifecycleOwner) { gwMessage ->
            if(gwMessage != "Success"){
                mBinding.icFailBackSide.visibility = View.VISIBLE
                mBinding.btnContinueFail.visibility = View.VISIBLE
                mBinding.btnRetryAll.visibility = View.VISIBLE
                mBinding.tvError.visibility = View.VISIBLE
                mBinding.btnContinueTrue.visibility = View.GONE
                mBinding.tvBackSide.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }else{
                mBinding.icGoodBackSide.visibility = View.VISIBLE
            }
        }
        sdkViewModel.responseImageLiveData.observe(viewLifecycleOwner) { gwMessage ->
            if(gwMessage != "Success"){
                mBinding.icFailPnSide.visibility = View.VISIBLE
                mBinding.btnContinueFail.visibility = View.VISIBLE
                mBinding.btnRetryAll.visibility = View.VISIBLE
                mBinding.tvError.visibility = View.VISIBLE
                mBinding.btnContinueTrue.visibility = View.GONE
                mBinding.tvWithPn.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }else{
                mBinding.icGoodPnSide.visibility = View.VISIBLE
            }
        }
        allImage()
        clickBtn()
    }
    private fun allImage(){
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
            parentFragmentManager.addFragment(fragment = CameraFaceConfirmFragment.newInstance())
        }
        mBinding.btnContinueTrue.setOnClickListener {
            parentFragmentManager.addFragment(fragment = RegisterInfoFragment.newInstance())
        }
    }
}