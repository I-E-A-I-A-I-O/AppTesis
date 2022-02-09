package com.justdance.apptesis.room.repository

import androidx.lifecycle.LiveData
import com.justdance.apptesis.room.dao.CoursesDao
import com.justdance.apptesis.room.entities.Courses

class CoursesRepository(private val coursesDao: CoursesDao) {
    fun getSemesterCourses(semesterId: String): LiveData<List<Courses>> {
        return coursesDao.getFromSemester(semesterId)
    }

    suspend fun updateStudents(courseId: String, students: List<String>) {
        coursesDao.updateStudents(courseId, students)
    }

    suspend fun getCourses(): List<Courses> {
        return coursesDao.getAll()
    }

    suspend fun insertCourse(courses: ArrayList<Courses>) {
        coursesDao.insertAll(*courses.toTypedArray())
    }

    suspend fun deleteCourse(course: Courses) {
        coursesDao.delete(course)
    }
}