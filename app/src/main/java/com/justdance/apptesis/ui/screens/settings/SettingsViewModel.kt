package com.justdance.apptesis.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.justdance.apptesis.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val app: Application): AndroidViewModel(app) {
    fun clearDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabase.getInstance(app.applicationContext)
            database.clearAllTables()
        }
    }
}