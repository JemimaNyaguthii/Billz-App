package com.example.billsmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billsmanagement.model.Bill
import com.example.billsmanagement.model.UpcomingBill
import com.example.billsmanagement.repository.BillsRepository
import kotlinx.coroutines.launch

class BillsViewModel:ViewModel(){
   lateinit var billsLiveData:LiveData<List<Bill>>
    val billRepo=BillsRepository()

    fun saveBill(bill: Bill){
        viewModelScope.launch {
            billRepo.saveBill(bill)
        }
    }
    fun getUpcomingBillsByFrequency(freq:String):LiveData<List<UpcomingBill>>{
        return billRepo.getUpcomingBillsByFrequency(freq)
    }
fun updateUpcomingBill(upcomingBill: UpcomingBill){
    viewModelScope.launch {
        billRepo.updateUpcomingBill(upcomingBill)
    }
}
    fun getPaidBills():LiveData<List<UpcomingBill>>{
        return billRepo.getPaidBills()
    }
//    triggering the downloads and the updates in the summary fragment fetching the remote bills
    fun fetchRemoteBills(){
        viewModelScope.launch {
            billRepo.fetchRemoteUpcomingBills()
            billRepo.fetchRemoteBills()
        }
    }
}


