package com.example.billsmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.billsmanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        setContentView(binding.root)
        binding.btnSignUp.setOnClickListener {
            validateRegistration()
        }
    }
    fun validateRegistration() {
        val name = binding.etusername.text.toString()
        val phonenumber =binding.etPhoneNumber .text.toString()
        val email=binding.etEmail .text.toString()
        val password =binding.etPassword .text.toString()
        val confirm=binding.etConfirm.text.toString()
        var error = false
        if (name.isBlank()) {
            binding.tilUsername.error = "Name required"
            error=true
        }
        if (name.isBlank()) {
            binding.tilPhoneNumber.error = "Phonenumber required"
            error=true
        }
        if (name.isBlank()) {
            binding.tilEmail.error = "Email required"
            error=true
        }

        if (name.isBlank()) {
            binding.tilPassword.error = "Password required"
            error=true
        }
        if (confirm!=password){
            binding.tilConfirm.error="Try again"
            error=true
        }
        if (!error){
            val intent= Intent(this,Login::class.java)
            startActivity(intent)
        }
    }
}

