package ru.speinmerk.mars_photo.data.source.network

import com.google.gson.Gson
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import ru.speinmerk.mars_photo.BuildConfig


class PhotoApiService {
    private val client = OkHttpClient()

    private fun createUrl(apiKey: String, sol: Int, page: Int): String {
        val urlBuilder = HttpUrl.parse(BuildConfig.DOMAIN).newBuilder()
        urlBuilder.addQueryParameter("api_key", apiKey)
        urlBuilder.addQueryParameter("sol", sol.toString())
        urlBuilder.addQueryParameter("page", page.toString())
        return urlBuilder.build().toString()
    }

    fun loadPhotos(apiKey: String, sol: Int, page: Int): ApiResponse<PhotosResponse> {
        val url = createUrl(apiKey, sol, page)
        val request = Request.Builder().url(url).build()
        return try {
            val response = client.newCall(request).execute()
            ApiResponse.create(response) {
                Gson().fromJson(it.string(), PhotosResponse::class.java)
            }
        } catch (error: Throwable) {
            ApiResponse.create(error)
        }
    }

}