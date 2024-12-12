package com.example.ekyc.ui.face

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekyc.R

class CameraStraightFaceFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera_straight_face, container, false)
    }

    companion object {
        fun newInstance() =
            CameraStraightFaceFragment().apply {
                arguments = Bundle()
            }
    }
}