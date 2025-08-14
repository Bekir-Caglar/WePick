package com.bekircaglar.wepick.presentation.screens.joinroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.wepick.domain.usecase.join.JoinRoomUseCase
import com.bekircaglar.wepick.utils.data
import com.bekircaglar.wepick.utils.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JoinViewModel(
    private val joinRoomUseCase: JoinRoomUseCase
) : ViewModel() {

    private val _isUserJoined = MutableStateFlow<Boolean?>(null)
    val isUserJoined = _isUserJoined.asStateFlow()

    fun joinRoom(
        roomCode: String,
        userId: String,
    ) = viewModelScope.launch {
        joinRoomUseCase(roomCode, userId).collect { response ->
            if (response.data == true)
                _isUserJoined.value = response.data
        }
    }
}