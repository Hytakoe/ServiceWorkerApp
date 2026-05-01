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

    @GET("workshop_worker")
    @Headers(
        "apikey: ${SupabaseApi.PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseApi.PUBLISHABLE_KEY}"
    )
    suspend fun findWorkerByName(
        @Query("name") name: String,
        @Query("surname") surname: String
    ): List<WorkshopWorker>

    @GET("worker_credentials")
    @Headers(
        "apikey: ${SupabaseApi.PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseApi.PUBLISHABLE_KEY}"
    )
    suspend fun getCredentialsByWorkerId(
        @Query("worker_id") workerId: String
    ): List<WorkerCredentials>

    @POST("worker_credentials")
    @Headers(
        "apikey: ${SupabaseApi.PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseApi.PUBLISHABLE_KEY}",
        "Prefer: return=representation"
    )
    suspend fun createCredentials(@Body credentials: WorkerCredentials): List<WorkerCredentials>

    @GET("workshop_worker")
    @Headers(
        "apikey: ${SupabaseApi.PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseApi.PUBLISHABLE_KEY}"
    )
    suspend fun getAllWorkers(): List<WorkshopWorker>

}