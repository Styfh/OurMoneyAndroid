package com.example.sesuperproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sesuperproject.MainActivity
import com.example.sesuperproject.R
import com.example.sesuperproject.api.RetrofitInterface
import com.example.sesuperproject.models.Item
import com.example.sesuperproject.models.TransactionDetail
import kotlinx.coroutines.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class PayDetailFragment : Fragment() {

    val payFragment = PayFragment()
    lateinit var currDetail: List<TransactionDetail>
    var currList = mutableListOf<Item>()
    lateinit var adapter: ProductListAdapter
    var paymentTotal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pay_detail, container, false)

        val transactionId = (activity as MainActivity).currentTransactionHeader.transactionId
        val storeId = (activity as MainActivity).currentTransactionHeader.transactionCanteenId

        val retrofitInterface = RetrofitInterface.create()

        val currBalance = (activity as MainActivity).loggedInUser.user_balance
        val currBalanceTxt = view.findViewById<TextView>(R.id.currBalance)
        val headerPrice = view.findViewById<TextView>(R.id.total_price)

        headerPrice.text = "Rp. " + paymentTotal.toString()
        currBalanceTxt.text = currBalance.toString()

        val storeName = view.findViewById<TextView>(R.id.cardStoreName)

        GlobalScope.launch (Dispatchers.IO) {

            val storeResponse = retrofitInterface.getStoreDetail(storeId).awaitResponse()

            if(storeResponse.isSuccessful){
                withContext(Dispatchers.Main){
                    storeName.text = storeResponse.body()!!.canteenName
                }
            }

        }

        val payBtn = view.findViewById<Button>(R.id.payBtn)
        payBtn.setOnClickListener{

            if(paymentTotal in 1 until currBalance){

                GlobalScope.launch (Dispatchers.IO){

                    var map = HashMap<String, Int>()
                    map["trans_id"] = transactionId
                    map["new_balance"] = (currBalance - paymentTotal)
                    map["user_id"] = (activity as MainActivity).loggedInUser.user_id

                    Log.d("local_data", map.toString())

                    val paymentResponse = retrofitInterface.payTransaction(map).awaitResponse()

                    withContext(Dispatchers.Main){
                        if(paymentResponse.isSuccessful){
                            Toast.makeText(requireContext(), "Payment Successful", Toast.LENGTH_LONG).show()

                            (activity as MainActivity).loggedInUser.user_balance -= paymentTotal

                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, payFragment)
                                .commit()
                        } else{
                            Toast.makeText(requireContext(), paymentResponse.errorBody().toString(), Toast.LENGTH_LONG).show()
                        }

                    }

                }
            } else{
                Toast.makeText(requireContext(), "Not enough balance", Toast.LENGTH_LONG)
            }


        }

        GlobalScope.launch (Dispatchers.IO){
            val detailResponse = retrofitInterface.getTransactionDetail(transactionId).awaitResponse()

            if(detailResponse.isSuccessful){
                currDetail = detailResponse.body()!!

                withContext(Dispatchers.Main){
                    val recyclerView = view.findViewById<RecyclerView>(R.id.itemList)
                    adapter = ProductListAdapter(currList, currDetail)

                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }

                Log.d("local_data", currDetail.toString())

                for(detail in currDetail){

                    async{
                        val itemResponse = retrofitInterface.getItemDetail(detail.itemId).awaitResponse()
                        if(itemResponse.isSuccessful){
                            currList.add(itemResponse.body()!!)
                            paymentTotal += (itemResponse.body()!!.itemPrice * detail.quantity)

                            withContext(Dispatchers.Main){
                                adapter?.let{
                                    it.notifyDataSetChanged()
                                }
                                val lblTotalPrice = view.findViewById<TextView>(R.id.total_price)
                                val diff = view.findViewById<TextView>(R.id.diff)
                                val balanceAfterTxt = view.findViewById<TextView>(R.id.balanceAfter)

                                val balanceAfter = (activity as MainActivity).loggedInUser.user_balance - paymentTotal

                                lblTotalPrice.text = "Rp. " + paymentTotal.toString()
                                diff.text = "-" + paymentTotal.toString()
                                balanceAfterTxt.text = balanceAfter.toString()
                            }
                        }
                    }
                }
            }
        }

        return view
    }

//    suspend fun getAllDetails(transactionId: Int): Response<List<TransactionDetail>>{
//        when(val result = )
//    }

}