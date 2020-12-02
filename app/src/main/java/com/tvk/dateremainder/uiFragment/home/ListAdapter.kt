package com.tvk.dateremainder.uiFragment.home

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.ListFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tvk.dateremainder.R
import com.tvk.dateremainder.model.ScheduleEntity
import com.tvk.dateremainder.uiFragment.update.UpdateFragment
import com.tvk.dateremainder.viewmodel.ScheduleLiveViewModel
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class ListAdapter:RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private  var timelist = emptyList<ScheduleEntity>()
    private lateinit var scheduleLiveViewModel: ScheduleLiveViewModel
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false))
    }

    override fun getItemCount(): Int {
        return timelist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentItem = timelist[position]
        holder.itemView.findViewById<TextView>(R.id.textViewwho).text = currentItem.who
        holder.itemView.findViewById<TextView>(R.id.textViewdesc).text = currentItem.desc
        holder.itemView.findViewById<TextView>(R.id.textViewtime).text = currentItem.time
        holder.itemView.findViewById<TextView>(R.id.textViewdate).text = currentItem.date

        holder.itemView.updatepencile.setOnClickListener{
           val action = HomeSchdeuleViewFragmentDirections.actionNavHomeToUpdateFragment(currentItem)
           holder.itemView.findNavController().navigate(action)
        }




    }



    fun setData(task:List<ScheduleEntity>){
           this.timelist = task
        notifyDataSetChanged()
    }
}