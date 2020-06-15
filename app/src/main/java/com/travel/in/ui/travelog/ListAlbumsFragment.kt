package com.travel.`in`.ui.travelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.travel.`in`.R
import com.travel.`in`.db.dao.PhotoDao
import com.travel.`in`.db.viewmodel.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListAlbumsFragment : Fragment() {

    private lateinit var listAlbumsViewModel: ListAlbumsViewModel
    var photoDao: PhotoDao? = null
    var map: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listAlbumsViewModel =
            ViewModelProviders.of(this).get(ListAlbumsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val localRecycler:RecyclerView = root.findViewById(R.id.myrecycler)
        map  = root.findViewById(R.id.map)
        photoDao = (activity as MainActivity).getDao()

        photoDao!!.getAllPhotos().observe(this,
            Observer {
                localRecycler.adapter = ListAlbumsAdapter(this, it)
            })

        localRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        map!!.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return root
    }

    fun navDiapo(id: Int) {
        val bundle = bundleOf("albumId" to id)
        view?.findNavController()?.navigate(R.id.action_navigation_dashboard_to_navigation_diapo,bundle)
    }

    

}