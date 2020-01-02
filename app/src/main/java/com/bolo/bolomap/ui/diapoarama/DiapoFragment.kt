package com.bolo.bolomap.ui.diapoarama

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Photo
import com.bolo.bolomap.db.viewmodel.MainActivity
import com.bolo.bolomap.ui.travelog.ListAlbumsViewModel
import android.R.attr.data
import android.R.attr.data
import android.content.ClipData






class DiapoFragment : Fragment() {

    private lateinit var listAlbumsViewModel: ListAlbumsViewModel
    lateinit var photo:Photo
    lateinit var imgAdd:ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listAlbumsViewModel =
            ViewModelProviders.of(this).get(ListAlbumsViewModel::class.java)
        val id = arguments?.getInt("albumId") ?: 0

        val root = inflater.inflate(R.layout.fragment_all_diapo, container, false)
        imgAdd = root.findViewById(R.id.imgAdd)

        val localRecycler:RecyclerView = root.findViewById(R.id.myrecycler)
        localRecycler.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)

        val photoDao = (activity as MainActivity).getDao()

        photoDao!!.findById(id).observe(this,
            Observer {
                if (it != null) {
                    photo = it
                    if (it.photos != null) {
                        val list = convertStringToArray(it.photos!!)
                        localRecycler.adapter = DiapoAdapter(this, list.toList())
                    }
                }
            })

        imgAdd.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, "Select picture"), 1)
        }


        return root
    }

    fun navDetails() {
        val bundle = bundleOf("albumId" to photo.id)
        view?.findNavController()?.navigate(R.id.action_navigation_diapo_to_navigation_detail,bundle)
    }


    var strSeparator = "__,__"

    private fun convertArrayToString(array: ArrayList<String>): String {
        var str = ""
        for (i in array.indices) {
            str += array[i]
            // Do not append comma at the end of last element
            if (i < array.size - 1) {
                str += strSeparator
            }
        }
        return str
    }

    private fun convertStringToArray(str: String): Array<String> {
        return str.split(strSeparator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val photos = ArrayList<String>()
                if (data != null) {
                    if (photo.photos != null)
                        photos.addAll(convertStringToArray(photo.photos.toString()))

                    val clipData = data.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            val imageUri = clipData.getItemAt(i).uri
                            photos.add(imageUri.toString())
                        }
                    }
                    else {
                        val uri = data.data
                        photos.add(uri.toString())
                    }
                }

                photo.photos = convertArrayToString(photos)
                (activity as MainActivity).updatePhoto(photo)
            }
        }
    }

}