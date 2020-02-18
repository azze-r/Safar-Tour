package com.bolo.bolomap.ui.travelog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Photo
import com.bolo.bolomap.utils.ImageUtils
import kotlinx.android.synthetic.main.view_item_list_medias.view.*


class ListAlbumsAdapter(val fragment: ListAlbumsFragment, val photos: List<Photo>) : RecyclerView.Adapter<ListAlbumsAdapter.ViewHolder>() {

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

            if (position == 1){
                holder.itemView.setVisibility(View.GONE)
                holder.itemView.setLayoutParams(RecyclerView.LayoutParams(0, 0))
            }
            try {
                ImageUtils.loadImageUriResize(photo.photo,R.drawable.baseline_add_photo_alternate_black_48,imgPic,context)

                if (photo.label.isNullOrEmpty())
                    label.text = "Aucun titre"
                else
                    label.text = photo.label

            }
            catch (e:Exception){}
            setOnClickListener {
                fragment.navDiapo(photo.id)
            }
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}