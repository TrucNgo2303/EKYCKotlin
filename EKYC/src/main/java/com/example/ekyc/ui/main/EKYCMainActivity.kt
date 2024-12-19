package com.example.ekyc.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ekyc.R
import com.example.ekyc.ui.document.RegisterInfoFragment
import com.example.ekyc.ui.face.CameraFaceConfirmFragment
import com.example.ekyc.ui.front.CameraFrontFragment
import com.example.ekyc.utils.extension.addFragment

class EKYCMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekycmain)

        supportFragmentManager.addFragment(fragment = CameraFrontFragment.newInstance())

        //supportFragmentManager.addFragment(fragment = BlankFragment.newInstance())
    }
}