package com.example.mobileapp.data.repository


import android.util.Log
import com.example.mobileapp.data.RetrofitClient.api
import com.example.mobileapp.data.SessionManager
import com.example.mobileapp.data.model.RepairWorker
import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.model.supabase.CarModelInfo
import com.example.mobileapp.data.model.supabase.SupabaseCar
import com.example.mobileapp.data.model.supabase.SupabaseTask
import com.example.mobileapp.data.model.supabase.TaskRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskRepositoryImpl: TaskRepository {

    private val sessionManager: SessionManager? = null
    private var carModelsCache: Map<Int, CarModelInfo> = emptyMap()

    private fun getMockTasksForWorker(workerId: Int): List<Task> {
        return when (workerId) {
            1 -> listOf( // Ратмир Селютин
                Task(1, "BMW X5 к321нр36", "Замена масла", "Масло 5w30, фильтр HU9254x"),
                Task(3, "Kia Sportage а123вр77", "Замена тормозных колодок", "Передние колодки, диски в норме"),
                Task(5, "Hyundai Creta с555ср50", "Ремонт кондиционера", "Заправка фреоном")
            )
            2 -> listOf( // Иван Иванов
                Task(2, "Audi A4 e567кх78", "Диагностика подвески", "Стук спереди на неровностях"),
                Task(6, "Mercedes GLC м666мм177", "Шиномонтаж", "Сезонная замена резины")
            )
            3 -> listOf( // Мария Петрова
                Task(4, "Lada Vesta о444оо99", "ТО-2", "Полное техническое обслуживание"),
                Task(7, "Toyota Camry т777ту78", "Замена свечей зажигания", "Платина NGK BKR6EQUP"),
                Task(8, "Volkswagen Tiguan в888вв79", "Обслуживание АКПП", "Замена масла в коробке")
            )
            else -> getMockTasks()
        }
    }
    override suspend fun getTasksForWorker(workerId: Int): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("TaskRepository", "getTasksForWorker вызван для workerId: $workerId")

                val allLinks = api.getRepairsWorkers()
                Log.d("TaskRepository", "Всего связей в repairs_workers: ${allLinks.size}")

                val workerLinks = allLinks.filter { it.workerId == workerId }
                Log.d("TaskRepository", "Связей для workerId $workerId: ${workerLinks.size}")

                if (workerLinks.isEmpty()) {
                    Log.w("TaskRepository", "Нет связей для workerId $workerId")
                    return@withContext emptyList()
                }

                val workerRepairIds = workerLinks.map { it.repairId }
                Log.d("TaskRepository", "ID задач сотрудника: $workerRepairIds")

                val allRepairs = api.getRepairsWithCars()
                Log.d("TaskRepository", "Всего задач в системе: ${allRepairs.size}")

                val workerRepairs = allRepairs.filter { repair ->
                    workerRepairIds.contains(repair.id)
                }
                Log.d("TaskRepository", "Задач для сотрудника: ${workerRepairs.size}")

                val carModelsCache = getCarModels()
                val tasks = workerRepairs.mapNotNull { repair ->
                    try {
                        repair.toTask(
                            carModels = carModelsCache,
                            carInfo = repair.client_cars
                        )
                    } catch (e: Exception) {
                        Log.e("TaskRepository", "Ошибка преобразования задачи ${repair.id}", e)
                        null
                    }
                }

                Log.d("TaskRepository", "Итоговых задач: ${tasks.size}")
                tasks

            } catch (e: Exception) {
                Log.e("TaskRepository", "Ошибка getTasksForWorker: ${e.message}", e)
                getMockTasksForWorker(workerId)
            }
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                val supabaseTasks = api.getRepairsWithCars()
                val carModelsCache = getCarModels()

                supabaseTasks.mapNotNull { supabaseTask ->
                    try {
                        supabaseTask.toTask(
                            carModels = carModelsCache,
                            carInfo = supabaseTask.client_cars
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
            } catch (e: Exception) {
                getMockTasks()
            }
        }
    }
    override suspend fun getTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                val supabaseTasks = api.getRepairsWithCars()

                loadCarModels()

                supabaseTasks.map { supabaseTask ->
                    supabaseTask.toTask(
                        carModels = carModelsCache,
                        carInfo = supabaseTask.client_cars
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getMockTasks()
            }
        }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return withContext(Dispatchers.IO) {
            try {
                val tasks = getTasks()
                tasks.firstOrNull { it.id == id }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun createTask(carId: Int, job: String, comment: String?, workerId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val taskRequest = TaskRequest(
                    carId = carId,
                    job = job,
                    comment = comment,
                    cost = 0
                )

                val result = api.createRepair(taskRequest)

                if (result.isNotEmpty()) {
                    val repairId = result.first().id
                    val assignment = RepairWorker(workerId, repairId)
                    api.assignTaskToWorker(assignment)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun completeTask(taskId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val updates = mapOf(
                    "date_of_finish" to getCurrentDateTime()
                )
                val result = api.completeRepair(taskId, updates)
                result.isNotEmpty()
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun getCarModels(): Map<Int, CarModelInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val models = api.getCarModels()
                models.associateBy { it.id }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyMap()
            }
        }
    }

    private suspend fun loadCarModels() {
        if (carModelsCache.isEmpty()) {
            carModelsCache = getCarModels()
        }
    }

    private fun getCurrentDateTime(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            .format(Date())
    }

    private fun getMockTasks(): List<Task> {
        return listOf(
            Task(1, "BMW X5 к321нр36", "Замена масла", "Масло 5w30, фильтр HU9254x"),
            Task(2, "Audi A4 e567кх78", "Диагностика подвески", "Стук спереди на неровностях"),
            Task(3, "Kia Sportage а123вр77", "Замена тормозных колодок", "Передние колодки, диски в норме")
        )
    }

}
private fun SupabaseTask.toTask(
    carModels: Map<Int, CarModelInfo>,
    carInfo: SupabaseCar?
): Task {
    val carName = buildCarName(carModels, carInfo)

    return Task(
        id = id,
        carName = carName,
        job = workResult,
        comment = comment,
        finishDate = finishDate
    )
}

private fun buildCarName(
    carModels: Map<Int, CarModelInfo>,
    carInfo: SupabaseCar?
): String {
    if (carInfo == null) return "Авто 0"

    val carModel = carModels[carInfo.vehicleId]

    return if (carModel != null) {
        "${carModel.brand} ${carModel.model} ${carInfo.licensePlate}"
    } else {
        "Авто ${carInfo.licensePlate}"
    }
}