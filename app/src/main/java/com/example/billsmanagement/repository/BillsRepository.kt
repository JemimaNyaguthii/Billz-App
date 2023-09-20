package com.example.billsmanagement.repository

import android.hardware.ConsumerIrManager.CarrierFrequencyRange
import androidx.lifecycle.LiveData
import com.example.billsmanagement.BillsApp
import com.example.billsmanagement.database.BillDb
import com.example.billsmanagement.database.UpcomingBillsDao
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
//    val  billDao=database.billDao()

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
                        paid = false
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
                        paid = false
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
                        paid = false
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
}


//crone job
//workmanager scheduling a task








