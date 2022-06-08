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

//        val callGetDetail = retrofitInterface.getTransactionDetail(transactionId)
//
//        callGetDetail.enqueue(object: Callback<List<TransactionDetail>> {
//            override fun onResponse(
//                call: Call<List<TransactionDetail>>,
//                responses: Response<List<TransactionDetail>>
//            ) {
//
//                if(responses.code() == 200){
//                    Log.d("response", responses.body().toString())
//
//                    currDetail = responses.body()!!
//
//                    for(item in currDetail){
//
//                        var callGetItem = retrofitInterface.getItemDetail(item.itemId)
//
//                        callGetItem.enqueue(object: Callback<Item>{
//
//                            override fun onResponse(call: Call<Item>, response: Response<Item>) {
//                                Log.d("response", response.body().toString())
//
//                                currList.add(response.body()!!)
//                                paymentTotal += response.body()!!.itemPrice
//
//                                adapter.notifyDataSetChanged()
//                            }
//
//                            override fun onFailure(call: Call<Item>, t: Throwable) {
//                                Toast.makeText(requireActivity() ,t.message, Toast.LENGTH_LONG)
//                            }
//
//                        })
//
//                    }
//
//                    Log.d("local_data", responses.body()!!.toMutableList().toString())
//
//                } else if(responses.code() == 404){
//                    Toast.makeText(requireActivity(), "Detail not found. Returning to pay page.", Toast.LENGTH_LONG)
//                    parentFragmentManager.beginTransaction()
//                        .replace(R.id.fragmentContainerView, payFragment)
//                        .commit()
//                }
//
//            }
//
//            override fun onFailure(call: Call<List<TransactionDetail>>, t: Throwable) {
//                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG)
//            }
//
//        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pay_detail, container, false)

        val transactionId = (activity as MainActivity).currentTransactionHeader.transactionId

        val retrofitInterface = RetrofitInterface.create()

        val headerPrice = view.findViewById<TextView>(R.id.total_price)
        headerPrice.text = "Rp. " + paymentTotal.toString()

        val storeName = view.findViewById<TextView>(R.id.cardStoreName)

        val payBtn = view.findViewById<Button>(R.id.payBtn)
        payBtn.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, payFragment)
                .commit()
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
                                lblTotalPrice.text = "Rp. " + paymentTotal.toString()
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