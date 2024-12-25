package com.example.ekyc.ui.portrait

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.api.ApiService
import com.example.ekyc.api.RetrofitClient
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraConfirmPortraitBinding
import com.example.ekyc.ui.document.UnverifiedImageFragment
import com.example.ekyc.utils.extension.addFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

internal class CameraConfirmPortraitFragment : BaseDataBindingFragment<FragmentCameraConfirmPortraitBinding, CameraConfirmPortraitViewModel>() {

    private lateinit var viewModel: SDKMainViewModel


    companion object {

        fun newInstance() =
            CameraConfirmPortraitFragment().apply {

            }
    }

    override fun layoutResId(): Int = R.layout.fragment_camera_confirm_portrait

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
        viewModel.portraitImage.observe(viewLifecycleOwner) { bitmap ->
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
            parentFragmentManager.addFragment(fragment = UnverifiedImageFragment.newInstance())
        }
    }

//    private fun callAPI() {
//        viewModel.portraitImage.value?.let { bitmap ->
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//
//            // Tạo RequestBody từ byteArray
//            val requestFile = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
//            val multipartBody = MultipartBody.Part.createFormData("image", "portrait.jpg", requestFile)
//
//            val apiService = RetrofitClient.apiClient.create(ApiService::class.java)
//
//            apiService.uploadImage(multipartBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ apiResponse ->
//                    if (apiResponse.gw_status == "success") {
//                        Log.d("Api", "Message success")
//                        parentFragmentManager.addFragment(fragment = UnverifiedImageFragment.newInstance())
//                    } else {
//                        Log.d("Api", "Message error")
//                        Toast.makeText(context, apiResponse.gw_message ?: "Error", Toast.LENGTH_SHORT).show()
//                    }
//                }, { throwable ->
//                    Log.d("Api", "Error: ${throwable.message}")
//                    Toast.makeText(context, "Error: ${throwable.message}", Toast.LENGTH_SHORT).show()
//                })
//        } ?: run {
//            Log.d("Api", "Image not available")
//            Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show()
//        }
//    }


}