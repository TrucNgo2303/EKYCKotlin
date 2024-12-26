package com.example.ekyc.ui.back

import androidx.lifecycle.MutableLiveData
import com.example.ekyc.base.BaseViewModel

internal class CameraConfirmBackViewModel : BaseViewModel() {
    val responseBackLiveData = MutableLiveData<String>()
}