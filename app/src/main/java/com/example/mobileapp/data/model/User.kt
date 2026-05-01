package com.example.mobileapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val surname: String,
    val title: String,  // Должность
    val phoneNumber: String? = null,
    val email: String? = null
)

data class WorkshopWorker(
    @SerializedName("id_worker")
    val id: Int,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("surname")
    val surname: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("title")
    val title: String, // Должность

    @SerializedName("salary")
    val salary: Int,

    @SerializedName("hire_date")
    val hireDate: String,

    @SerializedName("schedule")
    val schedule: String,

    @SerializedName("fire_date")
    val fireDate: String? = null
)