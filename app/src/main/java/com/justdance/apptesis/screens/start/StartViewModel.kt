package com.justdance.apptesis.screens.start

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.justdance.apptesis.network.Network
import com.justdance.apptesis.network.request.SessionVerify
import com.justdance.apptesis.network.response.GenericResponse
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.entities.Session
import com.justdance.apptesis.room.repository.SessionRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartViewModel(app: Application): AndroidViewModel(app) {
    private val sessionRepo: SessionRepository

    init {
        val database = AppDatabase.getInstance(app.applicationContext)
        val sessionDao = database.sessionDao()
        sessionRepo = SessionRepository(sessionDao)
    }

    fun verifySession(onResponse: (message: String, success: Boolean) -> Unit) {
        var session: Session? = null
        viewModelScope.launch {
            val list = sessionRepo.getSession()
            if (list.isNotEmpty())
                session = list.first()
        }.invokeOnCompletion {
            if (session == null) {
                onResponse("", false)
            } else {
                val net = Network()
                net.service.session(SessionVerify(session!!.token)).enqueue(
                    object: Callback<GenericResponse> {
                        override fun onResponse(
                            call: Call<GenericResponse>,
                            response: Response<GenericResponse>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    onResponse(it.message, true)
                                }
                            } else {
                                viewModelScope.launch {
                                    sessionRepo.deleteSession(session!!)
                                }.invokeOnCompletion {
                                    val body = net.parseError(response as Response<Any>)
                                    onResponse(body.message, false)
                                }
                            }
                        }

                        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                            Log.d("auto login error", "throwable", t)
                            onResponse("Error iniciando sesion.", false)
                        }

                    }
                )
            }
        }
    }
}