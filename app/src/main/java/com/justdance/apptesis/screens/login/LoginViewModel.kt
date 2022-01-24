package com.justdance.apptesis.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.repository.SessionRepository

class LoginViewModel(app: Application): AndroidViewModel(app) {
    private val sessionRepo: SessionRepository

    init {
        val database = AppDatabase.getInstance(app.applicationContext)
        val sessionDao = database.sessionDao()
        sessionRepo = SessionRepository(sessionDao)
    }

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _ciText = MutableLiveData("")
    val ciText: LiveData<String> = _ciText

    private var _passText = MutableLiveData("")
    val passText: LiveData<String> = _passText

    fun ciChanged(newVal: String) {
        _ciText.value = newVal
    }

    fun passChanged(newVal: String) {
        _passText.value = newVal
    }

    fun login(onResponse: (message: String, success: Boolean) -> Unit) {

    }
}