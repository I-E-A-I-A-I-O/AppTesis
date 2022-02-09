package com.justdance.apptesis.network.response

import com.google.gson.annotations.SerializedName

data class GetSemesterCoursesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("courses") val courses: List<SemesterCourse>
)

data class SemesterCourse(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("teacher") val teacherId: String
)
