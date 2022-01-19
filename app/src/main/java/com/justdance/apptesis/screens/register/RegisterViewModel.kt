package com.justdance.apptesis.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justdance.apptesis.network.Network
import com.justdance.apptesis.network.response.GenericResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

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

    fun register(career: String, onResponse: (message: String, success: Boolean) -> Unit) = effect {
        _isLoading.value = true
        Network().service.register(
            _nameText.value!!,
            _emailText.value!!,
            career,
            _passText.value!!
        ).enqueue(object: Callback<GenericResponse> {
            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {
                _isLoading.value = false
                response.body()?.let {
                    onResponse(it.message, true)
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                _isLoading.value = false
                onResponse("Hubo un error creando la cuenta. Intente de nuevo mas tarde.", false)
            }

        })
    }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { block() }
    }
}
