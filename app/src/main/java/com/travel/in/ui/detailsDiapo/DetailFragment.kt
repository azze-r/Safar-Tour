package com.travel.`in`.ui.detailsDiapo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.travel.`in`.R
import com.travel.`in`.db.entities.Photo
import com.travel.`in`.db.viewmodel.MainActivity
import com.travel.`in`.ui.travelog.ListAlbumsViewModel
import com.travel.`in`.utils.ImageUtils
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
                    //sliderView.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION)
                    sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                    sliderView.currentPagePosition = ImageUtils.position
                    sliderView.setIndicatorVisibility(false)
                }
            })


        return root
    }

    var strSeparator = "__,__"

    private fun convertStringToArray(str: String): Array<String> {
        return str.split(strSeparator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

}