package com.bekircaglar.wepick.data.repository

import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.repository.JoinRepository
import com.bekircaglar.wepick.utils.QueryState
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.collections.emptyMap
import kotlin.collections.first

class JoinRepositoryImp(
    private val databaseReference: DatabaseReference
) : JoinRepository {

    override suspend fun joinRoom(
        roomCode: String,
        userId: String
    ): Flow<QueryState<Boolean>> = flow {
        emit(QueryState.Loading)

        try {

            val roomRef = databaseReference.child("rooms").valueEvents.first()

            val room = roomRef.children.map { it.value<RoomModel>() }
                .firstOrNull { it.roomCode == roomCode }

            if (room != null) {
                val membersRef =
                    room.id?.let { databaseReference.child("rooms").child(it) }?.child("members")
                val participants = room.members

                val updatedMembers = if (!participants.contains(userId)) participants + userId else participants

                membersRef?.setValue(updatedMembers)
                emit(QueryState.Success(true))
            } else {
                emit(QueryState.Error("Room not found"))
            }

            /*databaseReference.child("rooms").valueEvents.collect {
                val room = it.children.map { it.value<RoomModel>() }.firstOrNull { room ->
                    room.roomCode == roomCode
                }

                    membersRef.setValue(updatedMembers)
                    emit(QueryState.Success(true))

                } else {
                    emit(QueryState.Error("Room not found"))
                }

            }*/
        } catch (e: Exception) {
            emit(QueryState.Error(e.message))
        }
    }
}