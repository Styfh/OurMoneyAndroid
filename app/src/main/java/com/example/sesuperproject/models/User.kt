package com.example.sesuperproject.models

import com.google.gson.annotations.SerializedName


data class User(
    var user_id: Int,
    var email: String,
    var full_name: String,
    var student_id: String,
    @SerializedName("class")
    var class_code: String,
    var absent_number: Int
    )