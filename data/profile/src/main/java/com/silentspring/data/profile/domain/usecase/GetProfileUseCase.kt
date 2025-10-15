package com.silentspring.data.profile.domain.usecase

import com.silentspring.data.profile.domain.ProfileRepository
import com.silentspring.data.profile.domain.model.ProfileBusiness
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): ProfileBusiness {
        return profileRepository.getProfile()
    }
}