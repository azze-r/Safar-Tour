package com.bolo.bolomap.ui.diapoarama

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_item_list_diapo.view.imgPic
import java.lang.Exception

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

        val url = url[position]

        holder.itemView.apply {
            try {
                Glide.with(fragment)
                    .load(Uri.parse(url))
                    .into(imgPic)

            }
            catch (e:Exception){}

            setOnClickListener {
                fragment.navDetails()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}