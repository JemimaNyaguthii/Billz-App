package com.example.billsmanagement.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName="UpcomingBills",
indices=[Index(value=["billId","dueDate"],unique=true)])
data class UpcomingBill(
    @PrimaryKey var upcomingBillId: String,
    @Expose var billId:String,
    @Expose var name:String,
    @Expose var amount: Double,
    @Expose  var frequency:String,
    @Expose var dueDate:String,
    @Expose var userId:String,
    @Expose var paid:Boolean,
    var synced:Boolean
)
