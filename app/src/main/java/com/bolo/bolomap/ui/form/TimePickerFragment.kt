package com.bolo.bolomap.ui.form

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    companion object {
        var localFragment: MediaFormFragment? = null
        fun show(manager: FragmentManager, fragment: MediaFormFragment) {
            val dialog = TimePickerFragment()
            localFragment = fragment
            dialog.show(manager, "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        localFragment?.displayHour(hourOfDay, minute)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        localFragment?.displayDate(p1, p2,p3 )
    }

}
