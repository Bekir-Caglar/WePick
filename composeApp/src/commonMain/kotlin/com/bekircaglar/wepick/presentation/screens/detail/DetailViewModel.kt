package com.bekircaglar.wepick.presentation.screens.detail

import com.bekircaglar.wepick.domain.usecase.GetUserUseCase
import com.bekircaglar.wepick.data.repository.User
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class DetailViewModel(private val getUserUseCase: GetUserUseCase) : ViewModel() {
    private val _user = mutableStateOf(getUserUseCase.execute())
    val user: State<User> = _user
}
