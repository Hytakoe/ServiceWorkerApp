package com.example.mobileapp.di

import com.example.mobileapp.data.repository.AuthRepository
import com.example.mobileapp.data.repository.AuthRepositoryImpl
import com.example.mobileapp.domain.usecase.SignInUseCase
import com.example.mobileapp.domain.usecase.SignUpUseCase
import com.example.mobileapp.ui.sign_in.SignInViewModel
import com.example.mobileapp.ui.sign_up.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single<AuthRepository> { AuthRepositoryImpl() }
        factory { SignInUseCase(get()) }
        factory { SignUpUseCase(get()) }

        viewModel { SignInViewModel(get()) }
        viewModel { SignUpViewModel(get()) }
    }
}