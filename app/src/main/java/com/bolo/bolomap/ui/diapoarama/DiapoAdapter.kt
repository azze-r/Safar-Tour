package com.bolo.bolomap.ui.diapoarama

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Media
import com.bolo.bolomap.utils.ImageUtils
import kotlinx.android.synthetic.main.view_item_list_medias.view.*

class DiapoAdapter(val fragment: DiapoFragment,val medias: List<Media>) : RecyclerView.Adapter<DiapoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = R.layout.view_item_list_diapo
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return medias.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val media = medias[position]

        holder.itemView.apply {
            ImageUtils.loadImageResize(media.photos, R.drawable.ic_launcher_background, imgPic,context)
            setOnClickListener {
                val bundle = bundleOf("userName" to media.photos)

                fragment.navDetails(bundle)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}