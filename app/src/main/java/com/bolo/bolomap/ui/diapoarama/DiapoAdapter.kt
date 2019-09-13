package com.bolo.bolomap.ui.diapoarama

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Album

class DiapoAdapter(val fragment: DiapoFragment,val albums: List<Album>) : RecyclerView.Adapter<DiapoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = R.layout.view_item_list_diapo
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val media = albums[position]

        holder.itemView.apply {
//            ImageUtils.loadImageResize(media.photo, R.drawable.ic_launcher_background, imgPic,context)
            setOnClickListener {
                val bundle = bundleOf("userName" to media.photo)

                fragment.navDetails(bundle)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}