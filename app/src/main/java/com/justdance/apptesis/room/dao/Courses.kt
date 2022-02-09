package com.justdance.apptesis.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justdance.apptesis.room.entities.Courses

@Dao
interface CoursesDao {
    @Query("SELECT * FROM courses")
    suspend fun getAll(): List<Courses>

    @Query("SELECT * FROM courses WHERE semester=:semesterId")
    fun getFromSemester(semesterId: String): LiveData<List<Courses>>

    @Query("UPDATE courses SET students=:students WHERE id=:courseId")
    suspend fun updateStudents(courseId: String, students: List<String>)

    @Insert
    suspend fun insertAll(vararg courses: Courses)

    @Delete
    suspend fun delete(course: Courses)
}