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
import com.example.sesuperproject.models.TransactionHeader
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_pay, container, false)

        val user = (activity as MainActivity).loggedInUser

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        botNav.visibility = View.VISIBLE

        val payBtn = view.findViewById<Button>(R.id.payBtn)
        payBtn.setOnClickListener{
            val inputTransactionId = view.findViewById<EditText>(R.id.inputTransactionId)
            val transactionId = Integer.parseInt(inputTransactionId.text.toString())

            val retrofitInterface = RetrofitInterface.create()
            val callGetHeader: Call<TransactionHeader> = retrofitInterface.getTransactionHeader(transactionId)

            callGetHeader.enqueue(object: Callback<TransactionHeader> {
                override fun onResponse(
                    call: Call<TransactionHeader>,
                    response: Response<TransactionHeader>
                ) {

                    if(response.code() == 204){
                        Toast.makeText(requireContext(), "Transaction does not exist", Toast.LENGTH_LONG).show()
                    } else if(response.body()!!.payed == 1){
                        Toast.makeText(requireContext(), "Transaction was already paid", Toast.LENGTH_LONG).show()
                    } else if(response.body()!!.transactionUserId != user.user_id){
                        Toast.makeText(requireContext(), "This transaction belongs to another user", Toast.LENGTH_LONG).show()
                    } else if(response.code() == 200){
                        (activity as MainActivity).currentTransactionHeader = response.body()!!

                        val payDetailFragment = PayDetailFragment()

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, payDetailFragment)
                            .commit()

                    }

                }

                override fun onFailure(call: Call<TransactionHeader>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }

            })

        }

        return view
    }

}