package com.tvk.dateremainder.repositary
import android.os.AsyncTask
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.tvk.dateremainder.db.ScheduleDAO
import com.tvk.dateremainder.model.ScheduleEntity


class ScheduleRepositary(private val scheduleDAO: ScheduleDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: LiveData<List<ScheduleEntity>> = scheduleDAO.getAlphabetizedWords()


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(scheduleEntity: ScheduleEntity) {

        scheduleDAO.insert(scheduleEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getalarms():List<ScheduleEntity> {

     return   scheduleDAO.getalarmcollection()
    }


    suspend fun updateSchedule(scheduleEntity: ScheduleEntity){
        scheduleDAO.updateSchedule(scheduleEntity)
    }

    suspend fun deleteTask(scheduleEntity: ScheduleEntity){
        scheduleDAO.deleteTask(scheduleEntity)
    }



}