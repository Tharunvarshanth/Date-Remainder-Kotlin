package com.tvk.dateremainder.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tvk.dateremainder.model.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDAO {

    @Query("SELECT * FROM schedule_table ")
    fun getAlphabetizedWords(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * FROM schedule_table ")
 suspend  fun getalarmcollection(): List<ScheduleEntity>



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: ScheduleEntity)

    @Query("DELETE FROM schedule_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteTask(scheduleentity: ScheduleEntity)

    @Update
    fun updateSchedule(scheduleentity: ScheduleEntity)
}