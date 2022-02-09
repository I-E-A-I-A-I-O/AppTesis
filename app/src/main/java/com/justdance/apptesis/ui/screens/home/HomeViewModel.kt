package com.justdance.apptesis.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdance.apptesis.network.Network
import com.justdance.apptesis.network.response.GetSemestersResponse
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.entities.Semesters
import com.justdance.apptesis.room.repository.SemestersRepository
import com.justdance.apptesis.room.repository.SessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(app: Application): AndroidViewModel(app) {
    private val semestersRepository: SemestersRepository
    private val sessionRepository: SessionRepository

    init {
        val database = AppDatabase.getInstance(app.applicationContext)
        val semestersDao = database.semestersDao()
        val sessionDao = database.sessionDao()
        sessionRepository = SessionRepository(sessionDao)
        semestersRepository = SemestersRepository(semestersDao)
    }

    val semesters = semestersRepository.getSemesters()

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _role = MutableLiveData("not")
    val role: LiveData<String> = _role

    fun getRole() {
        CoroutineScope(Dispatchers.IO).launch {
            val role = sessionRepository.getRole()
            CoroutineScope(Dispatchers.Main).launch {
                _role.value = role
            }
        }
    }

    fun update() {
        _isLoading.value = true
        val net = Network()
        net.service.getSemesters().enqueue(
            object: Callback<GetSemestersResponse> {
                override fun onResponse(
                    call: Call<GetSemestersResponse>,
                    response: Response<GetSemestersResponse>
                ) {
                    if (response.isSuccessful) {
                        CoroutineScope(Dispatchers.IO).launch {
                            response.body()?.let { reBody: GetSemestersResponse ->
                                val toDB: ArrayList<Semesters> = arrayListOf()

                                reBody.semesters.forEach { Semester ->
                                    val s = semesters.value?.find {
                                        return@find it.id == Semester.id
                                    }

                                    if (s == null) {
                                        toDB.add(Semesters(Semester.id, Semester.name, Semester.start, Semester.end))
                                    }
                                }

                                if (toDB.isNotEmpty()) {
                                    semestersRepository.insertSemester(toDB)
                                }
                            }
                        }
                    }
                    _isLoading.value = false
                }

                override fun onFailure(call: Call<GetSemestersResponse>, t: Throwable) {
                    _isLoading.value = false
                }
            }
        )
    }
}