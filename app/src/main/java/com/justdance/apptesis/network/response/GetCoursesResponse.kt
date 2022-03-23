package com.justdance.apptesis.network.response

import com.google.gson.annotations.SerializedName

data class GetSemesterCoursesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("courses") val courses: List<SemesterCourse>
)

data class GetSemesterResponse(
    @SerializedName("message") val message: String,
    @SerializedName("semesterId") val semesterId: String,
    @SerializedName("semesterName") val semesterName: String,
    @SerializedName("from") val start: String,
    @SerializedName("to") val end: String,
    @SerializedName("courses") val courses: List<SemesterCourse>
)

data class SemesterCourse(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("teacher") val teacherId: String,
    @SerializedName("group") val group: String,
    @SerializedName("students") val students: List<String>?
)

data class User(
    @SerializedName("_id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("ci") val ci: String,
    @SerializedName("name") val name: String
)
