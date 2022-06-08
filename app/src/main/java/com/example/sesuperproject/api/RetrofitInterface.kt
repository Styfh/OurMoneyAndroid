package com.example.sesuperproject.api

import com.example.sesuperproject.models.Item
import com.example.sesuperproject.models.TransactionDetail
import com.example.sesuperproject.models.TransactionHeader
import com.example.sesuperproject.models.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitInterface {

    @POST("/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<User>

    @GET("/th/{id}")
    fun getTransactionHeader(@Path("id") transactionId: Int): Call<TransactionHeader>

    @GET("/td/{id}")
    fun getTransactionDetail(@Path("id") transactionId: Int): Call<List<TransactionDetail>>

    @GET("/it/{id}")
    fun getItemDetail(@Path("id") itemId: Int): Call<Item>

    companion object{
        private const val BASE_URL = "http://10.0.2.2:8000"

        fun create() : RetrofitInterface {

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RetrofitInterface::class.java)

        }
    }

}