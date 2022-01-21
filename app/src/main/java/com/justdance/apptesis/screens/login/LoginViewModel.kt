package com.justdance.apptesis.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
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
}