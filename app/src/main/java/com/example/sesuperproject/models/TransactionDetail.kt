package com.example.sesuperproject.models

import com.google.gson.annotations.SerializedName

data class TransactionDetail (
    @SerializedName("transaction_id")
    var transactionId: Int,
    @SerializedName("item_id")
    var itemId: Int,
    @SerializedName("quantity")
    var quantity: Int
)