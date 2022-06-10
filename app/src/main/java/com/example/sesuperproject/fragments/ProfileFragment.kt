package com.example.sesuperproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.sesuperproject.MainActivity
import com.example.sesuperproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        botNav.visibility = View.VISIBLE

        val logoutDialogueFragment = LogoutDialogueFragment()

        val logoutBtn = view.findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener{
            logoutDialogueFragment.show(parentFragmentManager, "customDialog")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = (activity as MainActivity).loggedInUser

        val studentName = view.findViewById<TextView>(R.id.header_username)
        val studentId = view.findViewById<TextView>(R.id.header_userId)
        val studentEmail = view.findViewById<TextView>(R.id.cardStudentEmail)
        val studentStudentId = view.findViewById<TextView>(R.id.cardStudentId)
        val studentClass = view.findViewById<TextView>(R.id.cardStudentClass)
        val studentSeat = view.findViewById<TextView>(R.id.cardStudentSeat)

        studentName.text = user.full_name
        studentId.text = user.user_id.toString()
        studentEmail.text = studentEmail.text.toString() + " " + user.email
        studentStudentId.text = studentStudentId.text.toString() + " " + user.student_id
        studentClass.text = studentClass.text.toString() + " " + user.class_code
        studentSeat.text = studentSeat.text.toString() + " " + user.absent_number.toString()

    }
}