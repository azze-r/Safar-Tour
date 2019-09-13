package com.bolo.bolomap.ui.travelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.MainActivity
import com.bolo.bolomap.R
import com.bolo.bolomap.db.dao.AlbumDao
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TravelogFragment : Fragment() {

    private lateinit var travelogViewModel: TravelogViewModel
    var mediaDao: AlbumDao? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        travelogViewModel =
            ViewModelProviders.of(this).get(TravelogViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        val localRecycler:RecyclerView = root.findViewById(R.id.myrecycler)

        mediaDao = (activity as MainActivity).getDao()

        mediaDao!!.getAllAlbums().observe(this,
            Observer {
                val medias = it
                localRecycler.adapter = TraveLogAdapter(this, medias)
            })

        localRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        fab.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_media_form)
        }

        return root
    }

    fun navDiapo(){
        view?.findNavController()?.navigate(R.id.action_navigation_dashboard_to_navigation_diapo)
    }
//
//    class GetMediasAsyncTask internal constructor(private val mAsyncTaskDao: AlbumDao) :
//        AsyncTask<Void, Void, List<Album>>() {
//        override fun doInBackground(vararg p0: Void?): List<Album> {
//            return mAsyncTaskDao.getAllAlbums()
//        }
//    }
    

}