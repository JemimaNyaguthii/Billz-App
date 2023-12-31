package com.maimy.billz.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maimy.billz.BillsApp
import com.maimy.billz.api.ApiClient
import com.maimy.billz.api.ApiInterface
import com.maimy.billz.database.BillDb
import com.maimy.billz.model.Bill
import com.maimy.billz.model.BillsSummary
import com.maimy.billz.model.UpcomingBill
import com.maimy.billz.utils.Constants
import com.maimy.billz.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BillsRepository {
    private val database = BillDb.getDatabase(BillsApp.appContext)
   private val billsDao = database.billDao()
   private val upcomingBillsDao = database.upcomingBillsDao()
    val apiClient = ApiClient.buildClient(ApiInterface::class.java)

    suspend fun saveBill(bill: Bill) {
//        switching to ui thread
        withContext(Dispatchers.IO) {
          billsDao.saveBill(bill)
        }
    }
    fun getAllBills():LiveData<List<Bill>>{
        return billsDao.getAllBills()
    }

    suspend fun insertUpcomingBill(upcomingBill: UpcomingBill) {
        withContext(Dispatchers.IO) {
            upcomingBillsDao.insertUpcomingBill(upcomingBill)
        }
    }
//@RequiresApi(Build.VERSION_CODES.O)
    suspend fun createRecurringMonthlyBills() {
        withContext(Dispatchers.IO) {
            val monthlyBills = billsDao.getRecurringBills(Constants.MONTHLY)
            val startDate = DateTimeUtils.getFirstDayOfMonth()
            val endDate = DateTimeUtils.getLastDayOfMonth()
            monthlyBills.forEach { bill ->
                val existing = upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
                if (existing.isEmpty()) {
                    val newUpcomingBill = UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId = bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = DateTimeUtils.createDateFromDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false,
                        synced = false
                    )
                    upcomingBillsDao.insertUpcomingBill(newUpcomingBill)
                }
            }
        }
    }
    suspend fun createRecurringWeeklyBills() {
        withContext(Dispatchers.IO) {
            val weeklyBills = billsDao.getRecurringBills(Constants.WEEKLY)
            val startDate = DateTimeUtils.getFirstDateOfWeek()
            val endDate = DateTimeUtils.getLastDateOfWeek()
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
                        synced = false
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
                        synced = false
                    )
                    upcomingBillsDao.insertUpcomingBill(newAnnualBill)
                }
            }
        }

    }

    fun getUpcomingBillsByFrequency(freq: String): LiveData<List<UpcomingBill>> {
        return upcomingBillsDao.getUpcomingBillsByFrequency(freq, false)
    }

    suspend fun updateUpcomingBill(upcomingBill: UpcomingBill) {
        withContext(Dispatchers.IO) {
            upcomingBillsDao.updateUpcomingBill(upcomingBill)
        }
    }

    fun getPaidBills(): LiveData<List<UpcomingBill>> {
        return upcomingBillsDao.getPaidBills()
    }

    fun getAccessToken(): String {
        val prefs = BillsApp.appContext
            .getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        var token = prefs.getString(Constants.ACCESS_TOKEN,"")?:""
        token = "Bearer $token"
        return token
    }

    suspend fun syncBills() {
        var token = getAccessToken()
        withContext(Dispatchers.IO) {
            val unsyncedBills = billsDao.getUnsyncedBills()
            unsyncedBills.forEach { bill ->
                val response = apiClient.postBill(token, bill)
                if (response.isSuccessful) {
                    bill.synced = true
                    billsDao.saveBill(bill)
                }
            }
        }
    }

    suspend fun syncUpcomingBills() {
        withContext(Dispatchers.IO) {
            var token = getAccessToken()
            upcomingBillsDao.getUnsyncedUpcomingBills().forEach { upcomingBill ->
                val response = apiClient.postUpcomingBill(token, upcomingBill)
                if (response.isSuccessful) {
                    upcomingBill.synced = true
                    upcomingBillsDao.updateUpcomingBill(upcomingBill)
                }
            }
        }
    }

    suspend fun fetchRemoteBills() {
        withContext(Dispatchers.IO) {
            val token=getAccessToken()
            val response = apiClient.fetchRemoteBills(token)
            if (response.isSuccessful) {
                response.body()?.forEach { bill ->
                    bill.synced = true
                    billsDao.saveBill(bill)
                }
            }
        }
    }

    suspend fun fetchRemoteUpcomingBills() {
        withContext(Dispatchers.IO) {
            val token=getAccessToken()
            val response = apiClient.fetchRemoteUpcomingBills(token)
            if (response.isSuccessful) {
                response.body()?.forEach { upcomingBill ->
                    upcomingBill.synced = true
                    upcomingBillsDao.insertUpcomingBill(upcomingBill)
                }
            }
        }
    }

    suspend fun getMonthlySummary(): MutableLiveData<BillsSummary> {
     return withContext(Dispatchers.IO) {
            val startDate = DateTimeUtils.getFirstDayOfMonth()
            val endDate = DateTimeUtils.getLastDayOfMonth()
            val today = DateTimeUtils.getDateToday()
            val paid = upcomingBillsDao.getPaidMonthlyBillsSum(startDate, endDate)
            val upcoming = upcomingBillsDao.getUpcomingBillsThisMonth(startDate, endDate, today)
            val total = upcomingBillsDao.getTotalMonthlyBills(startDate, endDate)
            val overdue = upcomingBillsDao.getOverdueBillsThisMonth(startDate, endDate, today)
            val summary =
                BillsSummary(paid = paid, overdue = overdue, upcoming = upcoming, total = total)
            MutableLiveData(summary)
        }
    }
//    suspend fun getWeeklySummary(): MutableLiveData<BillsSummary> {
//        return withContext(Dispatchers.IO) {
//            val startDate = DateTimeUtils.getFirstDateOfWeek()
//            val endDate = DateTimeUtils.getLastDayOfMonth()
//            val today = DateTimeUtils.getDateToday()
//            val paid = upcomingBillsDao.getPaidMonthlyBillsSum(startDate, endDate)
//            val upcoming = upcomingBillsDao.getUpcomingBillsThisWeek(startDate, endDate, today)
//            val total = upcomingBillsDao.getTotalWeeklyBills(startDate, endDate)
//            val overdue = upcomingBillsDao.getOverdueBillsThisWeek(startDate, endDate,today)
//            val summary =
//                BillsSummary(paid = paid, overdue = overdue, upcoming = upcoming, total = total)
//            MutableLiveData(summary)
//        }
//    }
//    suspend fun getYearlySummary(): MutableLiveData<BillsSummary> {
//        return withContext(Dispatchers.IO) {
//            val startDate = DateTimeUtils.getFirstDayOfYear()
//            val endDate = DateTimeUtils.getLastDayOfYear()
//            val today = DateTimeUtils.getDateToday()
//            val paid = upcomingBillsDao.getPaidYearlyBillsSum(startDate, endDate)
//            val upcoming = upcomingBillsDao.getUpcomingBillsThisYear(startDate, endDate, today)
//            val total = upcomingBillsDao.getTotalYearlyBills(startDate, endDate)
//            val overdue = upcomingBillsDao.getOverdueBillsThisYear(startDate, endDate, today)
//            val summary =
//                BillsSummary(paid = paid, overdue = overdue, upcoming = upcoming, total = total)
//            MutableLiveData(summary)
//        }
//    }

}
//crone job
//workmanager
//building summary fragment
//a toogle to indicate the summary is for this wweek ,monthly or the year
//paid and pending








