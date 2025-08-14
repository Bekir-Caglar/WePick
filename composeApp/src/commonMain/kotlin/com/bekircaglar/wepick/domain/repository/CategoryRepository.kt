package com.bekircaglar.wepick.domain.repository

import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.utils.QueryState
import com.bekircaglar.wepick.utils.Response
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategories(): Flow<QueryState<List<CategoryModel>>>

    suspend fun createRoom(categoryId: String): Flow<QueryState<String>>


}