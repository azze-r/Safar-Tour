package com.bolo.bolomap.ui.travelog

import android.annotation.SuppressLint
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.db.entities.Photo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_item_list_medias.view.*


class TraveLogAdapter(val fragment: TravelogFragment,val photos: List<Photo>) : RecyclerView.Adapter<TraveLogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = com.bolo.bolomap.R.layout.view_item_list_medias
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val photo = photos[position]

        holder.itemView.apply {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, photo.photo?.toUri())
                Glide.with(context)
                    .asBitmap()
                    .load(bitmap)
                    .into(imgPic)


//                ImageUtils.loadImage(photo.photo?.toUri(),R.drawable.baseline_add_photo_alternate_black_48,imgPic)
            }
            catch (e:Exception){}
            setOnClickListener {
                fragment.navDiapo()
            }
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}