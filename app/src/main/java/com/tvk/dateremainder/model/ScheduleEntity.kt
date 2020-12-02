package com.tvk.dateremainder.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "schedule_table")
data class ScheduleEntity (
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id") val id: Int,
        @ColumnInfo(name = "who") val who:String,
        @ColumnInfo(name = "description") val desc:String,
        @ColumnInfo(name = "time")val time:String,
        @ColumnInfo(name = "date") val date:String
):Parcelable
