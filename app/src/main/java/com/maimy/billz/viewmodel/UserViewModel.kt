package com.maimy.billz.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maimy.billz.model.RegisterRequest
import com.maimy.billz.model.RegisterResponse
import com.maimy.billz.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel:ViewModel() {
    val userRepository = UserRepository()
    val regLiveData = MutableLiveData<RegisterResponse>()
    val errorLiveData = MutableLiveData<String>()

    fun registerUser(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            val response=userRepository.registerUser(registerRequest)
            if (response.isSuccessful){
                regLiveData.postValue(response.body())
            }
            else{
                errorLiveData.postValue(response.errorBody()?.string())
            }
        }
    }

            }
//live data holds data
//launch is responsible for launching/creating the coroutine
//viewmodel by default has its own view model scope we launch it because to cancel.
