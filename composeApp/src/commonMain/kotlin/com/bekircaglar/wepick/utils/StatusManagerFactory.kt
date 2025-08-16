package com.bekircaglar.wepick.utils

import com.bekircaglar.wepick.data.repository.FirebaseStatusRepository
import org.koin.core.component.KoinComponent

expect fun createStatusManagerFactory(): StatusManagerFactory

expect class StatusManagerFactory {
    fun create(): UserStatusManager
}