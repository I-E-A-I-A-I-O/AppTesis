package com.justdance.apptesis.room.repository

import com.justdance.apptesis.room.dao.CoursesDao
import com.justdance.apptesis.room.entities.Courses

class CoursesRepository(private val coursesDao: CoursesDao) {
    suspend fun getSession(): List<Courses> {
        return coursesDao.getAll()
    }

    suspend fun insertSession(session: Courses) {
        coursesDao.insertAll(session)
    }

    suspend fun deleteSession(session: Courses) {
        coursesDao.delete(session)
    }
}