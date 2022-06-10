package com.example.sesuperproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.sesuperproject.MainActivity
import com.example.sesuperproject.R
import com.example.sesuperproject.api.RetrofitInterface
import com.example.sesuperproject.models.TransactionHeader
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import retrofit2.await
import retrofit2.awaitResponse

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        botNav.visibility = View.VISIBLE

        val api = RetrofitInterface.create()

        var incomeTotal = 0
        var outcomeTotal = 0
        var diff = 0f

        val userId = (activity as MainActivity).loggedInUser.user_id

        val incomeView = view.findViewById<TextView>(R.id.income)
        val outcomeView = view.findViewById<TextView>(R.id.outcome)
        val statusView = view.findViewById<TextView>(R.id.status)

        incomeView.text = incomeView.text.toString() + " Rp. 0"
        outcomeView.text = outcomeView.text.toString() + " Rp. 0"
        statusView.text = statusView.text.toString() + " 0"

        GlobalScope.launch(Dispatchers.IO){

            val incomeCall = api.getIncome(userId).awaitResponse()
            val incomeList = incomeCall.body()

            if(incomeCall.isSuccessful){
                if (incomeList != null) {
                    for(income in incomeList){
                        incomeTotal += income.amount
                        diff = (incomeTotal - outcomeTotal).toFloat()
                        val status = (diff / incomeTotal) * 100

                        withContext(Dispatchers.Main){
                            incomeView.text = "Income: Rp. " + incomeTotal
                            statusView.text = "Status: " + String.format("%.1f", status) + "%"
                        }
                    }
                } else{
                    incomeTotal = 0
                }
            }

            val outcomeCall = api.getOutcome(userId).awaitResponse()
            val outcomeList = outcomeCall.body()

            if(outcomeCall.isSuccessful){
                if(outcomeList != null){
                    for(outcome in outcomeList){

                        async{
                            val outcomeDetailCall = api.getTransactionDetail(outcome.transactionId).awaitResponse()
                            val outcomeDetail = outcomeDetailCall.body()

                            if(outcomeDetailCall.isSuccessful){

                                if (outcomeDetail != null) {
                                    for(detail in outcomeDetail){

                                        async{
                                            val itemDetailCall = api.getItemDetail(detail.itemId).awaitResponse()
                                            val itemDetail = itemDetailCall.body()

                                            if(itemDetailCall.isSuccessful){
                                                if(itemDetail != null){
                                                    outcomeTotal += (itemDetail.itemPrice * detail.quantity)
                                                    diff = (incomeTotal - outcomeTotal).toFloat()
                                                    val status = (diff / incomeTotal) * 100

                                                    withContext(Dispatchers.Main){
                                                        outcomeView.text = "Outcome: Rp. " + outcomeTotal
                                                        statusView.text = "Status: " + String.format("%.1f", status) + "%"
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }

                            }
                        }

                    }

                } else{
                    outcomeTotal = 0
                }
            }

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = (activity as MainActivity).loggedInUser

        val welcomeName = view.findViewById<TextView>(R.id.welcome_user)
        val welcomeId = view.findViewById<TextView>(R.id.welcome_user_id)

        welcomeName.text = "Welcome, " + user.full_name
        welcomeId.text = user.user_id.toString()
    }


}