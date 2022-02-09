package com.justdance.apptesis.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdance.apptesis.network.Network
import com.justdance.apptesis.network.response.GetSemesterCoursesResponse
import com.justdance.apptesis.network.response.GetSemestersResponse
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.entities.Courses
import com.justdance.apptesis.room.entities.Semesters
import com.justdance.apptesis.room.repository.CoursesRepository
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
    private val coursesRepository: CoursesRepository

    init {
        val database = AppDatabase.getInstance(app.applicationContext)
        val semestersDao = database.semestersDao()
        val sessionDao = database.sessionDao()
        val coursesDao = database.coursesDao()
        coursesRepository = CoursesRepository(coursesDao)
        sessionRepository = SessionRepository(sessionDao)
        semestersRepository = SemestersRepository(semestersDao)
    }

    val semesters = semestersRepository.getSemesters()
    var courses = coursesRepository.getSemesterCourses("")

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _role = MutableLiveData("not")
    val role: LiveData<String> = _role


    fun getCourses(id: String) {
        _isLoading.value = true
        courses = coursesRepository.getSemesterCourses(id)

        CoroutineScope(Dispatchers.IO).launch {
            val token = sessionRepository.getToken()
            val network = Network()
            network.service.getSemesterCourses(id, token).enqueue(
                object: Callback<GetSemesterCoursesResponse> {
                    override fun onResponse(
                        call: Call<GetSemesterCoursesResponse>,
                        response: Response<GetSemesterCoursesResponse>
                    ) {
                        if (!response.isSuccessful) {
                            _isLoading.value = false
                            return
                        }

                        response.body()?.let { body ->
                            val toDB = arrayListOf<Courses>()

                            body.courses.forEach { Course ->
                                val c = courses.value?.find {
                                    it.id == Course.id
                                }

                                if (c == null) {
                                    toDB.add(
                                        Courses(
                                            Course.id,
                                            Course.name,
                                            id,
                                            Course.teacherId,
                                            listOf()
                                        )
                                    )
                                }
                            }

                            if (toDB.isNotEmpty()) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    coursesRepository.insertCourse(toDB)
                                }
                            }
                        }

                        _isLoading.value = false
                    }

                    override fun onFailure(call: Call<GetSemesterCoursesResponse>, t: Throwable) {
                        _isLoading.value = false
                    }
                }
            )
        }
    }

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