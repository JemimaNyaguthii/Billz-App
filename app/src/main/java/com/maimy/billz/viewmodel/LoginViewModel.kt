package com.maimy.billz.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maimy.billz.model.LoginRequest
import com.maimy.billz.model.LoginResponse
import com.maimy.billz.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel:ViewModel(){
        val loginRepository = LoginRepository()
        val errorLiveData = MutableLiveData<String>()
        val logLiveData= MutableLiveData<LoginResponse>()

        fun loginUser(loginRequest: LoginRequest) {
            viewModelScope.launch {
                val response=loginRepository.loginUser(loginRequest)
                if (response.isSuccessful){
                    logLiveData.postValue(response.body())
                }
                else{
                    errorLiveData.postValue(response.errorBody()?.string())
                }
            }
        }
    }