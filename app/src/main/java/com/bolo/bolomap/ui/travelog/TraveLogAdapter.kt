package com.bolo.bolomap.ui.travelog

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Photo
import kotlinx.android.synthetic.main.view_item_list_medias.view.*

class TraveLogAdapter(val fragment: TravelogFragment,val photos: List<Photo>) : RecyclerView.Adapter<TraveLogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = R.layout.view_item_list_medias
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val photo = photos[position]

        holder.itemView.apply {
            Log.i("tryhard",photo.photo)
            val realUri = Uri.parse(photo.photo)

            imgPic.setImageURI(realUri)
//            ImageUtils.loadImageResize(media.photo,R.drawable.ic_launcher_background,imgPic,context)
            setOnClickListener {
                fragment.navDiapo()
            }
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}