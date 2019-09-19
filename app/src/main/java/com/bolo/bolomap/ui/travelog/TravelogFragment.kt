package com.bolo.bolomap.ui.travelog

import android.os.Bundle
import android.util.Log
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
import com.bolo.bolomap.db.entities.Album
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TravelogFragment : Fragment() {

    private lateinit var travelogViewModel: TravelogViewModel
    var albumDao: AlbumDao? = null

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

        albumDao = (activity as MainActivity).getDao()

        albumDao!!.getAllAlbums().observe(this,
            Observer {
                var albums = it
                if (albums.isEmpty()){
                    val album1 = Album(0,null,"album 1",null,null,"https://images.unsplash.com/photo-1558981852-426c6c22a060?ixlib=rb-1.2.1&w=1000&q=80",null)
                    val album2 = Album(0,null,"album 1",null,null,"https://img4.goodfon.com/wallpaper/nbig/a/af/mountains-clouds-travel-wallpaper-background-priroda-peizazh.jpg",null)
                    val fakeAlbum = ArrayList<Album>()
                    fakeAlbum.add(album1)
                    fakeAlbum.add(album1)
                    fakeAlbum.add(album2)
                    fakeAlbum.add(album1)
                    fakeAlbum.add(album1)
                    fakeAlbum.add(album1)
                    albums = fakeAlbum
                }
                else {
                    Log.i("tryhard", albums.size.toString())
                }

                localRecycler.adapter = TraveLogAdapter(this, albums)
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