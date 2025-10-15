package com.silentspring.data.profile.data.model

import com.silentspring.data.profile.domain.model.ProfileBusiness

internal data class ProfileResponse(
    val name: String
)

internal fun ProfileResponse.toBusiness(): ProfileBusiness {
    return ProfileBusiness(name = name)
}
