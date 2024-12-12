package com.example.ekyc.module

import android.app.Activity
import android.content.Intent
import com.example.ekyc.ui.main.EKYCMainActivity

object SDKModule {

    fun start(activity: Activity) {
        val intent = Intent(activity, EKYCMainActivity::class.java)
        activity.startActivity(intent)
    }
}