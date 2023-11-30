package com.maimy.billz.repository

import com.maimy.billz.api.ApiClient
import com.maimy.billz.api.ApiInterface
import com.maimy.billz.model.RegisterRequest
import com.maimy.billz.model.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository {
    val apiClient=ApiClient.buildClient(ApiInterface::class.java)
    suspend fun registerUser(registerRequest: RegisterRequest):Response<RegisterResponse>{
        return withContext(Dispatchers.IO){
            apiClient.registerUser(registerRequest)
        }
    }

}