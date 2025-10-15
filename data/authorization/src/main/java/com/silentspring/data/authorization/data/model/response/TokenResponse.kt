package com.silentspring.data.authorization.data.model.response

import com.silentspring.data.authorization.domain.model.TokenBusiness

internal data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)


internal fun TokenResponse.toBusiness() = TokenBusiness(
    accessToken = accessToken,
    refreshToken = refreshToken
)
