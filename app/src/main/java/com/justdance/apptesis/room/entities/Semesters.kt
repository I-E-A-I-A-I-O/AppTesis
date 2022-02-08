package com.justdance.apptesis.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Semesters(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "semester_name") val name: String,
    @ColumnInfo(name = "semester_start") val from: String,
    @ColumnInfo(name = "semester_end") val to: String
)
