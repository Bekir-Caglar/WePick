package com.bekircaglar.wepick.domain.usecase.launch

import com.bekircaglar.wepick.domain.repository.LaunchRepository

class GetUsersByIdListUseCase(
    private val launchRepository: LaunchRepository,
) {
    suspend operator fun invoke(idList: List<String>) = launchRepository.getUsersByIdList(idList)
}