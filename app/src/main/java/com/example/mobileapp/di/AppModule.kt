package com.example.mobileapp.di

import com.example.mobileapp.data.SessionManager
import com.example.mobileapp.data.repository.AuthRepository
import com.example.mobileapp.data.repository.AuthRepositoryImpl
import com.example.mobileapp.data.repository.TaskRepository
import com.example.mobileapp.data.repository.TaskRepositoryImpl
import com.example.mobileapp.data.repository.WorkListRepository
import com.example.mobileapp.data.repository.WorkListRepositoryImpl
import com.example.mobileapp.domain.usecase.AddToWorkListUseCase
import com.example.mobileapp.domain.usecase.CompleteTaskUseCase
import com.example.mobileapp.domain.usecase.CreateTaskUseCase
import com.example.mobileapp.domain.usecase.GetTasksUseCase
import com.example.mobileapp.domain.usecase.GetWorkListItemsUseCase
import com.example.mobileapp.domain.usecase.RemoveFromWorkListUseCase
import com.example.mobileapp.domain.usecase.SignInUseCase
import com.example.mobileapp.domain.usecase.SignUpUseCase
import com.example.mobileapp.ui.sign_in.SignInViewModel
import com.example.mobileapp.ui.sign_up.SignUpViewModel
import com.example.mobileapp.ui.work_list.WorkListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single<AuthRepository> { AuthRepositoryImpl() }
        single<TaskRepository> { TaskRepositoryImpl() } // ДОБАВЬТЕ ЭТО
        single<WorkListRepository> { WorkListRepositoryImpl(taskRepository = get()) }
        single {
            SessionManager(get())  // Используем Context из предыдущей строки
        }
        factory { SignInUseCase(get()) }
        factory { SignUpUseCase(get()) }
        factory { GetWorkListItemsUseCase(workListRepository = get()) }
        factory { AddToWorkListUseCase(workListRepository = get()) }
        factory { RemoveFromWorkListUseCase(workListRepository = get()) }
        factory { CreateTaskUseCase(get()) }
        factory { CompleteTaskUseCase(get()) }
        factory { GetTasksUseCase(get()) }

        viewModel {
            SignInViewModel(
                signInUseCase = get(),
                sessionManager = get()  // Добавляем SessionManager в ViewModel
            )
        }
        viewModel { SignUpViewModel(get()) }
        viewModel {
            WorkListViewModel(
                getTasksUseCase = get(),
                createTaskUseCase = get(),
                sessionManager = get()
            )
        }
    }
}