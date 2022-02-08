package com.justdance.apptesis.room.repository

import com.justdance.apptesis.room.dao.SemestersDao
import com.justdance.apptesis.room.entities.Semesters

class SemestersRepository(private val semestersDao: SemestersDao) {
    suspend fun getSession(): List<Semesters> {
        return semestersDao.getAll()
    }

    suspend fun insertSession(session: Semesters) {
        semestersDao.insertAll(session)
    }

    suspend fun deleteSession(session: Semesters) {
        semestersDao.delete(session)
    }
}