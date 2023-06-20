package com.example.billsmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.billsmanagement.databinding.ActivityLoginBinding
import com.example.billsmanagement.databinding.ActivityMainBinding

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
   override fun onResume() {
       super.onResume()
       setContentView(binding.root)
       binding.btnLogin.setOnClickListener {
      validateLogin()
       }
   }
       fun validateLogin() {
           val email = binding.etEmail.text.toString()
           val password = binding.etPassword.text.toString()
           var error = false
           if (email.isBlank()) {
               binding.tilEmail.error = "Email required"
               error = true
           }
           if (password.isBlank()) {
               binding.tilPassword.error = "Password required"
               error = true
           }


       }
   }
