package com.example.billsmanagement.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
var message:String,
@SerializedName("user_id")var user_id:String,
@SerializedName("access_token")var access_token:String

)