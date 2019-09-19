package com.bolo.bolomap.ui.travelog

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Album
import com.bolo.bolomap.utils.ImageUtils
import kotlinx.android.synthetic.main.view_item_list_medias.view.*


class TraveLogAdapter(val fragment: TravelogFragment,val albums: List<Album>) : RecyclerView.Adapter<TraveLogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = R.layout.view_item_list_medias
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val media = albums[position]

        holder.itemView.apply {
            val realUri = Uri.parse(media.photo)
            ImageUtils.loadImageResize(media.photo,R.drawable.ic_launcher_background,imgPic,context)
//            ImageUtils.loadImageResizeLocal(realUri, R.drawable.ic_launcher_background, imgPic,context)
            setOnClickListener {
                fragment.navDiapo()
            }
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}