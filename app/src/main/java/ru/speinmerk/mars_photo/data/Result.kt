package ru.speinmerk.mars_photo.data

sealed class Result<out T> {
    companion object {
        fun <T> success(data: T) = Success(data)

        fun <T> loading(data: T? = null) = Loading(data)

        fun <T> error(msg: String, data: T? = null) = Error(msg, data)
    }

    data class Success<out T>(
        val data: T
    ) : Result<T>()

    data class Loading<out T>(
        val data: T?
    ) : Result<T>()

    data class Error<out T>(
        val msg: String,
        val data: T?
    ) : Result<T>()
}
