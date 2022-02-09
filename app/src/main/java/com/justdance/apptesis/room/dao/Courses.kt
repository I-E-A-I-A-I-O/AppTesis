package com.justdance.apptesis.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justdance.apptesis.room.entities.Courses

@Dao
interface CoursesDao {
    @Query("SELECT * FROM courses")
    suspend fun getAll(): List<Courses>

    @Insert
    suspend fun insertAll(vararg courses: Courses)

    @Delete
    suspend fun delete(course: Courses)
}