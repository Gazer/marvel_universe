package ar.com.p39.marvel_universe.common

sealed class Result<T> {
    data class Success<T>(val data: T): Result<T>()

    data class Error<T>(val error: String): Result<T>()
}
