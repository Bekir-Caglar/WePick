package com.bekircaglar.wepick.di

import com.bekircaglar.wepick.data.repository.UserRepository
import com.bekircaglar.wepick.domain.usecase.GetUserUseCase
import com.bekircaglar.wepick.presentation.screens.detail.DetailViewModel
import com.bekircaglar.wepick.presentation.screens.home.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single { UserRepository() }

        factoryOf(::GetUserUseCase)

        viewModelOf(::HomeViewModel)
        viewModelOf(::DetailViewModel)
    }
}