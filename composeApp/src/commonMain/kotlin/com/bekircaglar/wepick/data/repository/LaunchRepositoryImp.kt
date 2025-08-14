package com.bekircaglar.wepick.data.repository

import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.domain.repository.LaunchRepository
import com.bekircaglar.wepick.utils.QueryState
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class LaunchRepositoryImp(
    private val databaseReference: DatabaseReference
) : LaunchRepository {

    override suspend fun setUser(user: User): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)

        try {
            val userRef = user.id?.let { databaseReference.child("users").child(it) }
            userRef?.setValue(user)
            emit(QueryState.Success(Unit))
        } catch (e: Exception) {
            emit(QueryState.Error(e.message))
        }

        emit(QueryState.Idle)
    }

    override suspend fun getUsersByIdList(idList: List<String>): Flow<QueryState<List<User>>> =
        flow {
            emit(QueryState.Loading)

            try {
                val users = idList.map { id ->
                    val userRef = databaseReference.child("users").child(id)
                    userRef.valueEvents.first().value<User>()
                }

                emit(QueryState.Success(users))
            } catch (e: Exception) {
                emit(QueryState.Error(e.message))
            }

            emit(QueryState.Idle)
        }
}