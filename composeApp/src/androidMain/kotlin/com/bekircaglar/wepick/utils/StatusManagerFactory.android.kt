package com.bekircaglar.wepick.utils
import android.app.Application
import com.bekircaglar.wepick.AndroidUserStatusManager
import com.bekircaglar.wepick.data.repository.FirebaseStatusRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext
import org.koin.java.KoinJavaComponent.inject


actual fun createStatusManagerFactory(): StatusManagerFactory {
    return StatusManagerFactory(GlobalContext.get().get())
}

actual class StatusManagerFactory(private val repository: FirebaseStatusRepository) {
    actual fun create(): UserStatusManager = AndroidUserStatusManager(repository)
}