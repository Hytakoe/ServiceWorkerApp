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
        val carInfo = client_cars?.let { car ->
            // Получаем модель авто из spr_cars (нужен отдельный запрос)
            // Пока используем номер
            "Авто #${car.id} (${car.licensePlate})"
        } ?: "Авто #$carId"

        return Task(
            id = id,
            carName = carInfo,
            job = workResult,
            comment = comment
        )
    }
}