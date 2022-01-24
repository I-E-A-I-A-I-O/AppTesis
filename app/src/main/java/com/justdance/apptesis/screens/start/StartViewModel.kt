package com.justdance.apptesis.screens.start

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.repository.SessionRepository

class StartViewModel(app: Application): AndroidViewModel(app) {
    private val sessionRepo: SessionRepository

    init {
        val database = AppDatabase.getInstance(app.applicationContext)
        val sessionDao = database.sessionDao()
        sessionRepo = SessionRepository(sessionDao)
    }

    fun verifySession(onResponse: (message: String, success: Boolean) -> Unit) {

    }
}