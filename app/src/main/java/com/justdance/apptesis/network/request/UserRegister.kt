package com.justdance.apptesis.network.request

import com.google.gson.annotations.SerializedName

data class UserRegister(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("career") val career: String,
    @SerializedName("password") val password: String,
    @SerializedName("ci") val ci: String
)
