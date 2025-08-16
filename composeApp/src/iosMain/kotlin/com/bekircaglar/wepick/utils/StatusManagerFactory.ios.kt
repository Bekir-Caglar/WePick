package com.bekircaglar.wepick.utils

import com.bekircaglar.wepick.IOSUserStatusManager
import com.bekircaglar.wepick.data.repository.FirebaseStatusRepository
import org.koin.core.component.KoinComponent
import org.koin.mp.KoinPlatform.getKoin


actual fun createStatusManagerFactory(): StatusManagerFactory {
    return StatusManagerFactory(getKoin().get())
}

actual class StatusManagerFactory(private val repository: FirebaseStatusRepository) {
    actual fun create(): UserStatusManager {
        return IOSUserStatusManager(repository)
    }
}