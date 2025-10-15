package com.silentspring.data.profile.data

import com.silentspring.data.profile.data.model.toBusiness
import com.silentspring.data.profile.domain.ProfileRepository
import com.silentspring.data.profile.domain.model.ProfileBusiness
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {
    override suspend fun getProfile(): ProfileBusiness {
        return api.getProfile().toBusiness()
    }
}