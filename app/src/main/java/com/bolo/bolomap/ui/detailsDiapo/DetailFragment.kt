package com.bolo.bolomap.ui.detailsDiapo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Photo
import com.bolo.bolomap.db.viewmodel.MainActivity
import com.bolo.bolomap.ui.diapoarama.DiapoAdapter
import com.bolo.bolomap.ui.travelog.ListAlbumsViewModel
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class DetailFragment : Fragment() {

    private lateinit var listAlbumsViewModel: ListAlbumsViewModel
    lateinit var photo: Photo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listAlbumsViewModel =
            ViewModelProviders.of(this).get(ListAlbumsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_detail_diapo, container, false)
        val id = arguments?.getInt("albumId") ?: 0

        val photoDao = (activity as MainActivity).getDao()

        photoDao!!.findById(id).observe(this,
            Observer {
                photo = it
                if (photo.photos != null) {
                    val list = convertStringToArray(photo.photos!!)
                    val sliderView:SliderView =  root.findViewById(R.id.imageSlider)

                    val adapter = DetailSliderAdapter(context!!,list)
                    adapter.count = list.size
                    sliderView.sliderAdapter = adapter
                    sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION)
                    sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                    sliderView.indicatorSelectedColor = Color.WHITE
                    sliderView.indicatorUnselectedColor = Color.GRAY
                    sliderView.startAutoCycle()

                    sliderView.setOnIndicatorClickListener { position ->
                        sliderView.currentPagePosition = position
                    }
                }
            })


        return root
    }

    var strSeparator = "__,__"

    private fun convertStringToArray(str: String): Array<String> {
        return str.split(strSeparator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

}