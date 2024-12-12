package com.example.ekyc.ui.portrait

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraConfirmPortraitBinding
import com.example.ekyc.ui.back.CameraBackFragment
import com.example.ekyc.ui.face.CameraWholeFaceFragment
import com.example.ekyc.ui.front.CameraFrontFragment
import com.example.ekyc.utils.extension.addFragment

internal class CameraConfirmPortraitFragment : BaseDataBindingFragment<FragmentCameraConfirmPortraitBinding, CameraConfirmPortraitViewModel>() {

    private lateinit var viewModel: SDKMainViewModel


    companion object {

        fun newInstance() =
            CameraConfirmPortraitFragment().apply {

            }
    }

    override fun layoutResId(): Int = R.layout.fragment_camera_confirm_portrait

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

        // Truy cập ViewModel từ Activity
        viewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]

        // Lắng nghe thay đổi trong LiveData và xử lý ảnh khi có sự thay đổi
        viewModel.frontImage.observe(viewLifecycleOwner) { bitmap ->
            // Xử lý ảnh ở đây khi LiveData thay đổi
            if (bitmap != null) {
                // Sử dụng bitmap ở đây
                mBinding.ivCard.setImageBitmap(bitmap)
            }
        }
        mBinding.btnRetake.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraPortraitFragment.newInstance())
        }
        mBinding.btnContinue.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraWholeFaceFragment.newInstance())
        }
    }
}