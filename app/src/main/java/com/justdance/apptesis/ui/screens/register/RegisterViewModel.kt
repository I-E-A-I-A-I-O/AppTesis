package com.justdance.apptesis.ui.screens.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.justdance.apptesis.network.Network
import com.justdance.apptesis.network.request.UserRegister
import com.justdance.apptesis.network.response.GenericResponse
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

    private var _ciText = MutableLiveData("")
    val ciText: LiveData<String> = _ciText

    fun ciChanged(newVal: String) {
        _ciText.value = newVal
    }

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

    fun register(career: String, onResponse: (message: String, success: Boolean) -> Unit)  {
        _isLoading.value = true
        val net = Network()
        net.service.register(UserRegister(
            _nameText.value!!,
            _emailText.value!!.lowercase(),
            career,
            _passText.value!!,
            _ciText.value!!
        )).enqueue(object: Callback<GenericResponse> {
            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {
                _isLoading.value = false
                Log.d("FLAG", "IS SUS ${response.isSuccessful}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResponse(it.message, true)
                    }
                } else {
                    val body = net.parseError(response as Response<Any>)
                    onResponse(body.message, false)
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.d("Error", "whaaaaaaaaaaaaaaaaaaaaaaaaaaaat", t)
                _isLoading.value = false
                onResponse("Hubo un error creando la cuenta. Intente de nuevo mas tarde.", false)
            }

        })
    }
}
