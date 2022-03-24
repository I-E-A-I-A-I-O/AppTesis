package com.justdance.apptesis.ui.screens.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.repository.CoursesRepository
import com.justdance.apptesis.room.repository.SessionRepository

class NotificationsViewModel(app: Application): AndroidViewModel(app) {
    private val coursesRepository: CoursesRepository
    private val sessionRepository: SessionRepository

    init {
        val database = AppDatabase.getInstance(app.applicationContext)
        val sessionDao = database.sessionDao()
        val coursesDao = database.coursesDao()
        coursesRepository = CoursesRepository(coursesDao)
        sessionRepository = SessionRepository(sessionDao)
    }
}