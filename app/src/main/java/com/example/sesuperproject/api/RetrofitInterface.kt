package com.example.sesuperproject.api

import com.example.sesuperproject.models.*
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

    @GET("/canteens/{id}")
    fun getStoreDetail(@Path("id") canteenId: Int): Call<Store>

    @GET("/td/{id}")
    fun getTransactionDetail(@Path("id") transactionId: Int): Call<List<TransactionDetail>>

    @GET("/it/{id}")
    fun getItemDetail(@Path("id") itemId: Int): Call<Item>

    @POST("/pay")
    fun payTransaction(@Body map: HashMap<String, Int>): Call<Void>

    @GET("/income/{id}")
    fun getIncome(@Path("id") userId: Int): Call<List<Topup>>

    @GET("/outcome/{id}")
    fun getOutcome(@Path("id") userId: Int): Call<List<TransactionHeader>>

    @GET("/title/equipped/{id}")
    fun getEquippedTitle(@Path("id") userId: Int): Call<UserTitle>

    @GET("/title/locked/{id}")
    fun getLockedTitles(@Path("id") titleId: Int): Call<List<Title>>

    @GET("/title/{id}")
    fun getTitleDetails(@Path("id") titleId: Int): Call<Title>

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