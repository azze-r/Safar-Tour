package com.bolo.bolomap.ui.detailsDiapo

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bolo.bolomap.R
import com.bolo.bolomap.utils.ImageUtils
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class DetailSliderAdapter(private val context: Context, private val urls: Array<String>) :
    SliderViewAdapter<DetailSliderAdapter.SliderAdapterVH>() {
    private var mCount: Int = 0

    fun setCount(count: Int) {
        this.mCount = count
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }


    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {

        if (urls[position] == "null") {
            ImageUtils.loadImageUriResize(urls[position+1],R.drawable.baseline_add_photo_alternate_black_48,viewHolder.imageViewBackground,context)
        } else {
            ImageUtils.loadImageUriResize(urls[position],R.drawable.baseline_add_photo_alternate_black_48,viewHolder.imageViewBackground,context)
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mCount
    }

    inner class SliderAdapterVH(var itemView: View) :
        SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView

        init {
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
        }
    }
}