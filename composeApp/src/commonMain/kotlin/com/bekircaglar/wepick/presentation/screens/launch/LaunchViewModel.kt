package com.bekircaglar.wepick.presentation.screens.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.wepick.data.UserSession
import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.domain.usecase.launch.SetUserUseCase
import kotlinx.coroutines.launch

class LaunchViewModel(
    private val setUserUseCase: SetUserUseCase,
) : ViewModel() {

    fun setUser() = viewModelScope.launch {
        val userSession = UserSession.getCurrentUserSession()
        val user = User(
            id = userSession.id,
            name = userSession.name,
            emoji = userSession.emoji,
        )
        setUserUseCase(user).collect {
        }
    }
}
