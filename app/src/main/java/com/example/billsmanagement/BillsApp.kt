package com.example.billsmanagement

import android.app.Application
import android.content.Context

class BillsApp:Application(){
    companion object{
        lateinit var appContext: Context

    }

    override fun onCreate() {
        super.onCreate()
        appContext=applicationContext
    }
}