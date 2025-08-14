package com.bekircaglar.wepick.utils

sealed class QueryState<out T> {
    data object Idle : QueryState<Nothing>()
    data object Loading: QueryState<Nothing>()
    data class Success<T>(val data: T) : QueryState<T>()
    data class Error(val message: String? = null) : QueryState<Nothing>()

}

// TODO Add no internet connection state
// TODO Add progress state

inline val <T> QueryState<T>.isLoading: Boolean
    get() = this is QueryState.Loading

val <T> QueryState<T>.data: T?
    get() = (this as? QueryState.Success<T>)?.data

val <T> QueryState<T>.error: String?
    get() = (this as? QueryState.Error)?.message

val <T> QueryState<T>.isSuccess: Boolean
    get() = this is QueryState.Success

val <T> QueryState<T>.isError: Boolean
    get() = this is QueryState.Error

val <T> QueryState<T>.isIdle: Boolean
    get() = this is QueryState.Idle