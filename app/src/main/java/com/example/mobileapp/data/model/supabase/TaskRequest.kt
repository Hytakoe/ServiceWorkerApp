package com.example.mobileapp.data.model.supabase

import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("id_car")
    val carId: Int,

    @SerializedName("work_result")
    val job: String,

    @SerializedName("comment")
    val comment: String? = null,

    @SerializedName("cost_of_work")
    val cost: Int = 0
)