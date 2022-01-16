package com.justdance.apptesis.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    private var _emailText = MutableLiveData("")
    val emailText: LiveData<String> = _emailText

    private var _passText = MutableLiveData("")
    val passText: LiveData<String> = _passText

    fun emailChanged(newVal: String) {
        _emailText.value = newVal
    }

    fun passChanged(newVal: String) {
        _passText.value = newVal
    }
}