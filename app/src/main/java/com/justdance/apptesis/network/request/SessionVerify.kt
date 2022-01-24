package com.justdance.apptesis.network.request

import com.google.gson.annotations.SerializedName

data class SessionVerify(
    @SerializedName("token") val token: String
)
