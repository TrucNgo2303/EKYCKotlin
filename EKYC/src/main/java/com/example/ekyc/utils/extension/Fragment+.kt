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

fun FragmentManager.addFragmentWithAnimation(
    @IdRes id: Int = R.id.frameMain,
    fragment: Fragment,
    enterAnim: Int = R.anim.slide_up,
    exitAnim: Int = R.anim.slide_down,
    popEnterAnim: Int = R.anim.slide_up,
    popExitAnim: Int = R.anim.slide_down
) {
    val tag = fragment::class.java.simpleName
    beginTransaction()
        .setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        .add(id, fragment, tag)
        .addToBackStack(tag)
        .commit()
}
