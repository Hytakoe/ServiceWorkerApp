package com.example.mobileapp.ui.work_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.model.TaskInWorkList
import com.example.mobileapp.data.model.TaskResult
import com.example.mobileapp.data.model.WorkListResult
import com.example.mobileapp.domain.usecase.AddToWorkListUseCase
import com.example.mobileapp.domain.usecase.CompleteTaskUseCase
import com.example.mobileapp.domain.usecase.CreateTaskUseCase
import com.example.mobileapp.domain.usecase.GetTasksUseCase
import com.example.mobileapp.domain.usecase.GetWorkListItemsUseCase
import com.example.mobileapp.domain.usecase.RemoveFromWorkListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkListViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val tasksList = getTasksUseCase()
                _tasks.value = tasksList
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
                // В случае ошибки показываем мок-данные
                loadMockTasks()
            } finally {
                _loading.value = false
            }
        }
    }

    fun createTask(carId: Int, job: String, comment: String?) {
        viewModelScope.launch {
            try {
                val success = createTaskUseCase(carId, job, comment)
                if (success) {
                    loadTasks() // Обновляем список
                } else {
                    _error.value = "Не удалось создать задачу"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка: ${e.message}"
            }
        }
    }

    private fun loadMockTasks() {
        _tasks.value = listOf(
            Task(1, "BMW X5 к321нр36", "Замена масла", "Масло 5w30, фильтр HU9254x"),
            Task(2, "Audi A4 e567кх78", "Диагностика подвески", "Стук спереди на неровностях"),
            Task(3, "Kia Sportage а123вр77", "Замена тормозных колодок", "Передние колодки, диски в норме")
        )
    }
}