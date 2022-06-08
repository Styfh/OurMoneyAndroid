package com.example.sesuperproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sesuperproject.MainActivity
import com.example.sesuperproject.R
import com.example.sesuperproject.api.RetrofitInterface
import com.example.sesuperproject.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private val homeFragment = HomeFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val api = RetrofitInterface.create()

        val loginBtn = view.findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener{
            val emailInput = view.findViewById<EditText>(R.id.inputEmail)
            val passwordInput = view.findViewById<EditText>(R.id.inputPassword)

            val map = HashMap<String, String>()

            map["email"] = emailInput.text.toString()
            map["password"] = passwordInput.text.toString()

            val call: Call<User> = api.executeLogin(map)

            call.enqueue(object : Callback<User>{

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>) {

                    if(response.code() == 200){
                        Toast.makeText(requireActivity(), "Login successful", Toast.LENGTH_LONG).show()

                        (activity as MainActivity).loggedInUser = response.body()!!

                        parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, homeFragment)
                            .commit()
                    } else{
                        Toast.makeText(requireActivity(), "User does not exist", Toast.LENGTH_LONG).show()
                    }


                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }


            })

        }

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        botNav.visibility = View.GONE

        return view
    }

}