package com.example.billsmanagement.repository

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
    suspend fun createRecurringMonthlyBills(){
        withContext(Dispatchers.IO){
           val monthlyBills=billsDao.getRecurringBills(Constants.MONTHLY)
            val startDate=DateTimeUtils.getFirstDayOfMonth()
            val endDate= DateTimeUtils.getLastDayOfMonth()
            val year= DateTimeUtils.getCurrentYear()
            val month= DateTimeUtils.getCurrentMonth()
            monthlyBills.forEach { bill ->
                val existing=upcomingBillsDao.queryExistingBill(bill.billId,startDate, endDate)
                if (existing.isEmpty()){
              val newMonthlyBill=UpcomingBill(
                upcomingBillId = UUID.randomUUID().toString(),
                billId=bill.billId,
                name = bill.name,
                amount = bill.amount,
                frequency =bill.frequency,
                dueDate = "${bill.dueDate}/$month/$year",
                userId = bill.userId,
                paid = false
            ) } } } }
    suspend fun createRecurringWeeklyBills(){
        withContext(Dispatchers.IO){
            val weeklyBills=billsDao.getRecurringBills(Constants.WEEKLY)
            val startDate=DateTimeUtils.getFirstDateOfWeek()
            val endDate=DateTimeUtils.getLastDateOfWeek()
            val month=DateTimeUtils.getCurrentMonth()
            val year=DateTimeUtils.getCurrentYear()
           weeklyBills.forEach { bill->
               val existingBill=upcomingBillsDao.queryExistingBill(bill.billId,startDate,endDate)
               if (existingBill.isEmpty()){
                   val newWeeklyBill=UpcomingBill(
                       upcomingBillId = UUID.randomUUID().toString(),
                       billId=bill.billId,
                       name = bill.name,
                       amount = bill.amount,
                       frequency =bill.frequency,
                       dueDate = "${bill.dueDate}/$month/$year",
                       userId = bill.userId,
                       paid = false
                   )
                   upcomingBillsDao.insertUpcomingBill(newWeeklyBill)
               }



            }

        }
    }
}


//crone job
//workmanager scheduling a task








