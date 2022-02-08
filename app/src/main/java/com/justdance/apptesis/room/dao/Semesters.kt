package com.justdance.apptesis.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justdance.apptesis.room.entities.Semesters

@Dao
interface SemestersDao {
    @Query("SELECT * FROM semesters")
    suspend fun getAll(): List<Semesters>

    @Insert
    suspend fun insertAll(vararg semester: Semesters)

    @Delete
    suspend fun delete(semester: Semesters)
}