package com.bekircaglar.wepick.domain.repository

import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.utils.QueryState
import com.bekircaglar.wepick.utils.Response
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun createRoom(
        categoryId: String,
        subCategories: List<String>
    ): Flow<QueryState<String>>


}