package com.example.ekyc.ui.back

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.api.ApiService
import com.example.ekyc.api.RetrofitClient
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentCameraConfirmBackBinding
import com.example.ekyc.ui.document.UnverifiedImageFragment
import com.example.ekyc.ui.portrait.CameraPortraitFragment
import com.example.ekyc.utils.extension.addFragment
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

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

        viewModel.backImage?.let { bitmap ->
            mBinding.ivCard.setImageBitmap(bitmap)
        }

        mBinding.btnRetake.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraBackFragment.newInstance())
        }
        mBinding.btnContinue.setOnClickListener {
            callAPI()
            parentFragmentManager.addFragment(fragment = CameraPortraitFragment.newInstance())

        }
    }
    private fun callAPI() {
        // Lấy thời gian hiện tại
        val currentTimestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(
            Date()
        )

        // Chuyển currentTimestamp thành RequestBody
        val transId = currentTimestamp.toRequestBody("text/plain".toMediaTypeOrNull())

        // Tiến hành gọi API với transId
        viewModel.backImage?.let { bitmap ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            val requestFile = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", "$transId.jpg", requestFile)

            val side = "2".toRequestBody("text/plain".toMediaTypeOrNull())

            // Tạo Retrofit API service
            val apiService = RetrofitClient.apiClient.create(ApiService::class.java)

            // Gửi yêu cầu API với các tham số bổ sung
            apiService.uploadImageBack(
                multipartBody,
                transId,
                side
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val gwMessage = response.gw_message
                    viewModel.gwMessBack = gwMessage
                    viewModel.nationality = response.gw_body.value.issued_at
                    viewModel.issuanceDate = response.gw_body.value.issued_on
                    viewModel.issuancePlace = response.gw_body.value.issued_at
                    viewModel.expireDate = response.gw_body.value.license.B

                    Log.d("Api", "API Success: $response")
                }, { throwable ->
                    Log.d("Api", "Error: ${throwable.message}")
                })
        } ?: run {
            Log.d("Api", "Image not available")

        }
    }

}