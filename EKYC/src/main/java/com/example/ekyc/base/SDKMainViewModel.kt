package com.example.ekyc.base

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SDKMainViewModel: ViewModel() {

    //val frontImage: Bitmap? = null
    val frontImage: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val backImage: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val portraitImage: MutableLiveData<Bitmap?> = MutableLiveData(null)

    fun saveFrontImage(bitmap: Bitmap) {
        frontImage.value = bitmap
    }
    fun saveBackImage(bitmap: Bitmap) {
        backImage.value = bitmap
    }
    fun savePortraitImage(bitmap: Bitmap) {
        portraitImage.value = bitmap
    }

}