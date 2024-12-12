package com.example.ekyc.base

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SDKMainViewModel: ViewModel() {

    //val frontImage: Bitmap? = null
    val frontImage: MutableLiveData<Bitmap?> = MutableLiveData(null)

    fun saveImage(bitmap: Bitmap) {
        frontImage.value = bitmap
    }
}