package com.example.ekyc.ui.document

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.databinding.FragmentRegisterInfoBinding

internal class RegisterInfoFragment : BaseDataBindingFragment<FragmentRegisterInfoBinding,RegisterInfoViewModel>() {


    companion object {

        fun newInstance() =
            RegisterInfoFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_register_info

    override fun onBackFragmentPressed() {
        TODO("Not yet implemented")
    }

    override fun onLeftIconClick() {
        TODO("Not yet implemented")
    }

    override fun initialize() {
        mBinding.btnCalendar.setOnClickListener {
            showCalendarPopup(it, mBinding.tvBirthdayDay)
        }
    }
    private fun showCalendarPopup(view: View, tv: AppCompatTextView) {
        // Inflate layout popup_calendar.xml
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.popup_calendar, null)

        // Tạo PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val calendarView = popupView.findViewById<CalendarView>(R.id.calendarView)

        // Xử lý khi chọn ngày từ CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Tháng bắt đầu từ 0 nên cần +1
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            tv.text = selectedDate // Ghi ngày đã chọn vào Button
            popupWindow.dismiss() // Đóng Popup khi chọn xong
        }

        // Hiển thị PopupWindow phía dưới Button
        popupWindow.showAsDropDown(view, 0, 10)
    }
}