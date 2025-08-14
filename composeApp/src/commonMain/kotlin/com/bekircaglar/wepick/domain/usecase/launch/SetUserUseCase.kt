package com.bekircaglar.wepick.domain.usecase.launch

import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.domain.repository.LaunchRepository

class SetUserUseCase(
    private val launchRepository: LaunchRepository,
) {
    suspend operator fun invoke(user: User) = launchRepository.setUser(user)


}