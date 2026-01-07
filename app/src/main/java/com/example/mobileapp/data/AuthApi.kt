// data/AuthApi.kt
package com.example.mobileapp.data

import com.example.mobileapp.data.model.WorkshopWorker
import com.example.mobileapp.data.model.WorkerCredentials
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    // Поиск сотрудника по имени и фамилии
    @GET("workshop_worker")
    @Headers(
        "apikey: ${SupabaseApi.PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseApi.PUBLISHABLE_KEY}"
    )
    suspend fun findWorkerByName(
        @Query("name") name: String,
        @Query("surname") surname: String
    ): List<WorkshopWorker>

    // data/AuthApi.kt
    @GET("worker_credentials")
    @Headers(
        "apikey: ${SupabaseApi.PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseApi.PUBLISHABLE_KEY}"
    )
    suspend fun getCredentialsByWorkerId(
        @Query("worker_id") workerId: String // Используйте worker_id
    ): List<WorkerCredentials>

    // Создать новые учетные данные
    @POST("worker_credentials")
    @Headers(
        "apikey: ${SupabaseApi.PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseApi.PUBLISHABLE_KEY}",
        "Prefer: return=representation"
    )
    suspend fun createCredentials(@Body credentials: WorkerCredentials): List<WorkerCredentials>

    // Получить всех сотрудников (для отладки)
    @GET("workshop_worker")
    @Headers(
        "apikey: ${SupabaseApi.PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseApi.PUBLISHABLE_KEY}"
    )
    suspend fun getAllWorkers(): List<WorkshopWorker>

}