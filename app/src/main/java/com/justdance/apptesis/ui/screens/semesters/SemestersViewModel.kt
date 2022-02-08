package com.justdance.apptesis.ui.screens.semesters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdance.apptesis.network.Network
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.repository.SemestersRepository

class SemestersViewModel(app: Application): AndroidViewModel(app) {
    private val semestersRepository: SemestersRepository

    init {
        val database = AppDatabase.getInstance(app.applicationContext)
        val semestersDao = database.semestersDao()
        semestersRepository = SemestersRepository(semestersDao)
    }

    val semesters = semestersRepository.getSemesters()

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun update() {
        _isLoading.value = true
        val net = Network()

    }
}