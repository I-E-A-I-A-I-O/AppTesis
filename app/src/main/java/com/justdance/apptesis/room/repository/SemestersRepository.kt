package com.justdance.apptesis.room.repository

import androidx.lifecycle.LiveData
import com.justdance.apptesis.room.dao.SemestersDao
import com.justdance.apptesis.room.entities.Semesters
import java.time.LocalDate

class SemestersRepository(private val semestersDao: SemestersDao) {
    fun getSemesters(): LiveData<List<Semesters>> {
        return semestersDao.getAll()
    }

    suspend fun currentSemester(currentDate: LocalDate): Semesters? {
        return semestersDao.getCurrentSemester(currentDate)
    }

    suspend fun isIdRegistered(id: String): Boolean {
        val semester = semestersDao.getById(id)
        return semester != null
    }

    suspend fun insertSemester(semesters: ArrayList<Semesters>) {
        semestersDao.insertAll(*semesters.toTypedArray())
    }

    suspend fun updateSemesterDate(semesters: ArrayList<Semesters>) {
        semestersDao.updateDate(*semesters.toTypedArray())
    }

    suspend fun insertSemester(semester: Semesters) {
        semestersDao.insertAll(semester)
    }

    suspend fun deleteSemester(semester: Semesters) {
        semestersDao.delete(semester)
    }
}