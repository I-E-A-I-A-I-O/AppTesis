package com.justdance.apptesis.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String
)