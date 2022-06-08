package com.example.sesuperproject.models

import com.google.gson.annotations.SerializedName


data class TransactionHeader(
    @SerializedName("transaction_id")
    var transactionId: Int,
    @SerializedName("user_id")
    var transactionUserId: Int,
    @SerializedName("canteen_id")
    var transactionCanteenId: Int,
    @SerializedName("transaction_date")
    var transactionDate: String,
    @SerializedName("payed")
    var payed: Int
)