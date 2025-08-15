package com.bekircaglar.wepick.data.repository

import com.bekircaglar.wepick.data.UserSession
import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.repository.CategoryRepository
import com.bekircaglar.wepick.utils.QueryState
import com.bekircaglar.wepick.utils.Response
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CategoryRepositoryImp(
    private val databaseReference: DatabaseReference
) : CategoryRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            deleteClosedRooms()
        }
    }

    override suspend fun getCategories(): Flow<QueryState<List<CategoryModel>>> = flow {
        emit(QueryState.Loading)
        try {
            databaseReference.child("categories").valueEvents.collect {
                val categories = it.children.mapNotNull { snapshot ->
                    snapshot.value<CategoryModel?>()
                }
                if (categories.isNotEmpty()) {
                    emit(QueryState.Success(categories))
                } else {
                    emit(QueryState.Error("No categories found"))
                }
            }
        } catch (e: Exception) {
            emit(QueryState.Error(e.message ?: "An error occurred"))
        }
    }

    override suspend fun createRoom(categoryId: String): Flow<QueryState<String>> = flow {
        emit(QueryState.Loading)
        val roomCode = (1..5).map { ('A'..'Z').random() }.joinToString("")

        try {
            val roomId = databaseReference.child("rooms").push().key
                ?: throw Exception("Failed to create room")

            val userSession = UserSession.getCurrentUserSession()
            val roomData = RoomModel(
                id = roomId,
                name = "${userSession.nickname}'s Room ",
                roomCategory = categoryId,
                ownerId = userSession.id,
                members = listOf(userSession.id ?: ""),
                roomCode = roomCode,
            )
            databaseReference.child("rooms").child(roomId).setValue(roomData)
            emit(QueryState.Success(roomCode))
        } catch (e: Exception) {
            emit(QueryState.Error(e.message ?: "Unknown error"))
        }

    }
    private suspend fun deleteClosedRooms() {
        try {
            val roomsRef = databaseReference.child("rooms")
            roomsRef.valueEvents.collect { dataSnapshot ->
                dataSnapshot.children.forEach { snapshot ->
                    val room = snapshot.value<RoomModel>()
                    if (room.members.isEmpty()) {
                        room.id?.let { databaseReference.child("rooms").child(it) }?.removeValue()
                    }
                }
            }
        } catch (e: Exception) {
            print("Error deleting closed rooms: ${e.message}")
        }
    }

}