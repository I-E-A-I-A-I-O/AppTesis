package com.justdance.apptesis.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = Semesters::class, parentColumns = arrayOf("id"), childColumns = arrayOf("teacher"), onDelete = NO_ACTION, onUpdate = CASCADE),
    ForeignKey(entity = Users::class, parentColumns = arrayOf("id"), childColumns = arrayOf("students"), onDelete = NO_ACTION, onUpdate = CASCADE)
])
data class Courses(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "course_name") val name: String,
    @ColumnInfo(name = "teacher") val teacher: String,
    @ColumnInfo(name = "students") val students: List<String>
)
