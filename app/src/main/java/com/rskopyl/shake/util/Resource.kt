package com.rskopyl.shake.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

sealed interface Resource<T> {
    class Loading<T> : Resource<T>
    data class Success<T>(val data: T) : Resource<T>
    class Error<T> : Resource<T>
}

inline fun <T, R> Resource<T>.ifSuccess(
    crossinline transformation: (T) -> R
): Resource<R> {
    return when(this) {
        is Resource.Loading -> Resource.Loading()
        is Resource.Success -> Resource.Success(transformation(data))
        is Resource.Error -> Resource.Error()
    }
}

fun <T> Resource<T>.dataOrNull(): T? {
    return if (this is Resource.Success) data else null
}

fun <T> MutableStateFlow<Resource<T>>.updateIfSuccess(function: (T) -> T) {
    update { value -> value.ifSuccess(function) }
}