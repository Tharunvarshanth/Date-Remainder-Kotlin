package com.tvk.dateremainder.uiFragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tvk.dateremainder.R
import com.tvk.dateremainder.viewmodel.ScheduleLiveViewModel
import kotlinx.android.synthetic.main.recyclerview_item.view.*


class HomeSchdeuleViewFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var scheduleLiveViewModel: ScheduleLiveViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
      /*  val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        //Recycle view
        val adapter = ListAdapter()
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerview)
          recyclerView.adapter = adapter
        recyclerView.layoutManager  = LinearLayoutManager(requireContext())

        scheduleLiveViewModel = ViewModelProvider(this).get(ScheduleLiveViewModel::class.java)
       scheduleLiveViewModel.readAllData.observe(viewLifecycleOwner, Observer { t->
           adapter.setData(t)
       })





        return root
    }







}