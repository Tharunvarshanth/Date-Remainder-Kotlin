package com.tvk.dateremainder.uiFragment.update

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tvk.dateremainder.R
import com.tvk.dateremainder.model.ScheduleEntity
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
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_update, container, false)

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
                    date
                )
            scheduleLiveViewModel.updateSchedule(scheduleEntity)
            Toast.makeText(requireContext(),"Successfully Updated", Toast.LENGTH_LONG).show()
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
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
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
