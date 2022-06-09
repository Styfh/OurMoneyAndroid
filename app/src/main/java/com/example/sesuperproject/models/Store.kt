package com.example.sesuperproject.models

import com.google.gson.annotations.SerializedName

data class Store (
    @SerializedName("canteen_id")
    var canteenId: Int,
    @SerializedName("canteen_name")
    var canteenName: String
    )
