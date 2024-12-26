package com.example.ekyc.ui.portrait

import android.graphics.Bitmap
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

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
            callAPI()
            parentFragmentManager.addFragment(fragment = UnverifiedImageFragment.newInstance())
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
        viewModel.portraitImage.value?.let { bitmap ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            val requestFile = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image_selfie_path", "$transId.jpg", requestFile)

            //Tạo token
            //RetrofitClient.setToken("818f47f73f024b26abb11b78e05e1349")

            // Tạo Retrofit API service
            val apiService = RetrofitClient.apiClient.create(ApiService::class.java)

            var pathImage = viewModel.pathImage.toRequestBody("text/plain".toMediaTypeOrNull())

            Log.d("Api","Path Image: ${viewModel.pathImage}")

            // Gửi yêu cầu API với các tham số bổ sung
            apiService.uploadFace(
                transId,
                pathImage,
                multipartBody
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val gwMessage = response.gw_message
                    viewModel.responseImageLiveData.postValue(gwMessage)
                    Log.d("Api", "API Success: $response")
                }, { throwable ->
                    Log.d("Api", "Error: ${throwable.message}")
                })
        } ?: run {
            Log.d("Api", "Image not available")
        }
    }
}