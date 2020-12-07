package com.tvk.dateremainder.uiFragment.schedule

import android.app.*
import android.content.ComponentName
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tvk.dateremainder.R
import com.tvk.dateremainder.model.ScheduleEntity
import com.tvk.dateremainder.notification.RemainderBroadcast
import com.tvk.dateremainder.uiFragment.home.HomeSchdeuleViewFragment
import com.tvk.dateremainder.uiFragment.home.ListAdapter
import com.tvk.dateremainder.viewmodel.ScheduleLiveViewModel
import java.text.SimpleDateFormat
import java.util.*


class AddSchedule : Fragment() {

    private lateinit var addTaskScheduleViewModel: AddScheduleViewModel
    private lateinit var scheduleLiveViewModel: ScheduleLiveViewModel;
    private lateinit var who_edittext:EditText;
    private lateinit var desc_edittext:EditText;
    private  lateinit var time_edittext:Button;
    private  lateinit var date_edittext:EditText;

    private  lateinit var mAlarmMgr: AlarmManager;
    private lateinit var mAlarmIntent:PendingIntent;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        addTaskScheduleViewModel =
                ViewModelProvider(this).get(AddScheduleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addschedule, container, false)
     //   createNotificationChannel()

        scheduleLiveViewModel = ViewModelProvider(this).get(ScheduleLiveViewModel::class.java)

        who_edittext = root.findViewById(R.id.editTextTextPersonName)
        desc_edittext = root.findViewById(R.id.editTextTextdesc)
        time_edittext = root.findViewById(R.id.editTexttime)
        date_edittext = root.findViewById(R.id.editTextdate)
        root.findViewById<Button>(R.id.editTexttime).setOnClickListener{it
            context?.let { it1 -> getTime( root.findViewById<Button>(R.id.editTexttime), it1) }
        }

        val button = root.findViewById<Button>(R.id.addbutton)

        button.setOnClickListener {
          inserttoDatabase()

         //   finish()
        }






        return root
    }
    fun startAlarm(time: String,date: String) {


        var  HOUR = time[0].toString() + time[1].toString()
        var  MIN = time[3].toString() + time[4].toString()

        Log.i(ContentValues.TAG, "startAlarm")

        Log.i("hOUR", MIN)
        if(time[6].toString()=="P"){
            HOUR=(HOUR.toInt()+12).toString()
        }




        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var  intent= Intent(context, RemainderBroadcast::class.java);
        intent.putExtra("who",who_edittext.toString())
        intent.putExtra("desc",desc_edittext.toString())
        var  pendingIntent = PendingIntent.getBroadcast(context,HomeSchdeuleViewFragment.recycleviewSize,intent, PendingIntent.FLAG_ONE_SHOT);


        var list:List<String> = edittextdatedivider(date)

        val calendar = Calendar.getInstance()


        calendar[Calendar.MONTH] = list[1].toInt()-1
        calendar[Calendar.DAY_OF_MONTH] = list[0].toInt()
        calendar[Calendar.HOUR_OF_DAY] = HOUR.toInt()
        calendar[Calendar.MINUTE] = MIN.toInt()
        calendar[Calendar.SECOND] = 0
        val startUpTime = calendar.timeInMillis



       alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, startUpTime,startUpTime,
            pendingIntent
        )
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
                    requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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






    fun getTime(textView: Button, context: Context){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = SimpleDateFormat("hh:mm aa ").format(cal.time)
        }


            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false).show()

    }

    fun DatePickerVali(date: String) :Boolean{
         if(date.matches("^[a-zA-Z]*$".toRegex()))
         {
             return false
         }
        if(date.contains("/")  ){
            val strarray = date.split("/")
            if( daychecker(strarray[0].toInt()) && monthchecker(strarray[1].toInt()) ){
                 return true
            }
            else{
                return false
            }
        }
         else if(date.contains("-"))   {
            val strarray = date.split("-")
            if( daychecker(strarray[0].toInt()) && monthchecker(strarray[1].toInt()) ){
                return true
            }
            else{
                return false
            }
        }
 return false

    }
    fun monthchecker(num:Int):Boolean{
        if(num<13&&num>0)
            return true
        return false
    }
    fun daychecker(num:Int):Boolean{
         if(num<32&&num>0)
           return true
        return false
    }


   private fun inserttoDatabase(){
      val who = who_edittext.text.toString();
       val desc = desc_edittext.text.toString();
       val time = time_edittext.text.toString()
       val date = date_edittext.text.toString();
       Log.d("Size", ListAdapter().itemCount.toString())

       if(inputCheck(who,desc,time,date) && DatePickerVali(date)){
           val scheduleEntity =
               ScheduleEntity(
                   0,
                   who,
                   desc,
                   time,
                   date,
                       HomeSchdeuleViewFragment.recycleviewSize
               )
           scheduleLiveViewModel.insert(scheduleEntity)

           Toast.makeText(requireContext(),"Successfully Added",Toast.LENGTH_LONG).show()
           startAlarm(time,date)
          // return true;
         // findNavController().navigate(R.id.action_nav_home_to_nav_gallery)
       }else{
           Toast.makeText(requireContext(),"Fill out the fields Or Date Format wrong",Toast.LENGTH_LONG).show()
          //return false;
       }
    }

  private  fun  inputCheck(who:String,desc:String,time:String,date:String):Boolean{
     return !(TextUtils.isEmpty(who) && TextUtils.isEmpty(desc) && TextUtils.isEmpty(time) && TextUtils.isEmpty(date));
  }






}