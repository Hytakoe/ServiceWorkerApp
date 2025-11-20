package com.example.mobileapp.ui.catalogue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.model.TaskInWorkList
import com.example.mobileapp.data.model.TaskResult
import com.example.mobileapp.data.model.WorkListResult
import com.example.mobileapp.domain.usecase.AddToWorkListUseCase
import com.example.mobileapp.domain.usecase.GetAllTasksUseCase
import com.example.mobileapp.domain.usecase.GetWorkListItemsUseCase
import com.example.mobileapp.domain.usecase.RemoveFromWorkListUseCase
import kotlinx.coroutines.launch

class WorkListViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val addToWorkListUseCase: AddToWorkListUseCase,
    private val removeFromWorkListUseCase: RemoveFromWorkListUseCase,
    private val getWorkListItemsUseCase: GetWorkListItemsUseCase,
    private val currentUserId: Int
): ViewModel() {

    // состояние UI
    private val _uiState = MutableLiveData(WorkListUiState())
    val uiState: LiveData<WorkListUiState> = _uiState

    // все продукты изначально загружаются из репозитория/бд
    private val allTasks = mutableListOf<Task>()
    // кэш корзины пользователя
    private val userWorkListItems = mutableListOf<TaskInWorkList>()

    init {
        loadInitialData()
    }

    data class WorkListUiState(
        val workListUiItems: List<WorkListUiItem> = emptyList(),
        val searchQuery: String = "",
        val selectedFilter: TaskFilter = TaskFilter.NONE,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value?.copy(searchQuery = query)
        //applyFilterAndSearch()
    }

    fun onFilterSelected(filter: TaskFilter) {
        _uiState.value = _uiState.value?.copy(selectedFilter = filter)
        //applyFilterAndSearch()
    }

    fun onAddingToWorkList(taskId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true)

            when (val result = addToWorkListUseCase(currentUserId, taskId)) {
                is WorkListResult.Success -> loadUserWorkList()
                is WorkListResult.Error -> {
                    _uiState.value = _uiState.value?.copy(errorMessage = result.message, isLoading = false)
                }
                is WorkListResult.Loading -> {}
            }
        }
    }

    // ЗАГЛУШКА
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true)

            when (val result = getAllTasksUseCase()) {
                is TaskResult.Success -> {
                    allTasks.clear()
                    allTasks.addAll(result.data)

                    loadUserWorkList()

                    //applyFilterAndSearch()
                    _uiState.value = _uiState.value?.copy(isLoading = false)
                }
                is TaskResult.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        errorMessage = result.message,
                        isLoading = false
                    )
                } else -> { }
            }
        }
    }

    fun onRemovingFromWorkList(taskId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true)

            when (val result = removeFromWorkListUseCase(currentUserId, taskId)) {
                is WorkListResult.Success -> loadUserWorkList()
                is WorkListResult.Error -> {
                    _uiState.value = _uiState.value?.copy(errorMessage = result.message, isLoading = false)
                }
                is WorkListResult.Loading -> {}
            }
        }
    }

    private suspend fun loadUserWorkList() {
        when (val result = getWorkListItemsUseCase(currentUserId)) {
            is WorkListResult.Success -> {
                userWorkListItems.clear()
                userWorkListItems.addAll(result.data)
                //applyFilterAndSearch()
            }
            is WorkListResult.Error -> _uiState.value = _uiState.value?.copy(errorMessage = result.message)
            is WorkListResult.Loading -> _uiState.value = _uiState.value?.copy(isLoading = true)
        }
    }

    //заглушка
//    private fun loadTasksFromRepository() {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value?.copy(isLoading = true)
//
//            when (val result = getAllTasksUseCase()) { // suspend operator fun может быть вызвана из Корутин или из suspend fun
//                is TaskResult.Success -> {
//                    allTasks.clear()
//                    allTasks.addAll(result.data)
//                    applyFilterAndSearch()
//                    _uiState.value = _uiState.value?.copy(isLoading = false)
//                }
//                is TaskResult.Error -> {
//                    _uiState.value = _uiState.value?.copy(
//                        errorMessage = result.message,
//                        isLoading = false
//                    )
//                }
//                is TaskResult.Loading -> null
//            }
//        }
//    }

    /*private fun applyFilterAndSearch() {
        val state = _uiState.value
        var filteredTasks = allTasks

        // применяем поиск
        if (state.searchQuery.isNotBlank()) {
            filteredTasks = filteredTasks.filter { task ->
                task.job.startsWith(state.searchQuery)
            }.toMutableList()
        }

        // применяем выбранный фильтр
        filteredTasks = when (state.selectedFilter) {
            TaskFilter.NONE -> filteredTasks
            TaskFilter.PRICE_LOW_TO_HIGH -> filteredTasks.sortedBy { task -> task.price }.toMutableList()
            TaskFilter.PRICE_HIGH_TO_LOW -> filteredTasks.sortedByDescending { task -> task.price }.toMutableList()
            TaskFilter.NAME_A_TO_Z -> filteredTasks.sortedBy { task -> task.name }.toMutableList()
            TaskFilter.NAME_Z_TO_A -> filteredTasks.sortedByDescending { task -> task.name }.toMutableList()
        }

        // создаем ui для модели
        val catalogueUiItems = filteredTasks.map {
                task -> CatalogueUiItem(
            task,
            quantityInWorkList = userWorkListItems.find { it.taskId == task.id }?.quantity ?: 0
        )
        }

        _uiState.value = state?.copy(
            catalogueUiItems=catalogueUiItems,
            isLoading = false
        ) ?: CatalogueUiState (catalogueUiItems = catalogueUiItems)
    }*/
}

enum class TaskFilter {
    NONE,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    NAME_A_TO_Z,
    NAME_Z_TO_A
}