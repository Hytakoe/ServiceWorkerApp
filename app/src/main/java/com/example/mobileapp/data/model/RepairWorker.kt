package com.example.mobileapp.data.model

import com.google.gson.annotations.SerializedName

data class RepairWorker(
    @SerializedName("id_worker")
    val workerId: Int,

    @SerializedName("id_repair")
    val repairId: Int
)