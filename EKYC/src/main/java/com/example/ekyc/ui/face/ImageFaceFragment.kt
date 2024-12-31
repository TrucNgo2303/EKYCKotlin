package com.example.ekyc.ui.face

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.api.ApiService
import com.example.ekyc.api.RetrofitClient
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentImageFaceBinding
import com.example.ekyc.ui.document.UnverifiedImageFragment
import com.example.ekyc.utils.extension.addFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

internal class ImageFaceFragment : BaseDataBindingFragment<FragmentImageFaceBinding, ImageFaceViewModel>() {

    private lateinit var viewModel : SDKMainViewModel

    companion object {
        @JvmStatic
        fun newInstance() =
            ImageFaceFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_image_face

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

        viewModel.faceImage?.let { bitmap ->
            mBinding.imvFace.setImageBitmap(bitmap)
        }

        var gwMess = viewModel.gwMessFace
        if(gwMess != "Success")
        {
            mBinding.tvAssignment.backgroundTintList = ColorStateList.valueOf(Color.RED)
            mBinding.vCircleFail.visibility = View.VISIBLE
            mBinding.tvErrorFaceRecorded.visibility = View.VISIBLE
            mBinding.btnContinueFail.visibility = View.VISIBLE
            mBinding.btnRetry.visibility = View.VISIBLE
        }else
        {
            mBinding.vCircle1.visibility = View.VISIBLE
            mBinding.vCircle2.visibility = View.VISIBLE
            mBinding.vCircle3.visibility = View.VISIBLE
            mBinding.tvAssignment.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
            mBinding.btnContinue.visibility = View.VISIBLE
        }


        mBinding.btnRetry.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraFaceConfirmFragment.newInstance())
        }
        mBinding.btnContinueFail.setOnClickListener {
            parentFragmentManager.addFragment(fragment = UnverifiedImageFragment.newInstance())
        }
        mBinding.btnContinue.setOnClickListener {
            parentFragmentManager.addFragment(fragment = UnverifiedImageFragment.newInstance())
        }

    }

}