package com.tvk.dateremainder.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.tvk.dateremainder.model.ScheduleEntity
import com.tvk.dateremainder.repositary.ScheduleRepositary
import com.tvk.dateremainder.db.ScheduleRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleLiveViewModel(application: Application) : AndroidViewModel(application) {

   val readAllData :LiveData<List<ScheduleEntity>> ;

   private val repository: ScheduleRepositary;

    init{
        val scheduleDao = ScheduleRoomDatabase.getDatabase(application).scheduleDao()
        repository =
            ScheduleRepositary(scheduleDao)
        readAllData = repository.allWords
    }




    fun insert(scheduleEntity: ScheduleEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(scheduleEntity)
    }

    fun updateSchedule(scheduleEntity: ScheduleEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSchedule(scheduleEntity)
        }
    }

    fun deleteTask(scheduleEntity: ScheduleEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(scheduleEntity)
        }
    }

}
/*
class ScheduleLiveViewModelFactory(private val repository: ScheduleRepositary) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleLiveViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleLiveViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/