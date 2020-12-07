package com.tvk.dateremainder.backgroundprocess

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tvk.dateremainder.R
import com.tvk.dateremainder.viewmodel.ScheduleLiveViewModel

class CallNotification:BroadcastReceiver() {
    private lateinit var scheduleLiveViewModel: ScheduleLiveViewModel


    override fun onReceive(p0: Context?, p1: Intent?) {

       /* scheduleLiveViewModel = ViewModelProvider(this).get(ScheduleLiveViewModel::class.java)
        scheduleLiveViewModel.readAllData.observe(viewLifecycleOwner, Observer { t->


        })*/





    }
}