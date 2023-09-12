package com.example.billsmanagement.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.billsmanagement.model.UpcomingBill

@Dao
interface UpcomingBillsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUpcomingBill(upcomingBill: UpcomingBill)
    @Query("SELECT * FROM UpcomingBills WHERE billId=:billId AND dueDate BETWEEN :startDate AND :")
    fun queryExistingBill(billId:String,startDate: String,endDate: String):List<UpcomingBill>
}
