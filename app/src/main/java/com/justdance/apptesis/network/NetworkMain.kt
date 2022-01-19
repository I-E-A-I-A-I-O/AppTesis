package com.justdance.apptesis.network

import com.justdance.apptesis.network.response.GenericResponse
import com.justdance.apptesis.network.response.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Requests {
    @Headers("Content-Type: application/json")
    @POST("users")
    fun register(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("career") career: String,
        @Query("password") password: String
    ): Call<GenericResponse>

    @Headers("Content-Type: application/json")
    @POST("users/user")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<LoginResponse>
}

class Network {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    var service: Requests = retrofit.create(Requests::class.java)
}
