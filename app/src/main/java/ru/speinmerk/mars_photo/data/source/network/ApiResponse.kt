package ru.speinmerk.mars_photo.data.source.network

import com.squareup.okhttp.Response
import com.squareup.okhttp.ResponseBody

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error", error)
        }

        fun <T> create(response: Response?, parser: (ResponseBody) -> T): ApiResponse<T> {
            return if (response?.isSuccessful == true) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(parser(body))
                }
            } else {
                val errorMessage = response?.message() ?: "unknown error"
                ApiErrorResponse(errorMessage, Throwable(errorMessage))
            }
        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String, val error: Throwable) : ApiResponse<T>()
