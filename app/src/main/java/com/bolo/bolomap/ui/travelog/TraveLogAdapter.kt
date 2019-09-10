package com.bolo.bolomap.ui.travelog

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Media
import com.bolo.bolomap.utils.ImageUtils
import kotlinx.android.synthetic.main.view_item_list_medias.view.*

class TraveLogAdapter(val fragment: TravelogFragment,val medias: List<Media>) : RecyclerView.Adapter<TraveLogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = R.layout.view_item_list_medias
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return medias.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val media = medias[position]

        holder.itemView.apply {
            Log.i("tryhard",media.toString())
            ImageUtils.loadImageResize(media.photos, R.drawable.ic_launcher_background, imgPic,context)

            setOnClickListener {
                fragment.navDiapo()
            }
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}