package com.justdance.apptesis.ui.screens.semesters

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdance.apptesis.room.entities.Semesters
import com.justdance.apptesis.network.Network
import com.justdance.apptesis.network.response.GetSemesterResponse
import com.justdance.apptesis.room.AppDatabase
import com.justdance.apptesis.room.entities.Courses
import com.justdance.apptesis.room.repository.CoursesRepository
import com.justdance.apptesis.room.repository.SemestersRepository
import com.justdance.apptesis.room.repository.SessionRepository
import com.justdance.apptesis.room.repository.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class AddCourseViewModel(app: Application): AndroidViewModel(app) {
    private val coursesRepository: CoursesRepository
    private val usersRepository: UsersRepository
    private val sessionRepository: SessionRepository
    private val semestersRepository: SemestersRepository

    init {
        val database = AppDatabase.getInstance(app.applicationContext)
        val semestersDao = database.semestersDao()
        val sessionDao = database.sessionDao()
        val coursesDao = database.coursesDao()
        val usersDao = database.usersDao()
        semestersRepository = SemestersRepository(semestersDao)
        usersRepository = UsersRepository(usersDao)
        coursesRepository = CoursesRepository(coursesDao)
        sessionRepository = SessionRepository(sessionDao)
    }

    private var _courses = MutableLiveData(listOf<Courses>())
    val courses: LiveData<List<Courses>> = _courses

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCourses() {
        _isLoading.value = true

        CoroutineScope(Dispatchers.IO).launch {
            val semester = semestersRepository.currentSemester(LocalDate.now())
            var savedCourses = listOf<Courses>()

            semester?.let {
                savedCourses = coursesRepository.getSemesterCourses(it.id)
            }

            CoroutineScope(Dispatchers.Main).launch {
                _courses.value = savedCourses
            }

            val token = sessionRepository.getToken()
            val network = Network()
            network.service.getCurrentSemester(token).enqueue(
                object: Callback<GetSemesterResponse> {
                    override fun onResponse(
                        call: Call<GetSemesterResponse>,
                        response: Response<GetSemesterResponse>
                    ) {
                        if (!response.isSuccessful) {
                            _isLoading.value = false
                            return
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            response.body()?.let { body ->
                                val toDB = arrayListOf<Courses>()

                                if (!semestersRepository.isIdRegistered(body.semesterId)) {
                                    semestersRepository.insertSemester(
                                        Semesters(
                                            body.semesterId,
                                            body.semesterName,
                                            LocalDate.parse(body.start),
                                            LocalDate.parse(body.end)
                                        )
                                    )
                                }

                                body.courses.forEach { Course ->
                                    if (Course != null) {
                                        val c = savedCourses.find {
                                            it.id == Course.id && it.group == Course.group
                                        }

                                        if (c == null) {
                                            toDB.add(
                                                Courses(
                                                    Course.id,
                                                    Course.name,
                                                    body.semesterId,
                                                    Course.group,
                                                    Course.teacherId,
                                                    listOf()
                                                )
                                            )
                                        }
                                    }
                                }

                                val toDelete = savedCourses.mapNotNull { localCourse ->
                                    val inRemote = body.courses.find { remoteCourse ->
                                        remoteCourse.id == localCourse.id
                                    }

                                    if (inRemote == null)
                                        localCourse
                                    else
                                        null
                                }

                                if (toDelete.isNotEmpty()) {
                                    coursesRepository.deleteCourses(toDelete.toTypedArray())
                                    CoroutineScope(Dispatchers.Main).launch {
                                        _courses.value = _courses.value?.minus(toDelete)
                                    }
                                }

                                if (toDB.isNotEmpty()) {
                                    coursesRepository.insertCourse(toDB)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        _courses.value = _courses.value?.plus(toDB)
                                    }
                                }
                            }
                        }

                        _isLoading.value = false
                    }

                    override fun onFailure(call: Call<GetSemesterResponse>, t: Throwable) {
                        Log.e("HTTP Request", "GET courses error", t)
                        _isLoading.value = false
                    }
                }
            )
        }
    }
}