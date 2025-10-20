package com.bekircaglar.wepick.di

import com.bekircaglar.wepick.data.manager.UserSession
import com.bekircaglar.wepick.data.repository.CategoryRepositoryImp
import com.bekircaglar.wepick.data.repository.FirebaseStatusRepository
import com.bekircaglar.wepick.data.repository.JoinRepositoryImp
import com.bekircaglar.wepick.data.repository.LaunchRepositoryImp
import com.bekircaglar.wepick.data.repository.RoomRepositoryImp
import com.bekircaglar.wepick.data.repository.SelectionRepositoryImp
import com.bekircaglar.wepick.domain.repository.CategoryRepository
import com.bekircaglar.wepick.domain.repository.JoinRepository
import com.bekircaglar.wepick.domain.repository.LaunchRepository
import com.bekircaglar.wepick.domain.repository.RoomRepository
import com.bekircaglar.wepick.domain.repository.SelectionRepository
import com.bekircaglar.wepick.domain.service.OmdbApiService
import com.bekircaglar.wepick.domain.usecase.category.CreateRoomUseCase
import com.bekircaglar.wepick.domain.usecase.join.JoinRoomUseCase
import com.bekircaglar.wepick.domain.usecase.launch.GetUsersByIdListUseCase
import com.bekircaglar.wepick.domain.usecase.launch.SetUserUseCase
import com.bekircaglar.wepick.domain.usecase.room.CheckUserInRoomUseCase
import com.bekircaglar.wepick.domain.usecase.room.ExitRoomUseCase
import com.bekircaglar.wepick.domain.usecase.room.GetRoomUseCase
import com.bekircaglar.wepick.domain.usecase.room.ObserveRoomMemersUseCase
import com.bekircaglar.wepick.domain.usecase.room.SetUserReadyStatusUseCase
import com.bekircaglar.wepick.domain.usecase.room.StartGameUseCase
import com.bekircaglar.wepick.domain.usecase.selection.ClearRoomUseCase
import com.bekircaglar.wepick.domain.usecase.selection.GetMovieListUseCase
import com.bekircaglar.wepick.domain.usecase.selection.LikeSelectionItemUseCase
import com.bekircaglar.wepick.domain.usecase.selection.ObserveMatchUseCase
import com.bekircaglar.wepick.presentation.screens.categoryscreen.CategoryViewModel
import com.bekircaglar.wepick.presentation.screens.createroom.CreateRoomViewModel
import com.bekircaglar.wepick.presentation.screens.joinroom.JoinViewModel
import com.bekircaglar.wepick.presentation.screens.launch.LaunchViewModel
import com.bekircaglar.wepick.presentation.screens.selectionscreen.SelectionViewModel
import com.bekircaglar.wepick.utils.StatusManagerFactory
import com.bekircaglar.wepick.utils.createStatusManagerFactory
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

        single<StatusManagerFactory> { createStatusManagerFactory() }

        singleOf(::SelectionViewModel)

        singleOf(::CategoryRepositoryImp) {
            bind<CategoryRepository>()
        }

        singleOf(::RoomRepositoryImp) {
            bind<RoomRepository>()
        }

        singleOf(::LaunchRepositoryImp) {
            bind<LaunchRepository>()
        }

        singleOf(::JoinRepositoryImp) {
            bind<JoinRepository>()
        }

        singleOf(::SelectionRepositoryImp) {
            bind<SelectionRepository>()
        }

        singleOf(::FirebaseStatusRepository)
        singleOf(::OmdbApiService)

        factoryOf(::SetUserReadyStatusUseCase)
        factoryOf(::CheckUserInRoomUseCase)
        factoryOf(::ObserveRoomMemersUseCase)
        factoryOf(::JoinRoomUseCase)
        factoryOf(::GetUsersByIdListUseCase)
        factoryOf(::SetUserUseCase)
        factoryOf(::CreateRoomUseCase)
        factoryOf(::GetRoomUseCase)
        factoryOf(::ExitRoomUseCase)
        factoryOf(::StartGameUseCase)
        factoryOf(::GetMovieListUseCase)
        factoryOf(::LikeSelectionItemUseCase)
        factoryOf(::ObserveMatchUseCase)
        factoryOf(::ClearRoomUseCase)



        viewModelOf(::CreateRoomViewModel)
        viewModelOf(::LaunchViewModel)
        viewModelOf(::CategoryViewModel)
        viewModelOf(::LaunchViewModel)
        viewModelOf(::JoinViewModel)

    }
}