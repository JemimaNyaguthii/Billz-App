package com.example.billsmanagement.database
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.billsmanagement.model.UpcomingBill

@Dao
interface UpcomingBillsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUpcomingBill(upcomingBill: UpcomingBill)
    @Query("SELECT * FROM UpcomingBills WHERE billId=:billId AND dueDate BETWEEN :startDate AND :endDate")
    fun queryExistingBill(billId:String,startDate: String,endDate: String):List<UpcomingBill>
    @Query("SELECT * FROM UpcomingBills WHERE frequency=:freq AND paid=:paid ORDER BY dueDate ")
    fun getUpcomingBillsByFrequency(freq:String,paid:Boolean):LiveData<List<UpcomingBill>>

    @Update (onConflict = OnConflictStrategy.REPLACE)
    fun updateUpcomingBill(upcomingBill: UpcomingBill)

    @Query ("SELECT * FROM UpcomingBills WHERE paid=:paid ORDER BY dueDate")
    fun getPaidBills(paid:Boolean=true):LiveData<List<UpcomingBill>>

}
