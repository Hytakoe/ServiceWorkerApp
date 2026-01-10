// data/model/RepairWithTask.kt
package com.example.mobileapp.data.model

import com.example.mobileapp.data.model.supabase.SupabaseTask
import com.google.gson.annotations.SerializedName

data class RepairWithTask(
    @SerializedName("id_worker")
    val workerId: Int,

    @SerializedName("id_repair")
    val repairId: Int,

    @SerializedName("repairs")
    val repairs: SupabaseTask? = null
)