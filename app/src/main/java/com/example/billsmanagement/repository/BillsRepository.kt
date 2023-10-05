package com.example.billsmanagement.repository

import android.content.Context
import android.media.session.MediaSession.Token
import androidx.lifecycle.LiveData
import com.example.billsmanagement.BillsApp
import com.example.billsmanagement.api.ApiClient
import com.example.billsmanagement.api.ApiInterface
import com.example.billsmanagement.database.BillDb
import com.example.billsmanagement.model.Bill
import com.example.billsmanagement.model.UpcomingBill
import com.example.billsmanagement.utils.Constants
import com.example.billsmanagement.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BillsRepository {
    val database = BillDb.getDatabase(BillsApp.appContext)
    val billsDao = database.billDao()
    val upcomingBillsDao = database.upcomingBillsDao()
    val apiClient= ApiClient.buildClient(ApiInterface::class.java)

    suspend fun saveBill(bill: Bill) {
//        switching to ui thread
        withContext(Dispatchers.IO) {
            database.billDao().insertBills(bill)
        }
    }

    suspend fun insertUpcomingBill(upcomingBill: UpcomingBill) {
        withContext(Dispatchers.IO) {
            upcomingBillsDao.insertUpcomingBill(upcomingBill)
        }
    }

    suspend fun createRecurringMonthlyBills() {
        withContext(Dispatchers.IO) {
            val monthlyBills = billsDao.getRecurringBills(Constants.MONTHLY)
            val startDate = DateTimeUtils.getFirstDayOfMonth()
            val endDate = DateTimeUtils.getLastDayOfMonth()
            monthlyBills.forEach { bill ->
                val existing = upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
                if (existing.isEmpty()) {
                    val newMonthlyBill = UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId = bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = DateTimeUtils.createDateFromDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false,
                        synced=false
                    )
                    upcomingBillsDao.insertUpcomingBill(newMonthlyBill)
                }
            }
        }
    }

    suspend fun createRecurringWeeklyBills() {
        withContext(Dispatchers.IO) {
            val weeklyBills = billsDao.getRecurringBills(Constants.WEEKLY)
            val startDate = DateTimeUtils.getFirstDateOfWeek()
            val endDate = DateTimeUtils.getLastDateOfWeek()
            val month = DateTimeUtils.getCurrentMonth()
            val year = DateTimeUtils.getCurrentYear()
            weeklyBills.forEach { bill ->
                val existingBill =
                    upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
                if (existingBill.isEmpty()) {
                    val newWeeklyBill = UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId = bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = DateTimeUtils.createDateFromDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false,
                                synced=false
                    )
                    upcomingBillsDao.insertUpcomingBill(newWeeklyBill)
                }
            }
        }

    }

    suspend fun createRecurringAnnualBills() {
        withContext(Dispatchers.IO) {
            val annualBills = billsDao.getRecurringBills(Constants.ANNUAL)
            val currentYear = DateTimeUtils.getCurrentYear()
            val startDate = "$currentYear-01-01"
            val endDate = "$currentYear-12-31"
            annualBills.forEach { bill ->
                val existing = upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
                if (existing.isEmpty()) {
                    val newAnnualBill = UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId = bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = "$currentYear-${bill.dueDate}",
                        userId = bill.userId,
                        paid = false,
                                synced=false
                    )
                    upcomingBillsDao.insertUpcomingBill(newAnnualBill)
                }
            }
        }

    }

    fun getUpcomingBillsByFrequency(freq: String): LiveData<List<UpcomingBill>> {
        return upcomingBillsDao.getUpcomingBillsByFrequency(freq, false)
    }
    suspend fun updateUpcomingBill(upcomingBill: UpcomingBill){
        withContext(Dispatchers.IO){
            upcomingBillsDao.updateUpcomingBill(upcomingBill)
        }
    }
    fun getPaidBills():LiveData<List<UpcomingBill>>{
        return upcomingBillsDao.getPaidBills()
    }

fun getAuthToken():String{
    val prefs=BillsApp.appContext.getSharedPreferences(Constants.PREFS,Context.MODE_PRIVATE)
    var token =prefs.getString(Constants.ACCESS_TOKEN,Constants.EMPTY_STRING)
    token="Bearer$token"
    return token
}
suspend fun syncBills(){
    withContext(Dispatchers.IO) {
        var token=getAuthToken()
        val unsyncedBills =billsDao.getUnsyncedBills()
        unsyncedBills.forEach { bill ->
            val response = apiClient.postBill(token,bill)
            if (response.isSuccessful) {
                bill.synced = true
                billsDao.insertBills(bill)
            }
        }
    }
    }
    suspend fun syncUpcomingBills(){
        withContext(Dispatchers.IO){
            var token=getAuthToken()
            upcomingBillsDao.getUnsyncedUpcomingBills().forEach {upcomingBill ->
                val response=apiClient.postUpcomingBill(token,upcomingBill)
                if (response.isSuccessful){
                    upcomingBill.synced=true
                    upcomingBillsDao.updateUpcomingBill(upcomingBill)
                }
            }
        }
    }
    suspend fun fetchRemoteBills(){
        withContext(Dispatchers.IO){
            val response=apiClient.fetchRemoteBills(getAuthToken())
            if (response.isSuccessful){
                response.body()?.forEach { bill ->
                    bill.synced=true
                    billsDao.insertBills(bill) }
            }
        }
    }
    suspend fun fetchRemoteUpcomingBills(){
        withContext(Dispatchers.IO){
            val response=apiClient.fetchRemoteUpcomingBills(getAuthToken())
            if (response.isSuccessful){
                response.body()?.forEach { upcomingBill ->
                    upcomingBill.synced=true
                    upcomingBillsDao.insertUpcomingBill(upcomingBill) }
            }
        }
    }
}
//crone job
//workmanager


//building summary fragment
//        a toogle to indicate the summary is for this wweek ,monthly or the year
//        paid and pending








