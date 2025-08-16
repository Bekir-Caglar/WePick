package com.bekircaglar.wepick.presentation.screens.createroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.wepick.data.UserSession
import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.domain.usecase.join.JoinRoomUseCase
import com.bekircaglar.wepick.domain.usecase.launch.GetUsersByIdListUseCase
import com.bekircaglar.wepick.domain.usecase.room.CheckUserInRoomUseCase
import com.bekircaglar.wepick.domain.usecase.room.ExitRoomUseCase
import com.bekircaglar.wepick.domain.usecase.room.GetRoomUseCase
import com.bekircaglar.wepick.domain.usecase.room.ObserveRoomMemersUseCase
import com.bekircaglar.wepick.utils.QueryState
import com.bekircaglar.wepick.utils.data
import com.bekircaglar.wepick.utils.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateRoomViewModel(
    private val getRoomUseCase: GetRoomUseCase,
    private val exitRoomUseCase: ExitRoomUseCase,
    private val getUsersByIdListUseCase: GetUsersByIdListUseCase,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val observeRoomMemersUseCase: ObserveRoomMemersUseCase,
    private val checkUserInRoomUseCase: CheckUserInRoomUseCase
) : ViewModel() {

    private val _room = MutableStateFlow<QueryState<RoomModel>?>(null)
    val room = _room.asStateFlow()

    private val _roomUsers = MutableStateFlow<List<User>>(emptyList())
    val roomUsers = _roomUsers.asStateFlow()

    private val _isUserExit = MutableStateFlow<Boolean>(false)
    val isUserExit = _isUserExit.asStateFlow()


    fun startObservingRoomMembers(roomId: String) = viewModelScope.launch {
        observeRoomMemersUseCase(roomId = roomId).collect {
        }
    }

    fun getRoom(roomCode: String) = viewModelScope.launch {
        getRoomUseCase(roomCode = roomCode).collect { response ->
            _room.update {
                response
            }
            if (response.isSuccess) {
                response.data?.id?.let { startObservingRoomMembers(roomId = it) }
                UserSession.getId()?.let {
                    checkUserInRoom(
                        roomCode = roomCode,
                        userId = it
                    )
                }
                getUserListByUserId()
            }
        }
    }

    fun checkUserInRoom(roomCode: String, userId: String) = viewModelScope.launch {
        checkUserInRoomUseCase(
            roomCode = roomCode,
            userId = userId
        ).collect { response ->
            when (response) {
                is QueryState.Loading -> {
                    // Handle loading state if needed
                }

                is QueryState.Success -> {
                    _isUserExit.update { !response.data }
                }

                is QueryState.Error -> {
                    _isUserExit.update { false }
                }

                is QueryState.Idle -> {
                    _isUserExit.update { false }
                }
            }
        }
    }

    fun joinRoom(roomCode: String, userId: String) = viewModelScope.launch {
        joinRoomUseCase(
            roomCode = roomCode,
            userId = userId
        ).collect { response ->
            when (response) {
                is QueryState.Loading -> {
                    // Handle loading state if needed
                }

                is QueryState.Success -> {
                    getUserListByUserId()
                }

                is QueryState.Error -> {
                    // Handle error state if needed
                }

                is QueryState.Idle -> {
                    // Handle idle state if needed
                }
            }
        }
    }

    fun getUserListByUserId() = viewModelScope.launch {
        _room.value?.data?.members?.let { members ->
            getUsersByIdListUseCase(idList = members).collect { response ->
                when (response) {
                    is QueryState.Loading -> {
                        // Handle loading state if needed
                    }

                    is QueryState.Success -> {
                        _roomUsers.update {
                            response.data
                        }
                    }

                    is QueryState.Error -> {
                        // Handle error state if needed
                    }

                    is QueryState.Idle -> {
                        // Handle idle state if needed
                    }
                }
            }
        }
    }

    fun exitRoom(roomId: String, userId: String) = viewModelScope.launch {
        exitRoomUseCase(roomId = roomId, userId = userId).collect { response ->
            when (response) {
                is QueryState.Loading -> {
                    _isUserExit.update { false }
                }

                is QueryState.Success -> {
                    _isUserExit.update { response.data }
                }

                is QueryState.Error -> {
                    _isUserExit.update { false }
                }

                is QueryState.Idle -> {
                    _isUserExit.update { false }
                }
            }
        }
    }

}