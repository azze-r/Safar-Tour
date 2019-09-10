package com.bolo.bolomap.ui.detailsDiapo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Media
import com.bolo.bolomap.ui.travelog.TraveLogAdapter
import com.bolo.bolomap.ui.travelog.TravelogViewModel
import com.bolo.bolomap.utils.ImageUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.random.Random

class DetailFragment : Fragment() {

    private lateinit var travelogViewModel: TravelogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        travelogViewModel =
            ViewModelProviders.of(this).get(TravelogViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_detail_diapo, container, false)
        val img: ImageView = root.findViewById(R.id.imgPic)
        val name = arguments?.getString("userName") ?: "https://mcdaniel.hu/wp-content/uploads/2015/02/globe-map-suitcase-travel-1176x445.jpg"
        ImageUtils.loadImage(name, R.drawable.ic_launcher_background, img)

        return root
    }



}