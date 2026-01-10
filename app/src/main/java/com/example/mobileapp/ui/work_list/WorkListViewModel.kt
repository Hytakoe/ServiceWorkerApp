// ui/work_list/WorkListViewModel.kt
package com.example.mobileapp.ui.work_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.SessionManager
import com.example.mobileapp.data.model.Task
import com.example.mobileapp.domain.usecase.CreateTaskUseCase
import com.example.mobileapp.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkListViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    init {
        loadTasksForCurrentUser()
    }

    fun loadTasksForCurrentUser() {
        val userId = sessionManager.getCurrentUserId()
        val userName = sessionManager.getCurrentUser()?.name
        val userSurname = sessionManager.getCurrentUser()?.surname

        _currentUser.value = if (userName != null && userSurname != null) {
            "$userName $userSurname"
        } else {
            null
        }

        if (userId == -1) {
            _error.value = "Пользователь не авторизован"
            _currentUser.value = null
            return
        }

        Log.d("WorkListViewModel", "🔄 Загружаем задачи для пользователя ID: $userId")
        loadTasksForUser(userId)
    }

    private fun loadTasksForUser(workerId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                Log.d("WorkListViewModel", "📞 Вызываем useCase для workerId: $workerId")
                val tasksList = getTasksUseCase(workerId)
                Log.d("WorkListViewModel", "✅ Получено задач: ${tasksList.size}")

                _tasks.value = tasksList

                // Для отладки выводим задачи
                tasksList.forEachIndexed { index, task ->
                    Log.d("WorkListViewModel", "   Задача $index: ${task.id} - ${task.carName}")
                }

            } catch (e: Exception) {
                Log.e("WorkListViewModel", "❌ Ошибка загрузки: ${e.message}", e)
                _error.value = "Ошибка загрузки задач: ${e.message}"
                loadMockTasksForUser(workerId)
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadTasks() {
        loadTasksForCurrentUser()
    }

    fun createTask(carId: Int, job: String, comment: String?, cost: Int) {
        viewModelScope.launch {
            try {
                val currentUserId = sessionManager.getCurrentUserId()
                if (currentUserId == -1) {
                    _error.value = "Необходимо войти в систему"
                    return@launch
                }

                val success = createTaskUseCase(carId, job, comment, cost)
                if (success) {
                    loadTasksForCurrentUser() // Обновляем список
                } else {
                    _error.value = "Не удалось создать задачу"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun completeTask(taskId: Int) {
        // ... реализация завершения задачи
    }

    private fun loadMockTasksForUser(workerId: Int) {
        Log.d("WorkListViewModel", "🔄 Используем мок-данные для workerId: $workerId")

        val mockTasks = when (workerId) {
            1 -> listOf(
                Task(1, "BMW X5 к321нр36", "Замена масла", "Масло 5w30, фильтр HU9254x"),
                Task(3, "Kia Sportage а123вр77", "Замена тормозных колодок", "Передние колодки, диски в норме"),
                Task(5, "Hyundai Creta с555ср50", "Ремонт кондиционера", "Заправка фреоном, замена салонного фильтра")
            )
            2 -> listOf(
                Task(2, "Audi A4 e567кх78", "Диагностика подвески", "Стук спереди на неровностях"),
                Task(6, "Mercedes GLC м666мм177", "Шиномонтаж", "Сезонная замена резины")
            )
            3 -> listOf(
                Task(4, "Lada Vesta о444оо99", "ТО-2", "Полное техническое обслуживание"),
                Task(7, "Toyota Camry т777ту78", "Замена свечей зажигания", "Платина NGK BKR6EQUP"),
                Task(8, "Volkswagen Tiguan в888вв79", "Обслуживание АКПП", "Замена масла в коробке, фильтр OEM")
            )
            else -> emptyList()
        }

        _tasks.value = mockTasks
        Log.d("WorkListViewModel", "📋 Загружено мок-задач: ${mockTasks.size}")
    }
}