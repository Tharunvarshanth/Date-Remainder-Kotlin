package com.tvk.dateremainder.uiFragment.update

import android.app.*
import android.content.ComponentName
import android.content.ContentValues
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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.tvk.dateremainder.R
import com.tvk.dateremainder.model.ScheduleEntity
import com.tvk.dateremainder.notification.RemainderBroadcast
import com.tvk.dateremainder.viewmodel.ScheduleLiveViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import java.text.SimpleDateFormat
import java.util.*


class UpdateFragment : Fragment() {



    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var scheduleLiveViewModel:ScheduleLiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_update, container, false)
       createNotificationChannel()


        root.findViewById<EditText>(R.id.editTextTextPersonName_update).setText(args.currenttask.who)
        root.findViewById<EditText>(R.id.editTextTextdesc_update).setText(args.currenttask.desc)
        root.findViewById<Button>(R.id.editTexttime_update).setText(args.currenttask.time)
        root.findViewById<EditText>(R.id.editTextdate_update).setText(args.currenttask.date)

        scheduleLiveViewModel  = ViewModelProvider(this).get(ScheduleLiveViewModel::class.java)

        root.findViewById<Button>(R.id.updatebutton).setOnClickListener {
            updatetodb()
        }
        root.findViewById<Button>(R.id.deletebutton).setOnClickListener {
            deleteTask( args.currenttask)
        }

        root.findViewById<Button>(R.id.editTexttime_update).setOnClickListener{it
            context?.let { it1 ->  setTime( root.findViewById<Button>(R.id.editTexttime_update),it1) }
        }


        return root;
    }

    fun startAlarm(time: String,date: String) {

        var  HOUR = time[0].toString() + time[1].toString()
        var  MIN = time[3].toString() + time[4].toString()




        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var  intent= Intent(context, RemainderBroadcast::class.java);
        intent.putExtra("who",args.currenttask.who)
        intent.putExtra("desc",args.currenttask.desc)
        var  pendingIntent = PendingIntent.getBroadcast(context,args.currenttask.alarm_req_code,intent, PendingIntent.FLAG_ONE_SHOT);

        var list:List<String> = edittextdatedivider(date)

        if(time[6].toString()=="P"){
            HOUR=(HOUR.toInt()+12).toString()
        }

        Log.i("hOUR",list[0])
        val calendar = Calendar.getInstance()

       calendar[Calendar.MONTH]=list[1].toInt()-1
        calendar[Calendar.DAY_OF_MONTH]=list[0].toInt()
        calendar[Calendar.HOUR_OF_DAY] = HOUR.toInt()
        calendar[Calendar.MINUTE] =MIN.toInt()
        calendar[Calendar.SECOND] = 0
        val startUpTime = calendar.timeInMillis

     /*  alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, startUpTime,startUpTime,
             pendingIntent
        )*/

        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000*10
                ,pendingIntent)
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

    private fun updatetodb(){
        val who = editTextTextPersonName_update.text.toString()
        val desc = editTextTextdesc_update.text.toString()
        val time = editTexttime_update.text.toString()
        val date = editTextdate_update.text.toString()

        if(inputCheck(who,desc,time,date) && DatePickerVali(date)){
            val scheduleEntity =
                ScheduleEntity(
                    args.currenttask.id,
                    who,
                    desc,
                    time,
                    date,
                    args.currenttask.alarm_req_code
                )
            scheduleLiveViewModel.updateSchedule(scheduleEntity)
            Toast.makeText(requireContext(),"Successfully Updated", Toast.LENGTH_LONG).show()
            startAlarm(time,date)
             findNavController().navigate(R.id.action_updateFragment_to_nav_home)
        }else{
            Toast.makeText(requireContext(),"Fill out the fields", Toast.LENGTH_LONG).show()
        }
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

    private  fun  inputCheck(who:String,desc:String,time:String,date:String):Boolean{
        return !(TextUtils.isEmpty(who) && TextUtils.isEmpty(desc) && TextUtils.isEmpty(time) && TextUtils.isEmpty(date));
    }

    private fun deleteTask(currentItem:ScheduleEntity){
        lateinit var mAlarmMgr: AlarmManager;
        lateinit var mAlarmIntent:PendingIntent;

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->

            mAlarmMgr = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, RemainderBroadcast::class.java)
            mAlarmIntent = PendingIntent.getBroadcast(context, args.currenttask.alarm_req_code, intent, PendingIntent.FLAG_ONE_SHOT)
            mAlarmMgr.cancel(mAlarmIntent)

            scheduleLiveViewModel.deleteTask(currentItem)
            Toast.makeText(requireContext(),"Successfully Deleted", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_nav_home)
        }
        builder.setNegativeButton("No"){_,_->}
        builder.setTitle("Delete")
        builder.setMessage("Are You Want to Delete ?")
        builder.create().show()

    }

  private  fun setTime(textView: Button, context: Context){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = SimpleDateFormat("hh:mm aa").format(cal.time)
        }


        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false).show()

    }


}
