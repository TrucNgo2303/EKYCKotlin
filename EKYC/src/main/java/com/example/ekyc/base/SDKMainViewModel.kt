package com.example.ekyc.base

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SDKMainViewModel: ViewModel() {

    var frontImage: Bitmap? = null
    var backImage: Bitmap? = null
    var portraitImage: Bitmap? = null
    var faceImage: Bitmap? = null


    var pathImage: String = ""

    var birthday: String? = null
    var docType: String? = null
    var docNo: String? = null
    var nationality: String? = null
    var issuanceDate: String? = null
    var issuancePlace: String? = null
    var expireDate: String? = null

    var gwMessFront: String = ""
    var gwMessBack: String = ""
    var gwMessPortrait: String = ""
    var gwMessFace: String = ""

}