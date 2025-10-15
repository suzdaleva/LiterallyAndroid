package com.silentspring.data.profile.domain

import com.silentspring.data.profile.domain.model.ProfileBusiness

interface ProfileRepository {
    suspend fun getProfile(): ProfileBusiness
}