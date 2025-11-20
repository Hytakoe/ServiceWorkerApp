package com.example.mobileapp.data.repository

import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.model.TaskResult

class TaskRepositoryImpl: TaskRepository {
    private val tasksMap = mutableMapOf<Int, Task>().apply {
        put(1, Task(1, "BMW X5 к321нр36", "Замена масла","Масло 5w30, фильтр HU9254x"))
        put(2, Task(2, "Audi A4 e567кх78", "Диагностика подвески","Стук спереди на неровностях"))
        put(3, Task(3, "Kia Sportage а123вр77", "Замена тормозных колодок","Передние колодки, диски в норме"))
        put(4, Task(4, "Lada Vesta о444оо99", "ТО-2","Полное техническое обслуживание"))
        put(5, Task(5, "Hyundai Creta с555ср50", "Ремонт кондиционера","Заправка фреоном, замена салонного фильтра"))
        put(6, Task(6, "Mercedes GLC м666мм177", "Шиномонтаж","Сезонная замена резины"))
        put(7, Task(7, "Toyota Camry т777ту78", "Замена свечей зажигания","Платина NGK BKR6EQUP"))
        put(8, Task(8, "Volkswagen Tiguan в888вв79", "Обслуживание АКПП","Замена масла в коробке, фильтр OEM"))
    }

    override suspend fun getTaskById(TaskId: Int): TaskResult<Task> {
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
    }

    override suspend fun getAllTasks(): TaskResult<List<Task>> {
        return try {
            TaskResult.Success(tasksMap.values.toList())
        } catch (e: Exception) {
            TaskResult.Error("Failed to get all Tasks: ${e.message}")
        }
    }
}