import com.example.mobileapp.data.model.RepairWithTask
import com.example.mobileapp.data.model.RepairWorker
import com.example.mobileapp.data.model.WorkerCredentials
import com.example.mobileapp.data.model.WorkshopWorker
import com.example.mobileapp.data.model.supabase.CarModelInfo
import com.example.mobileapp.data.model.supabase.SupabaseCar
import com.example.mobileapp.data.model.supabase.SupabaseTask
import com.example.mobileapp.data.model.supabase.TaskRequest
import retrofit2.http.*

interface SupabaseApi {
    companion object {
        const val PUBLISHABLE_KEY = "sb_publishable_ACJWlzQHlZjBrEguHvfOxg_3BJgxAaH"
    }

    @GET("repairs")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getRepairsWithCars(
        @Query("select") select: String = "*,client_cars(*)"
    ): List<SupabaseTask>

    @GET("repairs")
    @Headers(
        "apublishable: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getRepairsByCarId(
        @Query("id_car") carId: Int
    ): List<SupabaseTask>

    @POST("repairs")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY",
        "Prefer: return=representation"
    )
    suspend fun createRepair(@Body task: TaskRequest): List<SupabaseTask>

    @PATCH("repairs")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY",
        "Prefer: return=representation"
    )
    suspend fun completeRepair(
        @Query("id_repair") repairId: Int,
        @Body updates: Map<String, Any>
    ): List<SupabaseTask>

    @GET("client_cars")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getClientCars(): List<SupabaseCar>

    @GET("spr_cars")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getCarModels(): List<CarModelInfo>

    @GET("spr_cars")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getCarModelById(
        @Query("id_car") carId: Int
    ): List<CarModelInfo>

    @GET("repairs")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun searchRepairs(
        @Query("client_cars.license_plate") like: String,
        @Query("select") select: String = "*,client_cars(*)"
    ): List<SupabaseTask>

    @GET("workshop_worker")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun findWorkerByName(
        @Query("name") name: String,
        @Query("surname") surname: String
    ): List<WorkshopWorker>

    @GET("workshop_worker")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun findWorker(
        @Query("name") name: String,
        @Query("surname") surname: String
    ): List<WorkshopWorker>

    @GET("worker_credentials")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getCredentialsByWorkerId(
        @Query("id_worker") workerIdEq: Int
    ): List<WorkerCredentials>

    @GET("workshop_worker")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getAllWorkers(): List<WorkshopWorker>

    @GET("repairs_workers")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getRepairsWorkers(): List<RepairWorker>

    @GET("repairs_workers")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getRepairsByWorkerId(
        @Query("id_worker") workerId: Int,
        @Query("select") select: String = "*,repairs(*,client_cars(*))"
    ): List<RepairWithTask>

    @POST("repairs_workers")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY",
        "Prefer: return=representation"
    )
    suspend fun assignTaskToWorker(@Body assignment: RepairWorker): List<RepairWorker>
    @GET("repairs")
    @Headers(
        "apikey: $PUBLISHABLE_KEY",
        "Authorization: Bearer $PUBLISHABLE_KEY"
    )
    suspend fun getRepairById(
        @Query("id_repair") repairId: Int,
        @Query("select") select: String = "*,client_cars(*)"
    ): List<SupabaseTask>

}