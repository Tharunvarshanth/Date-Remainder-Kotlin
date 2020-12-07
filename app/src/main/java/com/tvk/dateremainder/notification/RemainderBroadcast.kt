package com.tvk.dateremainder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tvk.dateremainder.R
import com.tvk.dateremainder.uiFragment.home.HomeSchdeuleViewFragment
import java.util.*


class RemainderBroadcast : BroadcastReceiver() {


    override fun onReceive(p0: Context, p1: Intent) {
        Log.d("RemainderBroadcast1", "passed")


        if (p1.action == "android.intent.action.BOOT_COMPLETED") {

            val bundle: Bundle = p1.extras!!
            val id = bundle.get("who")
            val language = bundle.get("desc")

            Log.d("RemainderBroadcast2", "passed")
            var builder = p0.let {
                NotificationCompat.Builder(it, "notifyLemubit")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle(HomeSchdeuleViewFragment.recycleviewSize.toString())
                        .setContentText(id.toString())
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(HomeSchdeuleViewFragment.alarmlist[0].who)
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)

            }

            with(p0?.let { NotificationManagerCompat.from(it) }) {
                // notificationId is a unique int for each notification that you must define
                this?.notify(200, builder!!.build())
            }

         }else{
          //  list = scheduleLiveViewModel.list


        val bundle: Bundle = p1.extras!!
        val id = bundle.get("who")
        val language = bundle.get("desc")

        Log.d("RemainderBroadcast2", "passed")
        var builder = p0.let {
            NotificationCompat.Builder(it, "notifyLemubit")
                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                    .setContentTitle(id.toString())
                    .setContentText(id.toString())
                    .setStyle(
                            NotificationCompat.BigTextStyle()
                                    .bigText(language.toString())
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        }

        with(p0?.let { NotificationManagerCompat.from(it) }) {
            // notificationId is a unique int for each notification that you must define
            this?.notify(200, builder!!.build())
        }


        //  }
    }
}

    fun startAlarm(time: String,date: String,p0: Context, p1: Intent) {

        var  HOUR = time[0].toString() + time[1].toString()
        var  MIN = time[3].toString() + time[4].toString()


        Log.i("Updatefragmentreqcode", "")

       val alarmManager = p0.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var  intent= Intent(p0, RemainderBroadcast::class.java);
        intent.putExtra("who","")
        intent.putExtra("desc","")
        var  pendingIntent = PendingIntent.getBroadcast(p0,0,intent, PendingIntent.FLAG_ONE_SHOT);

        var list:List<String> = edittextdatedivider(date)

        if(time[6].toString()=="P"){
            HOUR=(HOUR.toInt()+12).toString()
        }

        Log.i("hOUR", HOUR)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis

        calendar[Calendar.MONTH]=list[1].toInt()-1
        calendar[Calendar.DAY_OF_MONTH]=list[0].toInt()
        calendar[Calendar.HOUR_OF_DAY] = HOUR.toInt()
        calendar[Calendar.MINUTE] =MIN.toInt()
        calendar[Calendar.SECOND] = 0
        val startUpTime = calendar.timeInMillis

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, startUpTime,startUpTime,
                pendingIntent
        )

        //   mAlarmMgr.set(AlarmManager.RTC_WAKEUP,startUpTime,mAlarmIntent)
    }

    private fun edittextdatedivider(date:String):List<String>{
        lateinit var list:List<String>;

        if(date.contains("/")  ){
            list =   date.split("/")
        }
        else if(date.contains("-") ){
            list =  date.split("-")
        }
        return list

    }

}
/*
private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult: PendingResult = goAsync()
        val asyncTask = Task(pendingResult, intent)
        asyncTask.execute()
    }

    private class Task(
            private val pendingResult: PendingResult,
            private val intent: Intent
    ) : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String?): String {
            val sb = StringBuilder()
            sb.append("Action: ${intent.action}\n")
            sb.append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
            return toString().also { log ->
                Log.d(TAG, log)
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish()
        }
    }
}*/

