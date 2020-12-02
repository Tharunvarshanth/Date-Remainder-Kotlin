package com.tvk.dateremainder.uiFragment.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tvk.dateremainder.DatePickerFragment
import com.tvk.dateremainder.R
import com.tvk.dateremainder.model.ScheduleEntity
import com.tvk.dateremainder.uiFragment.TimePickerFragment
import com.tvk.dateremainder.viewmodel.ScheduleLiveViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddSchedule : Fragment() {

    private lateinit var addTaskScheduleViewModel: AddScheduleViewModel
    private lateinit var scheduleLiveViewModel: ScheduleLiveViewModel;
    private lateinit var who_edittext:EditText;
    private lateinit var desc_edittext:EditText;
    private  lateinit var time_edittext:Button;
    private  lateinit var date_edittext:EditText;


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        addTaskScheduleViewModel =
                ViewModelProvider(this).get(AddScheduleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addschedule, container, false)

        scheduleLiveViewModel = ViewModelProvider(this).get(ScheduleLiveViewModel::class.java)

        who_edittext = root.findViewById(R.id.editTextTextPersonName)
        desc_edittext = root.findViewById(R.id.editTextTextdesc)
        time_edittext = root.findViewById(R.id.editTexttime)
        date_edittext = root.findViewById(R.id.editTextdate)

        val button = root.findViewById<Button>(R.id.addbutton)

        button.setOnClickListener {
           inserttoDatabase()
         //   finish()
        }

        root.findViewById<Button>(R.id.editTexttime).setOnClickListener{it
            context?.let { it1 -> getTime( root.findViewById<Button>(R.id.editTexttime), it1) }



        }




        return root
    }
    fun getTime(textView: Button, context: Context){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = SimpleDateFormat("hh:mm aa").format(cal.time)
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

       if(inputCheck(who,desc,time,date) && DatePickerVali(date)){
           val scheduleEntity =
               ScheduleEntity(
                   0,
                   who,
                   desc,
                   time,
                   date
               )
           scheduleLiveViewModel.insert(scheduleEntity)
           Toast.makeText(requireContext(),"Successfully Added",Toast.LENGTH_LONG).show()
          // findNavController().navigate(R.id.action_nav_home_to_nav_gallery)
       }else{
           Toast.makeText(requireContext(),"Fill out the fields Or Date Format wrong",Toast.LENGTH_LONG).show()
       }
    }

  private  fun  inputCheck(who:String,desc:String,time:String,date:String):Boolean{
     return !(TextUtils.isEmpty(who) && TextUtils.isEmpty(desc) && TextUtils.isEmpty(time) && TextUtils.isEmpty(date));
  }




}