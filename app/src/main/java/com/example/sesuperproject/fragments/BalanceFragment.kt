package com.example.sesuperproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sesuperproject.MainActivity
import com.example.sesuperproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class BalanceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_balance, container, false)

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        botNav.visibility = View.VISIBLE

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = (activity as MainActivity).loggedInUser

        val username = view.findViewById<TextView>(R.id.header_username)
        val userId = view.findViewById<TextView>(R.id.header_userId)

        username.text = user.full_name
        userId.text = user.user_id.toString()
    }


}