package com.example.ekyc.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ekyc.R
import com.example.ekyc.ui.document.RegisterInfoFragment
import com.example.ekyc.ui.face.CameraFaceConfirmFragment
import com.example.ekyc.ui.front.CameraFrontFragment
import com.example.ekyc.ui.portrait.CameraPortraitFragment
import com.example.ekyc.utils.extension.addFragment

class EKYCMainActivity : AppCompatActivity() {

        private val CAMERA_REQUEST_CODE = 100

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_ekycmain)
            checkCameraPermission()
        }

        fun checkCameraPermission() {
            // Kiểm tra xem quyền đã được cấp chưa
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Quyền đã được cấp
                startCamera()
            } else {
                // Quyền chưa được cấp, yêu cầu quyền
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            }
        }

        // Hàm xử lý khi người dùng phản hồi yêu cầu cấp quyền
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Quyền được cấp
                    startCamera()
                } else {
                    // Quyền bị từ chối
                    showPermissionDeniedMessage()
                }
            }
        }

        private fun startCamera() {
            // Khởi tạo và thêm Fragment camera vào Activity
            supportFragmentManager.addFragment(fragment = CameraFrontFragment.newInstance())
        }

        private fun showPermissionDeniedMessage() {
            // Thông báo rằng người dùng cần cấp quyền để sử dụng tính năng
            Toast.makeText(this, "Bạn cần cấp quyền Camera để sử dụng tính năng này", Toast.LENGTH_SHORT).show()
        }
    }


//}