package com.bekircaglar.wepick.data.repository

import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.repository.RoomRepository
import com.bekircaglar.wepick.utils.QueryState
import com.bekircaglar.wepick.utils.UserStatus
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
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
                val activeObservations = mutableMapOf<String, Job>()

                roomRef.child("members").valueEvents.collect { membersSnapshot ->
                    if (membersSnapshot.exists) {
                        val currentMemberIds =
                            membersSnapshot.children.map { it.value<String>() }.toSet()

                        activeObservations.keys.filterNot { it in currentMemberIds }
                            .forEach { userId ->
                                activeObservations[userId]?.cancel()
                                activeObservations.remove(userId)
                            }

                        currentMemberIds.filterNot { it in activeObservations.keys }
                            .forEach { userId ->
                                val observerJob = CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        databaseReference.child("users").child(userId)
                                            .child("status")
                                            .valueEvents.collect { statusSnapshot ->
                                                val status = statusSnapshot.value<String>()

                                                if (status != UserStatus.ONLINE.name) {
                                                    activeObservations[userId]?.cancel()
                                                    activeObservations.remove(userId)

                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        try {
                                                            removeOfflineMemberFromRoom(
                                                                roomId,
                                                                userId
                                                            )
                                                        } catch (e: Exception) {
                                                            println("Error removing offline member: ${e.message}")
                                                        }
                                                    }
                                                }
                                            }
                                    } catch (e: Exception) {
                                        println("Error observing user $userId status: ${e.message}")
                                    }
                                }

                                activeObservations[userId] = observerJob
                            }

                        emit(QueryState.Success(Unit))
                    } else {
                        activeObservations.values.forEach { it.cancel() }
                        activeObservations.clear()
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

    override suspend fun setUserReadyStatus(
        roomId: String,
        userId: String,
        isReady: Boolean
    ): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)

        try {
            val roomRef = databaseReference.child("rooms").child(roomId)
            val readyMembersRef = roomRef.child("readyMembers")

            val currentReadyMembers = readyMembersRef.valueEvents.first().children
                .map { it.value<String>() }.toMutableSet()

            if (isReady) {
                currentReadyMembers.add(userId)
            } else {
                currentReadyMembers.remove(userId)
            }

            readyMembersRef.setValue(currentReadyMembers)
            emit(QueryState.Success(Unit))

        } catch (e: Exception) {
            emit(QueryState.Error(e.message))
        }
    }

    private suspend fun removeOfflineMemberFromRoom(roomId: String, userId: String) {
        try {
            val roomRef = databaseReference.child("rooms").child(roomId)
            val membersRef = roomRef.child("members")
            val ownerRef = roomRef.child("ownerId")
            val readyMembersRef = roomRef.child("readyMembers")

            val currentMembers = membersRef.valueEvents.first()
            val updatedMembers = currentMembers.children
                .map { it.value<String>() }
                .filter { it != userId }

            val currentReadyMembers = readyMembersRef.valueEvents.first().children
                .map { it.value<String>() }
                .filter { it != userId }
                .toMutableSet()

            val currentOwnerId = roomRef.valueEvents.first().child("ownerId").value<String>()

            if (updatedMembers.isEmpty()) {
                databaseReference.child("rooms").child(roomId).removeValue()
            } else {
                if (userId == currentOwnerId && updatedMembers.isNotEmpty()) {
                    val newOwnerId = updatedMembers.first()
                    ownerRef.setValue(newOwnerId)
                    currentReadyMembers.add(newOwnerId)
                }
                readyMembersRef.setValue(currentReadyMembers)
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
                val ownerRef = databaseReference.child("rooms").child(roomId).child("ownerId")
                val readyMembersRef = databaseReference.child("rooms").child(roomId).child("readyMembers")
                val participants =
                    roomRef.child("members").children.associate { it.value to it.value<String>() }.values
                val updatedMembers = participants.filter { it != userId }

                val currentReadyMembers = roomRef.child("readyMembers").children
                    .map { it.value<String>() }
                    .filter { it != userId }
                    .toMutableSet()

                val currentOwnerId = roomRef.child("ownerId").value<String>()

                if (updatedMembers.isEmpty()) {
                    databaseReference.child("rooms").child(roomId).removeValue()
                    emit(QueryState.Success(true))
                } else {
                    // Owner değişirse yeni owner'ı readyMembers'a ekle
                    if (userId == currentOwnerId && updatedMembers.isNotEmpty()) {
                        val newOwnerId = updatedMembers.first()
                        ownerRef.setValue(newOwnerId)
                        currentReadyMembers.add(newOwnerId)
                    }
                    readyMembersRef.setValue(currentReadyMembers)
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