package com.justdance.apptesis.network.response

import com.google.gson.annotations.SerializedName

data class GetSemestersResponse(
    @SerializedName("message") val message: String,
    @SerializedName("semesters") val semesters: List<Semester>,
)

data class Semester(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("from") val start: String,
    @SerializedName("to") val end: String
)