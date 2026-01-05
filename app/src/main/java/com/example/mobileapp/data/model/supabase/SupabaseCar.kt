package com.example.mobileapp.data.model.supabase

import com.google.gson.annotations.SerializedName

// Модель для данных из таблицы client_cars
data class SupabaseCar(
    @SerializedName("id_car")
    val id: Int,

    @SerializedName("client_id")
    val clientId: Int,

    @SerializedName("id_vehicle")
    val vehicleId: Int,

    @SerializedName("license_plate")
    val licensePlate: String,

    @SerializedName("production_year")
    val year: Int,

    @SerializedName("mileage")
    val mileage: Int
)