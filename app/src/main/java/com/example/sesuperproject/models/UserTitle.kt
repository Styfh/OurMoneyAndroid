package com.example.sesuperproject.models

import com.google.gson.annotations.SerializedName

data class UserTitle (
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("title_id")
    var titleId: Int
        )