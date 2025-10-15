package com.silentspring.data.profile.data

import com.silentspring.data.profile.data.model.ProfileResponse
import retrofit2.http.GET

internal interface ProfileApi {
    @GET("/profile")
    suspend fun getProfile(): ProfileResponse
}