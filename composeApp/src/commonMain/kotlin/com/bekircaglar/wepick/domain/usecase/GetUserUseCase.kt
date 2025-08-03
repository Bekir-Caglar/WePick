package com.bekircaglar.wepick.domain.usecase

import com.bekircaglar.wepick.data.repository.User
import com.bekircaglar.wepick.data.repository.UserRepository

class GetUserUseCase(private val repository: UserRepository) {
    fun execute(): User = repository.getUser()
}