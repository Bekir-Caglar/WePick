package com.bekircaglar.wepick.data.repository

import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.repository.RoomRepository
import com.bekircaglar.wepick.utils.QueryState
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class RoomRepositoryImp(
    private val databaseReference: DatabaseReference
) : RoomRepository {
    override suspend fun getRoom(roomCode: String): Flow<QueryState<RoomModel>> = flow {
        emit(QueryState.Loading)
        val databaseQuery = databaseReference.child("rooms")

        try {
            databaseQuery.valueEvents.collect {
                val room = it.children.map { it.value<RoomModel>() }.first { room ->
                    room.roomCode == roomCode
                }
                emit(QueryState.Success(room))
            }

        } catch (e: Exception) {
            emit(QueryState.Error(e.message))
        }

    }

    override suspend fun exitRoom(
        roomId: String,
        userId: String
    ): Flow<QueryState<Boolean>> = flow {
        emit(QueryState.Loading)
       try {
           val roomRef = databaseReference.child("rooms").child(roomId).valueEvents.first()

           if (roomRef.exists) {
               val membersRef = databaseReference.child("rooms").child(roomId).child("members")
               val participants =
                   roomRef.child("members").children.associate { it.value to it.value<String>() }.values
               val updatedMembers = participants.filter { it != userId }

               if (updatedMembers.isEmpty()) {
                   databaseReference.child("rooms").child(roomId).removeValue()
                   emit(QueryState.Success(true))
               } else {
                   membersRef.setValue(updatedMembers)
                   emit(QueryState.Success(true))
               }
           } else {
               emit(QueryState.Error("Room not found"))
           }

       } catch (e: Exception) {
           println("Exception: ${e.message}")
           emit(QueryState.Error(e.message))
       }
    }
}