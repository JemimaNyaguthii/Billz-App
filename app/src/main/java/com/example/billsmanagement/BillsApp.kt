package com.example.billsmanagement

import android.app.Application
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.billsmanagement.utils.Constants
import com.example.billsmanagement.workmanager.UpcomingBillsWorker
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.MINUTES
import kotlin.time.DurationUnit

class BillsApp:Application(){
    companion object{
        lateinit var appContext: Context

    }

    override fun onCreate() {
        super.onCreate()
        appContext=applicationContext
        val periodicWorkRequest= PeriodicWorkRequestBuilder<UpcomingBillsWorker>(15,TimeUnit.MINUTES)
            .addTag(Constants.CREATE_RECURRING_BILLS).build()
        WorkManager
            .getInstance(appContext)
            .enqueueUniquePeriodicWork(Constants.CREATE_RECURRING_BILLS,ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest)
    }
}