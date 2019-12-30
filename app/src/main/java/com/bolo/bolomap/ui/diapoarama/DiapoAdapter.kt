package com.bolo.bolomap.ui.diapoarama

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bolo.bolomap.utils.ImageUtils
import kotlinx.android.synthetic.main.view_item_list_diapo.view.*

class DiapoAdapter(val fragment: DiapoFragment,val url: List<String>) : RecyclerView.Adapter<DiapoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = R.layout.view_item_list_diapo
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return url.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val media = url[position]

        holder.itemView.apply {
            ImageUtils.loadImageResize(media, R.drawable.ic_launcher_background, imgPic,context)
            setOnClickListener {
                val bundle = bundleOf("userName" to media)
                fragment.navDetails(bundle)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}