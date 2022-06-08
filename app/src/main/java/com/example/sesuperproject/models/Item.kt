package com.example.sesuperproject.models

import com.google.gson.annotations.SerializedName

data class Item (
    @SerializedName("item_id")
    var itemId: Int,
    @SerializedName("canteen_id")
    var canteenId: Int,
    @SerializedName("item_name")
    var itemName: String,
    @SerializedName("item_price")
    var itemPrice: Int
)