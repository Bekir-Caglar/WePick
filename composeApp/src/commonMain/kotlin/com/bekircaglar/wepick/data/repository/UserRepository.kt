package com.bekircaglar.wepick.data.repository

// Örnek veri modeli

data class User(val id: String, val name: String)

// Örnek repository
class UserRepository {
    fun getUser(): User = User("1", "Bekir")
}

