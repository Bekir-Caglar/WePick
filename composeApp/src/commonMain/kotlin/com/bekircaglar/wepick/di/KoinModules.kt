package com.bekircaglar.wepick.di

import com.bekircaglar.wepick.data.repository.CategoryRepositoryImp
import com.bekircaglar.wepick.data.repository.JoinRepositoryImp
import com.bekircaglar.wepick.data.repository.LaunchRepositoryImp
import com.bekircaglar.wepick.data.repository.RoomRepositoryImp
import com.bekircaglar.wepick.domain.repository.CategoryRepository
import com.bekircaglar.wepick.domain.repository.JoinRepository
import com.bekircaglar.wepick.domain.repository.LaunchRepository
import com.bekircaglar.wepick.domain.repository.RoomRepository
import com.bekircaglar.wepick.domain.usecase.category.CreateRoomUseCase
import com.bekircaglar.wepick.domain.usecase.category.GetCategoryListUseCase
import com.bekircaglar.wepick.domain.usecase.join.JoinRoomUseCase
import com.bekircaglar.wepick.domain.usecase.launch.GetUsersByIdListUseCase
import com.bekircaglar.wepick.domain.usecase.launch.SetUserUseCase
import com.bekircaglar.wepick.domain.usecase.room.ExitRoomUseCase
import com.bekircaglar.wepick.domain.usecase.room.GetRoomUseCase
import com.bekircaglar.wepick.presentation.screens.categoryscreen.CategoryViewModel
import com.bekircaglar.wepick.presentation.screens.createroom.CreateRoomViewModel
import com.bekircaglar.wepick.presentation.screens.joinroom.JoinViewModel
import com.bekircaglar.wepick.presentation.screens.launch.LaunchViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single { Firebase.database.reference() }

        singleOf(::CategoryRepositoryImp) {
            bind<CategoryRepository>()
        }

        singleOf(::RoomRepositoryImp) {
            bind<RoomRepository>()
        }

        singleOf(::LaunchRepositoryImp){
            bind<LaunchRepository>()
        }

        singleOf(::JoinRepositoryImp){
            bind<JoinRepository>()
        }

        factoryOf(::JoinRoomUseCase)
        factoryOf(::GetUsersByIdListUseCase)
        factoryOf(::SetUserUseCase)
        factoryOf(::CreateRoomUseCase)
        factoryOf(::GetCategoryListUseCase)
        factoryOf(::GetRoomUseCase)
        factoryOf(::ExitRoomUseCase)



        viewModelOf(::CreateRoomViewModel)
        viewModelOf(::LaunchViewModel)
        viewModelOf(::CategoryViewModel)
        viewModelOf(::LaunchViewModel)
        viewModelOf(::JoinViewModel)

    }
}