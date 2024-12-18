package com.example.ekyc.ui.back

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraConfirmBackBinding
import com.example.ekyc.ui.portrait.CameraPortraitFragment
import com.example.ekyc.utils.extension.addFragment

internal class CameraConfirmBackFragment : BaseDataBindingFragment<FragmentCameraConfirmBackBinding, CameraConfirmBackViewModel>() {

    private lateinit var viewModel : SDKMainViewModel


    companion object {

        fun newInstance() =
            CameraConfirmBackFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_camera_confirm_back

    override fun onBackFragmentPressed() {

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
        viewModel.backImage.observe(viewLifecycleOwner) { bitmap ->
            // Xử lý ảnh ở đây khi LiveData thay đổi
            if (bitmap != null) {
                // Sử dụng bitmap ở đây
                mBinding.ivCard.setImageBitmap(bitmap)
            }
        }



        mBinding.btnRetake.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraBackFragment.newInstance())
        }
        mBinding.btnContinue.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraPortraitFragment.newInstance())
        }
    }

}