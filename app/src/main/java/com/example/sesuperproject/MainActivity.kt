package com.example.sesuperproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sesuperproject.fragments.BalanceFragment
import com.example.sesuperproject.fragments.HomeFragment
import com.example.sesuperproject.fragments.PayFragment
import com.example.sesuperproject.fragments.ProfileFragment
import com.example.sesuperproject.models.TransactionHeader
import com.example.sesuperproject.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var loggedInUser: User
    lateinit var currentTransactionHeader: TransactionHeader

    private val homeFragment = HomeFragment()
    private val balanceFragment = BalanceFragment()
    private val payFragment = PayFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botNav = findViewById<BottomNavigationView>(R.id.navigation)

        botNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.balance -> replaceFragment(balanceFragment)
                R.id.pay -> replaceFragment(payFragment)
                R.id.profile -> replaceFragment(profileFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.commit()
        }
    }

}