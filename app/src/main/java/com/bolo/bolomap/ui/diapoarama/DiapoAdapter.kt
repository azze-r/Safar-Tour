package com.bolo.bolomap.ui.diapoarama

import android.annotation.SuppressLint
import android.media.Image
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bolo.bolomap.utils.ImageUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_item_list_diapo.view.imgPic
import java.lang.Exception

class DiapoAdapter(val fragment: DiapoFragment,val urls: List<String>) : RecyclerView.Adapter<DiapoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = R.layout.view_item_list_diapo
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Log.i("tryhard", "urls at position : $position : ${urls[position]}")

        holder.itemView.apply {

            try {

                if (urls[position] == "null"){
                    ImageUtils.loadImageUriResize(urls[position+1],R.drawable.baseline_add_photo_alternate_black_48,imgPic,context)
                }
                else {
                    ImageUtils.loadImageUriResize(urls[position],R.drawable.baseline_add_photo_alternate_black_48,imgPic,context)
                }
            }
            catch (e:Exception){

            }

            setOnClickListener {
                fragment.navDetails()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}