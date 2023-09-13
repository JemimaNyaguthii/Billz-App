package com.example.billsmanagement.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Bills",
    indices=[Index(value=["name"],unique=true)])
data class Bill(
    @PrimaryKey
    var billId:String =UUID.randomUUID().toString(),
    var name:String,
    var amount: Double,
    var frequency:String,
    var dueDate:String,
    var userId:String
)
