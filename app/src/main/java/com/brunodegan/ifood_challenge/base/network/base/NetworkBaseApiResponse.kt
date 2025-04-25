package com.brunodegan.ifood_challenge.base.network.base

sealed class ErrorType(val message: String?) {
    data class Generic(val customMessage: String?) : ErrorType(customMessage)
}

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: ErrorType) : Resource<T>()
}