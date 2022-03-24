package com.justdance.apptesis.network.request

import com.google.gson.annotations.SerializedName

data class CourseJoinRequest (
    @SerializedName("requestId") val requestId: String,
    @SerializedName("operationType") val operationType: String
)