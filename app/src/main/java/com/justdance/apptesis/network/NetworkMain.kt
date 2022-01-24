package com.justdance.apptesis.network

import com.justdance.apptesis.network.request.SessionVerify
import com.justdance.apptesis.network.request.UserLogin
import com.justdance.apptesis.network.request.UserRegister
import com.justdance.apptesis.network.response.GenericResponse
import com.justdance.apptesis.network.response.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.IOException
import java.util.concurrent.TimeUnit

interface Requests {
    @Headers("Content-Type: application/json")
    @POST("users")
    fun register(@Body registerForm: UserRegister): Call<GenericResponse>

    @Headers("Content-Type: application/json")
    @POST("users/user")
    fun login(@Body loginForm: UserLogin): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("users/user/token")
    fun session(@Body token: SessionVerify): Call<GenericResponse>
}

class Network {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.190:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    var service: Requests = retrofit.create(Requests::class.java)

    fun parseError(response: Response<Any>): GenericResponse {
        val converter: Converter<ResponseBody, GenericResponse> =
            retrofit.responseBodyConverter(GenericResponse::class.java, arrayOfNulls(0))
        return try {
            val responseBody = response.errorBody()
            if (responseBody != null) {
                converter.convert(responseBody) ?: GenericResponse()
            } else {
                GenericResponse()
            }
        } catch (e: IOException) {
            GenericResponse()
        }
    }
}
