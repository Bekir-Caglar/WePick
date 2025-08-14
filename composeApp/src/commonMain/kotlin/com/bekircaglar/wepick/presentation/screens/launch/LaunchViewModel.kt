package com.bekircaglar.wepick.presentation.screens.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.wepick.data.UserSession
import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.domain.usecase.launch.GetUsersByIdListUseCase
import com.bekircaglar.wepick.domain.usecase.launch.SetUserUseCase
import kotlinx.coroutines.launch

class LaunchViewModel(
    private val setUserUseCase: SetUserUseCase,
) : ViewModel() {

    fun setUser() = viewModelScope.launch {
        val user = User(
            id = UserSession.id,
            name = UserSession.nickname,
            emoji = UserSession.emoji,
        )
        setUserUseCase(user).collect {
        }
    }
}
