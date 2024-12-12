package com.example.ekyc.utils.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ekyc.R


fun FragmentManager.addFragment(@IdRes id: Int = R.id.frameMain, fragment: Fragment) {
    val tag = fragment::class.java.simpleName
    beginTransaction()
        .add(id, fragment, tag)
        .addToBackStack(tag)
        .commit()
}