package com.justdance.apptesis.network.response

import com.google.gson.annotations.SerializedName

data class GenericResponse (
    @SerializedName("message") val message: String = "Error procesando la operacion."
)