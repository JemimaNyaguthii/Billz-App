package com.example.billsmanagement.api

import com.example.billsmanagement.model.LoginRequest
import com.example.billsmanagement.model.LoginResponse
import com.example.billsmanagement.model.RegisterRequest
import com.example.billsmanagement.model.RegisterResponse
import com.example.billsmanagement.ui.Login
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest):Response<RegisterResponse>
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest):Response<LoginResponse>
}