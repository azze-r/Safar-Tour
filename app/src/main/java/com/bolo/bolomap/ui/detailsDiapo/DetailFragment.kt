package com.bolo.bolomap.ui.detailsDiapo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bolo.bolomap.R
import com.bolo.bolomap.ui.travelog.TravelogViewModel
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

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
        val name = arguments?.getString("userName") ?: "https://mcdaniel.hu/wp-content/uploads/2015/02/globe-map-suitcase-travel-1176x445.jpg"

//        val img: ImageView = root.findViewById(R.id.imgPic)
//        ImageUtils.loadImage(name, R.drawable.ic_launcher_background, img)

        val bundle = bundleOf("userName" to name)

        val sliderView:SliderView =  root.findViewById(R.id.imageSlider)

        val adapter: DetailSliderAdapter =
            DetailSliderAdapter(context)
        adapter.count = 5

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
        return root
    }



}