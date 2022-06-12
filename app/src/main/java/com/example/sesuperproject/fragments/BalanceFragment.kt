package com.example.sesuperproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sesuperproject.MainActivity
import com.example.sesuperproject.R
import com.example.sesuperproject.api.RetrofitInterface
import com.example.sesuperproject.models.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


class BalanceFragment : Fragment() {

    val TAG = "balance_fragment"

    lateinit var equippedTitle: UserTitle
    var lockedTitles = mutableListOf<Title>()

    lateinit var adapter: NextTitleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_balance, container, false)

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        botNav.visibility = View.VISIBLE

        val unlockCardView = view.findViewById<CardView>(R.id.cardViewProgress)

        val user = (activity as MainActivity).loggedInUser

        val retrofitInterface = RetrofitInterface.create()

        updateEquippedTitle(view, retrofitInterface, user)

        unlockCardView.setOnClickListener{
            if(lockedTitles[0] != null){
                if(user.user_balance >= lockedTitles[0].pointRequirement){

                    var param = HashMap<String, Int>()
                    param["user_id"] = user.user_id
                    param["title_id"] = lockedTitles[0].titleId

                    GlobalScope.launch(Dispatchers.IO) {
                        val unlockTitleCall = retrofitInterface.unlockTitle(param).awaitResponse()

                        if(unlockTitleCall.isSuccessful){
                            lockedTitles.removeFirst()
                            updateEquippedTitle(view, retrofitInterface, user)
                        }
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = (activity as MainActivity).loggedInUser

        val username = view.findViewById<TextView>(R.id.header_username)
        val userId = view.findViewById<TextView>(R.id.header_userTitle)
        val currBalanceTxt = view.findViewById<TextView>(R.id.balanceAmount)

        username.text = user.full_name
        userId.text = user.user_id.toString()
        currBalanceTxt.text = "Rp. " + user.user_balance.toString()
    }

    private fun updateEquippedTitle(view: View, retrofitInterface: RetrofitInterface, user: User){
        val titleView = view.findViewById<TextView>(R.id.header_userTitle)
        val nextTitleAmountView = view.findViewById<TextView>(R.id.nextTitleTarget)

        GlobalScope.launch(Dispatchers.IO){
            val equippedTitleCall = retrofitInterface.getEquippedTitle(user.user_id).awaitResponse()

            if(equippedTitleCall.isSuccessful){
                equippedTitle = equippedTitleCall.body()!!

                val equippedTitleDetailCall = retrofitInterface.getTitleDetails(equippedTitle.titleId).awaitResponse()
                if(equippedTitleDetailCall.isSuccessful){
                    val equippedTitleDetail = equippedTitleDetailCall.body()!!

                    val lockedTitleCall = retrofitInterface.getLockedTitles(equippedTitle.titleId).awaitResponse()
                    if(lockedTitleCall.isSuccessful){
                        lockedTitles = lockedTitleCall.body()!!

                        withContext(Dispatchers.Main){
                            val recyclerView = view.findViewById<RecyclerView>(R.id.nextTitleView)

                            var n = 4
                            if(lockedTitles.size < n)
                                n = lockedTitles.size

                            adapter = NextTitleListAdapter(lockedTitles.subList(0, n))
                            recyclerView.adapter = adapter
                            val nonScrollableLayoutManager = object : LinearLayoutManager(requireContext()){
                                override fun canScrollVertically(): Boolean {
                                    return false
                                }
                            }
                            recyclerView.layoutManager = nonScrollableLayoutManager

                            if(lockedTitles.isNotEmpty()){
                                nextTitleAmountView.text = "Rp. " + lockedTitles[0].pointRequirement
                                updateProgressBar(view, user)
                            }

                        }
                    }

                    withContext(Dispatchers.Main){
                        titleView.text = equippedTitleDetail.titleName
                    }

                }
            }
        }
    }

    private fun updateProgressBar(view: View, user:User){
        val progressBar = view.findViewById<ProgressBar>(R.id.rewardProgress)
        val percentView = view.findViewById<TextView>(R.id.progressViewPercent)

        var progress: Float = (user.user_balance.toFloat() / lockedTitles[0].pointRequirement) * 100
        progressBar.max = 100;

        percentView.text = String.format("%.1f", progress) + "% completed"
        progressBar.setProgress(progress.toInt())
    }


}