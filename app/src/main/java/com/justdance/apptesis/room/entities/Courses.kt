package com.justdance.apptesis.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["id", "semester", "group"])
data class Courses(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "course_name") val name: String,
    @ColumnInfo(name = "semester") val semester: String,
    @ColumnInfo(name = "group") val group: String,
    @ColumnInfo(name = "teacher") val teacher: String,
    @ColumnInfo(name = "students") val students: List<String>
)
