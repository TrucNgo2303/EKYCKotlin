package com.example.ekyc.ui.document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import com.example.ekyc.R
import com.example.ekyc.base.BaseDataBindingFragment
import com.example.ekyc.base.SDKMainViewModel
import com.example.ekyc.databinding.FragmentRegisterInfoBinding
import com.example.ekyc.ui.front.CameraFrontFragment
import com.example.ekyc.utils.extension.addFragment
import java.util.*

internal class RegisterInfoFragment : BaseDataBindingFragment<FragmentRegisterInfoBinding,RegisterInfoViewModel>() {

    private lateinit var sdkViewModel: SDKMainViewModel

    companion object {

        fun newInstance() =
            RegisterInfoFragment().apply {
                arguments = Bundle()
            }
    }

    override fun layoutResId(): Int = R.layout.fragment_register_info

    override fun onBackFragmentPressed() {

    }

    override fun onLeftIconClick() {
        mBinding.imgArrowBack.setOnClickListener {
            parentFragmentManager.addFragment(fragment = CameraFrontFragment.newInstance())
        }
    }

    override fun initialize() {
        onLeftIconClick()
        sdkViewModel = ViewModelProvider(requireActivity())[SDKMainViewModel::class.java]

        info()
        allImage()

        mBinding.btnCalendar.setOnClickListener {
            showCalendarPopup(it, mBinding.tvBirthdayDay)
        }
        mBinding.btnCalendarIssuanceDate.setOnClickListener {
            showCalendarPopup(it, mBinding.tvIssuanceDate)
        }
        mBinding.btnCalendarExpireDate.setOnClickListener {
            showCalendarPopup(it, mBinding.tvExpireDate)
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

        // Lấy Spinner và CalendarView từ layout
        val spinnerYear = popupView.findViewById<Spinner>(R.id.spinnerYear)
        val spinnerMonth = popupView.findViewById<Spinner>(R.id.spinnerMonth)
        val calendarView = popupView.findViewById<CalendarView>(R.id.calendarView)

        // Lấy danh sách các năm và tháng
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear - 50..currentYear + 50).toList() // Ví dụ: Từ năm hiện tại trừ 50 đến cộng 50
        val months = (1..12).toList()

        // Thiết lập Adapter cho Spinner chọn năm và tháng
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = yearAdapter

        // Đặt giá trị mặc định cho spinnerYear là năm hiện tại
        spinnerYear.setSelection(years.indexOf(currentYear))

        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = monthAdapter

        // Đặt tháng mặc định là tháng hiện tại
        spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        // Cập nhật ngày trên CalendarView khi năm và tháng thay đổi
        spinnerYear.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateCalendar(calendarView, spinnerYear.selectedItem as Int, spinnerMonth.selectedItem as Int)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        spinnerMonth.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateCalendar(calendarView, spinnerYear.selectedItem as Int, spinnerMonth.selectedItem as Int)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        // Xử lý khi chọn ngày từ CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Lấy giá trị từ Spinner
            val selectedYear = spinnerYear.selectedItem.toString().toInt()
            val selectedMonth = spinnerMonth.selectedItem.toString().toInt()

            // Cập nhật giá trị vào TextView
            val selectedDate = "$dayOfMonth/$selectedMonth/$selectedYear"
            tv.text = selectedDate // Ghi ngày đã chọn vào TextView
            popupWindow.dismiss() // Đóng Popup khi chọn xong
        }

        // Hiển thị PopupWindow phía dưới View
        popupWindow.showAsDropDown(view, 0, 10)
    }

    private fun updateCalendar(calendarView: CalendarView, year: Int, month: Int) {
        // Cập nhật lại ngày của CalendarView theo năm và tháng đã chọn
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // Month là từ 0-11 nên cần -1
        calendarView.date = calendar.timeInMillis
    }


    private fun info(){
        mBinding.tvBirthdayDay.text = sdkViewModel.birthday
        val docType = sdkViewModel.docType
        if(docType == "1"){
            mBinding.tvDocType.text = "ID"
        }else if(docType == "2"){
            mBinding.tvDocType.text = "Passport"
        }else if(docType == "3"){
            mBinding.tvDocType.text = "Driver License"
        }



        mBinding.tvDocNo.text = sdkViewModel.docNo
        mBinding.tvNationality.text = sdkViewModel.nationality
        mBinding.tvIssuanceDate.text = sdkViewModel.issuanceDate
        mBinding.tvIssuancePlace.text = sdkViewModel.issuancePlace
        mBinding.tvExpireDate.text = sdkViewModel.expireDate
    }
    private fun allImage(){
        // Lấy ảnh mặt trước căn cước
        sdkViewModel.frontImage.observe(viewLifecycleOwner) { bitmap ->
            // Xử lý ảnh ở đây khi LiveData thay đổi
            if (bitmap != null) {
                // Sử dụng bitmap ở đây
                mBinding.imgFrontSide.setImageBitmap(bitmap)
            }
        }

        // Lấy ảnh mặt sau căn cước
        sdkViewModel.backImage.observe(viewLifecycleOwner) { bitmap ->
            // Xử lý ảnh ở đây khi LiveData thay đổi
            if (bitmap != null) {
                // Sử dụng bitmap ở đây
                mBinding.imgBackSide.setImageBitmap(bitmap)
            }
        }

        // Lấy ảnh chính giữa với số điện thoại
        sdkViewModel.portraitImage.observe(viewLifecycleOwner) { bitmap ->
            // Xử lý ảnh ở đây khi LiveData thay đổi
            if (bitmap != null) {
                // Sử dụng bitmap ở đây
                mBinding.imgWithPn.setImageBitmap(bitmap)
            }
        }
    }



}