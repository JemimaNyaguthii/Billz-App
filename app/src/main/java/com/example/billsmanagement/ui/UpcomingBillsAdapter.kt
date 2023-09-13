package com.example.billsmanagement.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.billsmanagement.databinding.UpcomingBillsListItemBinding
import com.example.billsmanagement.model.UpcomingBill

class UpcomingBillsAdapter(var upcomingBill: List<UpcomingBill>):Adapter<UpcomingBillsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingBillsViewHolder {
    var binding= UpcomingBillsListItemBinding.inflate(LayoutInflater.from(parent.context))
        return UpcomingBillsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingBillsViewHolder, position: Int) {
      var upcomingBill=upcomingBill.get(position)
        holder.binding.apply {
            cbUpcoming.text=upcomingBill.name
            tvAmount.text=upcomingBill.amount.toString()
            tvDueDate.text=upcomingBill.dueDate
        }
    }

    override fun getItemCount(): Int {
    return upcomingBill.size
    }
}
class UpcomingBillsViewHolder(var binding: UpcomingBillsListItemBinding):ViewHolder(binding.root){

}