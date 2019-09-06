package com.bolo.bolomap.ui.media

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bolo.bolomap.R

class MediaFormFragment : Fragment() {

    companion object {
        fun newInstance() = MediaFormFragment()
    }

    private lateinit var viewModel: MediaFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_form_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MediaFormViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
