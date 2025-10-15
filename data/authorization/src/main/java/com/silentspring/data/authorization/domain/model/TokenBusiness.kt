package com.silentspring.data.authorization.domain.model

data class TokenBusiness(
    val accessToken: String,
    val refreshToken: String
)