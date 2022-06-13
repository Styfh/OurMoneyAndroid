package com.example.sesuperproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sesuperproject.MainActivity
import com.example.sesuperproject.R
import com.example.sesuperproject.api.RetrofitInterface
import com.example.sesuperproject.models.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import retrofit2.await
import retrofit2.awaitResponse

class HomeFragment : Fragment() {

    lateinit var equippedTitle: UserTitle
    var lockedTitles = mutableListOf<Title>()

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

        val user = (activity as MainActivity).loggedInUser
        val userId = (activity as MainActivity).loggedInUser.user_id

        val rewardView = view.findViewById<CardView>(R.id.cardViewProgress)

        updateData(view, incomeTotal, outcomeTotal, 0f)

        GlobalScope.launch(Dispatchers.IO){

            val incomeCall = api.getIncome(userId).awaitResponse()
            val incomeList = incomeCall.body()

            if(incomeCall.isSuccessful){
                if (incomeList != null) {
                    for(income in incomeList){
                        incomeTotal += income.amount

                        withContext(Dispatchers.Main){
                            updateData(view, incomeTotal, outcomeTotal, calcStatus(incomeTotal, outcomeTotal))
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

                                                    withContext(Dispatchers.Main){
                                                        updateData(view, incomeTotal, outcomeTotal, calcStatus(incomeTotal, outcomeTotal))
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

        getNextTitle(view, api, user)

        rewardView.setOnClickListener{
            if(lockedTitles.isNotEmpty()){
                if(user.user_balance >= lockedTitles[0].pointRequirement){
                    parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, BalanceFragment())
                        .commit()
                }
            } else{
                Toast.makeText(requireContext(), "You've unlocked all the titles", Toast.LENGTH_LONG).show()
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

    private fun getNextTitle(view: View, retrofitInterface: RetrofitInterface, user: User){
        GlobalScope.launch(Dispatchers.IO){
            val equippedTitleCall = retrofitInterface.getEquippedTitle(user.user_id).awaitResponse()

            if(equippedTitleCall.isSuccessful){
                equippedTitle = equippedTitleCall.body()!!

                val lockedTitleCall = retrofitInterface.getLockedTitles(equippedTitle.titleId).awaitResponse()
                if(lockedTitleCall.isSuccessful){
                    lockedTitles = lockedTitleCall.body()!!

                    withContext(Dispatchers.Main){
                        if(lockedTitles.isNotEmpty()){
                            updateProgressBar(view, user)
                        }

                    }
                }

            }
        }
    }

    private fun updateProgressBar(view: View, user: User){
        val progressBar = view.findViewById<ProgressBar>(R.id.rewardProgress)
        val percentView = view.findViewById<TextView>(R.id.progressViewPercent)

        var progress: Float = (user.user_balance.toFloat() / lockedTitles[0].pointRequirement) * 100
        progressBar.max = 100;

        percentView.text = String.format("%.1f", progress) + "% completed"
        progressBar.setProgress(progress.toInt())
    }

    private fun updateData(view: View, income: Int, outcome: Int, status: Float){
        val incomeView = view.findViewById<TextView>(R.id.income)
        val outcomeView = view.findViewById<TextView>(R.id.outcome)
        val statusView = view.findViewById<TextView>(R.id.status)

        incomeView.text = "Income: Rp. " + income
        outcomeView.text = "Outcome: Rp. " + outcome
        statusView.text = "Status: " + String.format("%.1f", status) + "%"
    }

    private fun calcStatus(income: Int, outcome: Int): Float {
        val diff = (income - outcome).toFloat()
        return (diff / income) * 100
    }


}