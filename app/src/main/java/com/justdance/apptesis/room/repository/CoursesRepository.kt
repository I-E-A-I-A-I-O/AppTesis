package com.justdance.apptesis.room.repository

import androidx.lifecycle.LiveData
import com.justdance.apptesis.room.dao.CoursesDao
import com.justdance.apptesis.room.entities.Courses

class CoursesRepository(private val coursesDao: CoursesDao) {
    suspend fun getSemesterCourses(semesterId: String): List<Courses> {
        return coursesDao.getFromSemester(semesterId)
    }

    suspend fun getSemesterCoursesUser(semesterId: String, userId: String): List<Courses> {
        val courses = coursesDao.getFromSemester(semesterId)
        return courses.filter { it.students.contains(userId) }
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

    suspend fun deleteCourses(courses: Array<Courses>) {
        coursesDao.delete(*courses)
    }
}