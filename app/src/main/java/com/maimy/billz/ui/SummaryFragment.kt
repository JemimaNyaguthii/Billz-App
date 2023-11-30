package com.maimy.billz.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.graphics.vector.SolidFill
import com.maimy.billz.databinding.FragmentSummaryBinding
import com.maimy.billz.model.BillsSummary
import com.maimy.billz.utils.Utilis
import com.maimy.billz.viewmodel.BillsViewModel

class SummaryFragment : Fragment(){
    var binding: FragmentSummaryBinding? = null
    private lateinit var billsViewModel:BillsViewModel
    lateinit var adapter: BillSaveAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        billsViewModel = ViewModelProvider(this).get(BillsViewModel::class.java)
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return  binding?.root
    }
    
@RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        binding?.fabAddBill?.setOnClickListener {
            startActivity(Intent(requireContext(), AddBillActivity::class.java))
        }
        billsViewModel.getMonthlySummary()
        showMonthlySummary()
//    billsViewModel.getWeeklySummary()
//    showWeeklySummary()


    }
    fun showMonthlySummary(){
        billsViewModel.summaryLiveData.observe(this){summary->
        binding?.tvPaidAmt?.text=Utilis.formatCurrency(summary.paid)
        binding?.tvTotalAmt?.text=Utilis.formatCurrency(summary.total)
        binding?.tvOverdueAmt?.text=Utilis.formatCurrency(summary.overdue)
        binding?.tvUpcomingAmt?.text=Utilis.formatCurrency(summary.upcoming)
        showChart(summary)
        }
    }
//    fun showWeeklySummary(){
//        billsViewModel.summaryLiveData.observe(this){summary->
//            binding?.tvPaidAmt?.text=Utilis.formatCurrency(summary.paid)
//            binding?.tvTotalAmt?.text=Utilis.formatCurrency(summary.total)
//            binding?.tvOverdueAmt?.text=Utilis.formatCurrency(summary.overdue)
//            binding?.tvUpcomingAmt?.text=Utilis.formatCurrency(summary.upcoming)
//            showChart(summary)
//        }
    fun showChart(summary: BillsSummary){
        val entries= mutableListOf<DataEntry>()
        entries.add(ValueDataEntry("paid",summary.paid))
        entries.add(ValueDataEntry("upcoming",summary.upcoming))
        entries.add(ValueDataEntry("overdue",summary.overdue))
        val pieChart=AnyChart.pie()
        pieChart.data(entries)
        pieChart.innerRadius(80)
        pieChart.palette().itemAt(0, SolidFill("#E86136",100))
        pieChart.palette().itemAt(1, SolidFill("#B8AF6C",100))
        pieChart.palette().itemAt(2, SolidFill("#94c2c1",100))
        binding?.summaryChart?.setChart(pieChart)
    }
}


