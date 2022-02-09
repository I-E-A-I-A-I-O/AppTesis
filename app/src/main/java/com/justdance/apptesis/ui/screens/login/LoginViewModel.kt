package com.justdance.apptesis.ui.screens.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.justdance.apptesis.network.Network
import com.justdance.apptesis.network.request.UserLogin
import com.justdance.apptesis.network.response.LoginResponse
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.entities.Session
import com.justdance.apptesis.room.repository.SessionRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        _isLoading.value = true
        val net = Network()
        net.service.login(UserLogin(ciText.value!!, passText.value!!)).enqueue(
            object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            loginResponse: LoginResponse ->
                            viewModelScope.launch {
                                val result = sessionRepo.getSession()

                                if (result.isNotEmpty())
                                    sessionRepo.deleteSession(result.first())

                                sessionRepo.insertSession(Session(0, loginResponse.token, loginResponse.name, loginResponse.email, loginResponse.ci, loginResponse.role))
                            }.invokeOnCompletion {
                                _isLoading.value = false
                                onResponse(loginResponse.message, true)
                            }
                        }
                    } else {
                        _isLoading.value = false
                        val body = net.parseError(response as Response<Any>)
                        onResponse(body.message, false)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("Network failure", "Login Throwable", t)
                    _isLoading.value = false
                    onResponse("Hubo un error iniciando sesion. Intente de nuevo mas tarde.", false)
                }

            }
        )
    }
}