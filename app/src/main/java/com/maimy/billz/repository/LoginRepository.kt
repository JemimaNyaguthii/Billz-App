package com.maimy.billz.repository

import com.maimy.billz.api.ApiClient
import com.maimy.billz.api.ApiInterface
import com.maimy.billz.model.LoginRequest
import com.maimy.billz.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository {
    val apiClient= ApiClient.buildClient(ApiInterface::class.java)
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return withContext(Dispatchers.IO){
            apiClient.loginUser(loginRequest)
        }
    }

}
