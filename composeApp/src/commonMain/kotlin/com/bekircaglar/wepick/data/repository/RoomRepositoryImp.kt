package com.bekircaglar.wepick.data.repository

import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.repository.RoomRepository
import com.bekircaglar.wepick.utils.QueryState
import com.bekircaglar.wepick.utils.UserStatus
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

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

    override suspend fun observeRoomMembersOnlineStatus(roomId: String): Flow<QueryState<Unit>> =
        flow {
            emit(QueryState.Loading)

            try {
                val roomRef = databaseReference.child("rooms").child(roomId)
                val observedUsers = mutableSetOf<String>()

                roomRef.child("members").valueEvents.collect { membersSnapshot ->
                    if (membersSnapshot.exists) {
                        val memberIds = membersSnapshot.children.map { it.value<String>() }

                        memberIds.forEach { userId ->
                            if (!observedUsers.contains(userId)) {
                                observedUsers.add(userId)

                                CoroutineScope(Dispatchers.IO).launch {
                                    databaseReference.child("users").child(userId).child("status")
                                        .valueEvents.collect { statusSnapshot ->
                                            val status = statusSnapshot.value<String>()

                                            if (status != UserStatus.ONLINE.name) {
                                                removeOfflineMemberFromRoom(roomId, userId)
                                            }
                                        }
                                }
                            }
                        }

                        emit(QueryState.Success(Unit))
                    }
                }

            } catch (e: Exception) {
                emit(QueryState.Error(e.message))
            }
        }

   override suspend fun checkUserInRoom(
       roomCode: String,
       userId: String
   ): Flow<QueryState<Boolean>> = flow {
       emit(QueryState.Loading)

       try {
           val roomsRef = databaseReference.child("rooms")

           roomsRef.valueEvents.collect { roomsSnapshot ->
               val room = roomsSnapshot.children.map { it.value<RoomModel>() }
                   .firstOrNull { it.roomCode == roomCode }

               if (room != null) {
                   val memberIds = roomsSnapshot.child(room.id ?: "")
                       .child("members").children.map { it.value<String>() }
                   val isUserInRoom = memberIds.contains(userId)

                   emit(QueryState.Success(isUserInRoom))
               } else {
                   emit(QueryState.Success(false))
               }
           }

       } catch (e: Exception) {
           emit(QueryState.Error(e.message))
       }
   }

    private suspend fun removeOfflineMemberFromRoom(roomId: String, userId: String) {
        try {
            val roomRef = databaseReference.child("rooms").child(roomId)
            val membersRef = roomRef.child("members")

            val currentMembers = membersRef.valueEvents.first()
            val updatedMembers = currentMembers.children
                .map { it.value<String>() }
                .filter { it != userId }

            if (updatedMembers.isEmpty()) {
                // Oda boş kaldıysa odayı sil
                databaseReference.child("rooms").child(roomId).removeValue()
            } else {
                // Güncellenmiş üye listesini kaydet
                membersRef.setValue(updatedMembers)
            }

        } catch (e: Exception) {
            println("Error removing offline member: ${e.message}")
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