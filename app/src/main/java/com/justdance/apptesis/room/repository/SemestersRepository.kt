package com.justdance.apptesis.room.repository

import androidx.lifecycle.LiveData
import com.justdance.apptesis.room.dao.SemestersDao
import com.justdance.apptesis.room.entities.Semesters

class SemestersRepository(private val semestersDao: SemestersDao) {
    fun getSemesters(): LiveData<List<Semesters>> {
        return semestersDao.getAll()
    }

    suspend fun insertSemester(session: ArrayList<Semesters>) {
        semestersDao.insertAll(*session.toTypedArray())
    }

    suspend fun deleteSemester(session: Semesters) {
        semestersDao.delete(session)
    }
}