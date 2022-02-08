package com.justdance.apptesis.room.repository

import com.justdance.apptesis.room.dao.CoursesDao
import com.justdance.apptesis.room.entities.Courses

class CoursesRepository(private val coursesDao: CoursesDao) {
    suspend fun getCourses(): List<Courses> {
        return coursesDao.getAll()
    }

    suspend fun insertCourse(session: Courses) {
        coursesDao.insertAll(session)
    }

    suspend fun deleteCourse(session: Courses) {
        coursesDao.delete(session)
    }
}