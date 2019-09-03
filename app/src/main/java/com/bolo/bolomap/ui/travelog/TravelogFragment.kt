package com.bolo.bolomap.ui.travelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bolo.bolomap.R

class TravelogFragment : Fragment() {

    private lateinit var travelogViewModel: TravelogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        travelogViewModel =
            ViewModelProviders.of(this).get(TravelogViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_travelog, container, false)
        val textView: TextView = root.findViewById(R.id.text_travelog)
        travelogViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}