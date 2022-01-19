package com.justdance.apptesis.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {
    private var _emailText = MutableLiveData("")
    val emailText: LiveData<String> = _emailText

    private var _passText = MutableLiveData("")
    val passText: LiveData<String> = _passText

    private var _confirmPassText = MutableLiveData("")
    val confirmPassText: LiveData<String> = _confirmPassText

    private var _nameText = MutableLiveData("")
    val nameText: LiveData<String> = _nameText

    fun nameChanged(newVal: String) {
        _nameText.value = newVal
    }

    fun confirmPassChanged(newVal: String) {
        _confirmPassText.value = newVal
    }

    fun emailChanged(newVal: String) {
        _emailText.value = newVal
    }

    fun passChanged(newVal: String) {
        _passText.value = newVal
    }
}
