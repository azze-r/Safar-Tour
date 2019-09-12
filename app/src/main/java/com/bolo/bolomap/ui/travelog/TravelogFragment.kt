package com.bolo.bolomap.ui.travelog

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolo.bolomap.MainActivity
import com.bolo.bolomap.R
import com.bolo.bolomap.RoomDatabase
import com.bolo.bolomap.db.dao.MediaDao
import com.bolo.bolomap.db.entities.Media
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.random.Random

class TravelogFragment : Fragment() {

    private lateinit var travelogViewModel: TravelogViewModel
    var mediaDao: MediaDao? = null

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

        localRecycler.adapter = GetMediasAsyncTask(mediaDao!!).execute().get().let {
            TraveLogAdapter(this, GetMediasAsyncTask(mediaDao!!).execute().get())
        }

        localRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)



        fab.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_media_form)
        }

        return root
    }

    fun navDiapo(){
        view?.findNavController()?.navigate(R.id.action_navigation_dashboard_to_navigation_diapo)
    }

    class GetMediasAsyncTask internal constructor(private val mAsyncTaskDao: MediaDao) :
        AsyncTask<Void, Void, List<Media>>() {
        override fun doInBackground(vararg p0: Void?): List<Media> {
            return mAsyncTaskDao.getAll()
        }

    }
    

}