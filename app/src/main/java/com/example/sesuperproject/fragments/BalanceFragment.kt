package com.example.sesuperproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sesuperproject.MainActivity
import com.example.sesuperproject.R
import com.example.sesuperproject.api.RetrofitInterface
import com.example.sesuperproject.models.Title
import com.example.sesuperproject.models.UserTitle
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


class BalanceFragment : Fragment() {

    lateinit var equippedTitle: UserTitle
    var lockedTitles = emptyList<Title>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_balance, container, false)

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        botNav.visibility = View.VISIBLE

        val titleView = view.findViewById<TextView>(R.id.header_userTitle)
        val nextTitleAmountView = view.findViewById<TextView>(R.id.nextTitleTarget)

        val userId = (activity as MainActivity).loggedInUser.user_id

        val retrofitInterface = RetrofitInterface.create()

        GlobalScope.launch(Dispatchers.IO){

            val equippedTitleCall = retrofitInterface.getEquippedTitle(userId).awaitResponse()

            if(equippedTitleCall.isSuccessful){
                equippedTitle = equippedTitleCall.body()!!

                val equippedTitleDetailCall = retrofitInterface.getTitleDetails(equippedTitle.titleId).awaitResponse()
                if(equippedTitleDetailCall.isSuccessful){
                    val equippedTitleDetail = equippedTitleDetailCall.body()!!

                    val lockedTitleCall = retrofitInterface.getLockedTitles(equippedTitle.titleId).awaitResponse()
                    if(lockedTitleCall.isSuccessful){
                        lockedTitles = lockedTitleCall.body()!!

                        withContext(Dispatchers.Main){
                            nextTitleAmountView.text = "Rp. " + lockedTitles[0].pointRequirement
                        }
                    }

                    withContext(Dispatchers.Main){
                        titleView.text = equippedTitleDetail.titleName
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


}