//
//package com.example.billsmanagement.ui
//import AddBillActivity
//import BillsRvAdapter
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.billsmanagement.databinding.FragmentSummaryBinding
//import com.example.billsmanagement.model.Bill
//import com.example.billsmanagement.viewmodel.BillsViewModel
//
//class SummaryFragment : Fragment() {
//    var billId=0
//    private var _binding: FragmentSummaryBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var viewModel: BillsViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel = ViewModelProvider(requireActivity()).get(BillsViewModel::class.java)
//
//        binding.fabAddBill.setOnClickListener {
//            startActivity(Intent(requireContext(), AddBillActivity::class.java))
//        }
//
//        viewModel.billsLiveData.observe(viewLifecycleOwner) { bills ->
//            if (bills.isNotEmpty()) {
//                displayBills(billData = bills) // Display the first bill (you might want to adjust this)
//            } else {
//                Toast.makeText(requireContext(), "No bills available", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }
//
//private fun displayBills(billData: List<Bill>){
//    val adapter= BillsRvAdapter(billData)
//    binding.rvBills.adapter=adapter
//}
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
package com.example.billsmanagement.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.billsmanagement.R
import com.example.billsmanagement.databinding.FragmentSummaryBinding

class SummaryFragment : Fragment() {
    var binding: FragmentSummaryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return  binding?.root
    }

    override fun onResume() {
        super.onResume()
        binding?.fabAddBill?.setOnClickListener {
            startActivity(Intent(requireContext(), AddBillActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
