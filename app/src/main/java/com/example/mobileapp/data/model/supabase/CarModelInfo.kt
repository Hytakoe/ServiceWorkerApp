package com.example.mobileapp.data.model.supabase

import com.google.gson.annotations.SerializedName

data class CarModelInfo(
    @SerializedName("id_car")
    val id: Int,

    @SerializedName("brand")
    val brand: String, // "BMW"

    @SerializedName("model")
    val model: String, // "X5"

    @SerializedName("model_year")
    val modelYear: Int,

    @SerializedName("config")
    val config: String? = null
) {
    fun getFullName(): String {
        return "$brand $model (${config ?: "$modelYear г."})"
    }
}