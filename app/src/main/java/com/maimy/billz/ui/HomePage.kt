package com.maimy.billz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.maimy.billz.R
import com.maimy.billz.databinding.ActivityHomePageBinding
import com.maimy.billz.viewmodel.BillsViewModel

class HomePage : AppCompatActivity() {
    lateinit var binding:ActivityHomePageBinding
    val billsViewModel:BillsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    override fun onResume() {
        super.onResume()
        setupBottomNav()
//        billsViewModel.createRecurringBills()
    }
    fun setupBottomNav(){
        binding.bnvHome.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.summary->{
                  supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.fcvHome, SummaryFragment()).commit()
                  true
                }
                R.id.upcoming->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fcvHome, UpcomingBillsFragment()).commit()
                    true


                }
                R.id.payment->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fcvHome, PaidBillsFragment()).commit()
                    true
                }
                R.id.settings->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fcvHome, SettingsFragment()).commit()
                    true
                }
                else->false
            }
        }
    }
}