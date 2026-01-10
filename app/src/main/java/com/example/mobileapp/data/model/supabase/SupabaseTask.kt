package com.example.mobileapp.data.model.supabase

import com.example.mobileapp.data.model.Task
import com.google.gson.annotations.SerializedName

// Модель для данных из таблицы repairs
data class SupabaseTask(
    @SerializedName("id_repair")
    val id: Int = 0,


    @SerializedName("id_car")
    val carId: Int = 0,

    @SerializedName("work_result")
    val workResult: String = "",

    @SerializedName("comment")
    val comment: String? = null,

    @SerializedName("issue_date")
    val issueDate: String = "",

    @SerializedName("cost_of_work")
    val cost: Int = 0,

    @SerializedName("date_of_finish")
    val finishDate: String? = null,

    // Для JOIN с автомобилями
    val client_cars: SupabaseCar? = null
) {
    // Преобразование в вашу модель Task
    fun toTask(): Task {
        // Преобразование в Task
        return Task(
            id = id,
            carName = "Авто #$carId", // или другая логика
            job = workResult,
            comment = comment,
            finishDate = finishDate
        )
    }
}