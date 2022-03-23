package com.justdance.apptesis.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.justdance.apptesis.room.entities.Semesters
import java.time.LocalDate

@Dao
interface SemestersDao {
    @Query("SELECT * FROM semesters")
    fun getAll(): LiveData<List<Semesters>>

    @Query("SELECT * FROM semesters WHERE :currentDate >= semester_start AND :currentDate <= semester_end")
    suspend fun getCurrentSemester(currentDate: LocalDate): Semesters?

    @Query("SELECT * FROM semesters WHERE id = :id")
    suspend fun getById(id: String): Semesters?

    @Update(onConflict = REPLACE)
    suspend fun updateDate(vararg semester: Semesters)

    @Insert
    suspend fun insertAll(vararg semester: Semesters)

    @Delete
    suspend fun delete(semester: Semesters)
}