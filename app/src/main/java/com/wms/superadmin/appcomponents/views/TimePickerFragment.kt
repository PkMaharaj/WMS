package com.wms.superadmin.appcomponents.views

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var defaultDate: Date? = null
    private var callback: ((selectedDate: Date) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        c.time = Date()

        var year = arguments?.getInt(BUNDLE_YEAR, -1) ?: -1
        var month = arguments?.getInt(BUNDLE_MONTH, -1) ?: -1
        var dayOfMonth = arguments?.getInt(BUNDLE_DAY_OF_MONTH, -1) ?: -1
        var hour = arguments?.getInt(BUNDLE_HOURS_OF_DAY, -1) ?: -1
        var minute = arguments?.getInt(BUNDLE_MINUTE, -1) ?: -1

        year = if (year != -1) year else c.get(Calendar.YEAR)
        month = if (month != -1) month else c.get(Calendar.MONTH)
        dayOfMonth = if (dayOfMonth != -1) dayOfMonth else c.get(Calendar.DAY_OF_MONTH)
        hour = if (hour != -1) hour else c.get(Calendar.HOUR_OF_DAY)
        minute = if (minute != -1) minute else c.get(Calendar.MINUTE)

        c.set(year, month, dayOfMonth, hour, minute)
        defaultDate = c.time
        return TimePickerDialog(
            requireContext(),
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    fun show(
        manager: FragmentManager, tag: String?,
        callback: (selectedDate: Date) -> Unit
    ) {
        super.show(manager, tag)
        this.callback = callback
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        defaultDate?.let {
            calendar.time = it
        }
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        callback?.invoke(calendar.time)
    }


    public companion object {
        public const val TAG: String = "TIME_PICKER_FRAGMENT"

        private const val BUNDLE_DAY_OF_MONTH: String = "bundle_selected_day_of_month"
        private const val BUNDLE_MONTH: String = "bundle_selected_month"
        private const val BUNDLE_YEAR: String = "bundle_selected_year"
        private const val BUNDLE_HOURS_OF_DAY: String = "bundle_selected_hour_of_day"
        private const val BUNDLE_MINUTE: String = "bundle_selected_minute"

        public fun getInstance(defaultDate: Date? = null): TimePickerFragment {
            val bundle = Bundle()
            defaultDate?.let {
                val c = Calendar.getInstance()
                c.time = it
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val hoursOfDay = c.get(Calendar.HOUR_OF_DAY)
                val minutes = c.get(Calendar.MINUTE)

                bundle.putInt(BUNDLE_YEAR, year)
                bundle.putInt(BUNDLE_MONTH, month)
                bundle.putInt(BUNDLE_DAY_OF_MONTH, day)
                bundle.putInt(BUNDLE_HOURS_OF_DAY, hoursOfDay)
                bundle.putInt(BUNDLE_MINUTE, minutes)
            }

            val fragment = TimePickerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}