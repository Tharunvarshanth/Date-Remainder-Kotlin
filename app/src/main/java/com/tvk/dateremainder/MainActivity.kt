package com.tvk.dateremainder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.tvk.dateremainder.notification.RemainderBroadcast
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home,R.id.nav_slideshow, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

      fab.setOnClickListener {
            startAlarm("05:30 PM","ld")
        }


    }
   fun startAlarm(time: String,date: String) {

       var  HOUR = time[0].toString() + time[1].toString()
       var  MIN = time[3].toString() + time[4].toString()

       val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
       var  intent= Intent(this, RemainderBroadcast::class.java);
       intent.putExtra("who","khb")
       intent.putExtra("desc","jb")
       var  pendingIntent = PendingIntent.getBroadcast(this,0,intent, PendingIntent.FLAG_ONE_SHOT);

       Log.i("hOUR", HOUR)
       val calendar = Calendar.getInstance()


       calendar[Calendar.MONTH]=11
       calendar[Calendar.DAY_OF_MONTH]=5
       calendar[Calendar.HOUR_OF_DAY] = 23
       calendar[Calendar.MINUTE] =47
       calendar[Calendar.SECOND] = 0
       val startUpTime = calendar.timeInMillis
/*
        val receiver = this!!.context?.let { ComponentName(it, RemainderBroadcast::class.java) }

        if (receiver != null) {
            context?.packageManager?.setComponentEnabledSetting(
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
            )
        }
*/
       alarmManager.setRepeating(
               AlarmManager.RTC_WAKEUP, startUpTime,startUpTime,
               pendingIntent
       )
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifyLemubit", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}