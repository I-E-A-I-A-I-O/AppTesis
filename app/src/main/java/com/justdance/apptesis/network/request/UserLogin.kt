package com.justdance.apptesis.network.request

import com.google.gson.annotations.SerializedName

data class UserLogin(
    @SerializedName("ci") val ci: String,
    @SerializedName("password") val password: String
)
