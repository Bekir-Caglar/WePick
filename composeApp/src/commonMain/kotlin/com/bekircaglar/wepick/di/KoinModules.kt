package com.bekircaglar.wepick.di

import com.bekircaglar.wepick.data.repository.UserRepository
import com.bekircaglar.wepick.domain.usecase.GetUserUseCase
import com.bekircaglar.wepick.presentation.screens.launch.LaunchViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single { UserRepository() }

        factoryOf(::GetUserUseCase)

        viewModelOf(::LaunchViewModel)
    }
}