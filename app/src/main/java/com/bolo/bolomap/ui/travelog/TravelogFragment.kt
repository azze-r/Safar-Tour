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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_dashboard.*


class TravelogFragment : Fragment() {

    private lateinit var travelogViewModel: TravelogViewModel
    var isFABOpen = false
    lateinit var fab1: FloatingActionButton
    lateinit var fab2: FloatingActionButton
    lateinit var fab3: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        travelogViewModel =
            ViewModelProviders.of(this).get(TravelogViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab1 = root.findViewById(R.id.fab1)
        fab2 = root.findViewById(R.id.fab2)
        fab3 = root.findViewById(R.id.fab3)

        fab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }
        return root
    }

    private fun showFABMenu() {
        isFABOpen = true
        fab1.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fab2.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        fab3.animate().translationY(-resources.getDimension(R.dimen.standard_155))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        fab1.animate().translationY(0F)
        fab2.animate().translationY(0F)
        fab3.animate().translationY(0F)
    }


}