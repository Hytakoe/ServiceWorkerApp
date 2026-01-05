package com.example.mobileapp.data.repository


import com.example.mobileapp.data.RetrofitClient
import com.example.mobileapp.data.RetrofitClient.api
import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.model.TaskResult
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
    //private val api = RetrofitClient.supabaseApi
    private var carModelsCache: Map<Int, CarModelInfo> = emptyMap()
    private val tasksMap = mutableMapOf<Int, Task>()/*.apply {
        put(1, Task(1, "BMW X5 к321нр36", "Замена масла","Масло 5w30, фильтр HU9254x"))
        put(2, Task(2, "Audi A4 e567кх78", "Диагностика подвески","Стук спереди на неровностях"))
        put(3, Task(3, "Kia Sportage а123вр77", "Замена тормозных колодок","Передние колодки, диски в норме"))
        put(4, Task(4, "Lada Vesta о444оо99", "ТО-2","Полное техническое обслуживание"))
        put(5, Task(5, "Hyundai Creta с555ср50", "Ремонт кондиционера","Заправка фреоном, замена салонного фильтра"))
        put(6, Task(6, "Mercedes GLC м666мм177", "Шиномонтаж","Сезонная замена резины"))
        put(7, Task(7, "Toyota Camry т777ту78", "Замена свечей зажигания","Платина NGK BKR6EQUP"))
        put(8, Task(8, "Volkswagen Tiguan в888вв79", "Обслуживание АКПП","Замена масла в коробке, фильтр OEM"))
    }*/

    /*override suspend fun getTaskById(TaskId: Int): TaskResult<Task> {
        return try {
            val Task = tasksMap[TaskId]
            if (Task != null) {
                TaskResult.Success(Task)
            } else {
                TaskResult.Error("Задача с id=${TaskId} не найден")
            }
        } catch (e: Exception) {
            TaskResult.Error("Failed to get Tasks: ${e.message}")
        }
    }*/

    /*override suspend fun getAllTasks(): TaskResult<List<Task>> {
        return try {
            TaskResult.Success(tasksMap.values.toList())
        } catch (e: Exception) {
            TaskResult.Error("Failed to get all Tasks: ${e.message}")
        }
    }
    override suspend fun getTasksWithCars(): List<TaskWithCar> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getTasksWithCars()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            } as List<TaskWithCar>
        }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getTaskById(id)
                if (response.isSuccessful) {
                    response.body()?.firstOrNull()
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun getTasksByCarId(carId: Int): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getTasksByCarId(carId)
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun createTask(task: TaskRequest): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.createTask(task)
                response.isSuccessful
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
                val response = api.updateTask(taskId, updates)
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun searchTasksByLicensePlate(licensePlate: String): List<TaskWithCar> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchTasksByLicensePlate(licensePlate)
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    private fun getCurrentDateTime(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            .format(Date())
    }*/
    override suspend fun getTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Загружаем задачи из Supabase
                val supabaseTasks = api.getRepairsWithCars()

                // 2. Загружаем модели авто для маппинга
                loadCarModels()

                // 3. Преобразуем в ваши Task
                supabaseTasks.map { supabaseTask ->
                    supabaseTask.toTask(
                        carModels = carModelsCache,
                        carInfo = supabaseTask.client_cars
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Возвращаем мок-данные при ошибке
                getMockTasks()
            }
        }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return withContext(Dispatchers.IO) {
            try {
                // Получаем все задачи и фильтруем
                val tasks = getTasks()
                tasks.firstOrNull { it.id == id }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun createTask(carId: Int, job: String, comment: String?): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val taskRequest = TaskRequest(
                    carId = carId,
                    job = job,
                    comment = comment,
                    cost = 0 // Можно добавить поле стоимости позже
                )

                val result = api.createRepair(taskRequest)
                result.isNotEmpty()
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

    // Вспомогательные методы

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

// Расширение для преобразования SupabaseTask в Task
private fun SupabaseTask.toTask(
    carModels: Map<Int, CarModelInfo>,
    carInfo: SupabaseCar?
): Task {
    val carName = buildCarName(carModels, carInfo)

    return Task(
        id = id,
        carName = carName,
        job = workResult,
        comment = comment
    )
}

private fun buildCarName(
    carModels: Map<Int, CarModelInfo>,
    carInfo: SupabaseCar?
): String {
    if (carInfo == null) return "Авто 0"

    // Ищем модель авто по vehicleId
    val carModel = carModels[carInfo.vehicleId]

    return if (carModel != null) {
        "${carModel.brand} ${carModel.model} ${carInfo.licensePlate}"
    } else {
        "Авто ${carInfo.licensePlate}"
    }
}