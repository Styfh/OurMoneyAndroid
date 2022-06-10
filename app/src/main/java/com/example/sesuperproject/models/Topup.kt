package com.example.sesuperproject.models

import com.google.gson.annotations.SerializedName

data class Topup (
    @SerializedName("topup_id")
    var topupId: Int,
    @SerializedName("parent_id")
    var parentId: Int,
    @SerializedName("user_id")
    var userId: Int,
    var amount: Int,
    @SerializedName("topup_date")
    var topupDate: String
        )