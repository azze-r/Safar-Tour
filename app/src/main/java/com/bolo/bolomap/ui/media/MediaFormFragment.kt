package com.bolo.bolomap.ui.media

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.bolo.bolomap.R
import com.bolo.bolomap.utils.BaseFragment
import kotlinx.android.synthetic.main.media_form_fragment.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MediaFormFragment : BaseFragment() {

    private lateinit var viewModel: MediaFormViewModel
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle? ): View? {

        return inflater.inflate(R.layout.media_form_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MediaFormViewModel::class.java)
        val formatter = SimpleDateFormat(getString(R.string.format_date))
        val formatted = formatter.format(Date())
        date_ET.setText(formatted)

        date_ET.setOnClickListener {
            showTimePickerDialog(it)
        }
    }

    fun showTimePickerDialog(v: View) {
        fragmentManager?.let {
            TimePickerFragment.show(it,this)
        }
    }

    fun displayHour(hourOfDay: Int, minute: Int) {
        Toast.makeText(context,hourOfDay.toString(),Toast.LENGTH_SHORT).show()
    }

    fun displayDate(p1: Int, p2: Int, p3: Int) {

    }

}
