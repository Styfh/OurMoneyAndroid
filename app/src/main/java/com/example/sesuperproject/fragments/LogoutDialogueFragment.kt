package com.example.sesuperproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.sesuperproject.R

class LogoutDialogueFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logout_dialogue, container,false)

        val yesBtn = view.findViewById<Button>(R.id.yesBtn)
        val noBtn = view.findViewById<Button>(R.id.noBtn)

        yesBtn.setOnClickListener{
            dismiss()
            val loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, loginFragment)
                .commit()
        }

        noBtn.setOnClickListener{
            dismiss()
        }

        return view
    }

}