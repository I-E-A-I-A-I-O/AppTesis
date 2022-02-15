package com.justdance.apptesis.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Semesters(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "semester_name") val name: String,
    @ColumnInfo(name = "semester_start") val from: LocalDate,
    @ColumnInfo(name = "semester_end") val to: LocalDate
)
