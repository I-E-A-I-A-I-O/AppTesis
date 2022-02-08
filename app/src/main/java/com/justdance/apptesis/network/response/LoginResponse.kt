package com.justdance.apptesis.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("ci") val ci: String,
    @SerializedName("role") val role: String
)