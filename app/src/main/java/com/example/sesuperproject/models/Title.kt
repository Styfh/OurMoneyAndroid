package com.example.sesuperproject.models

import com.google.gson.annotations.SerializedName

data class Title (
    @SerializedName("title_id")
    var titleId: Int,
    @SerializedName("title_name")
    var titleName: String,
    @SerializedName("points_requirement")
    var pointRequirement: Int
        )