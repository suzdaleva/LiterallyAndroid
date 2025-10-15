package com.silentspring.data.authorization.di


import com.silentspring.core.network.utils.TokenAuthenticator
import com.silentspring.core.network.utils.UnauthInterceptorOkHttpClient
import com.silentspring.data.authorization.data.AuthorizationApi
import com.silentspring.data.authorization.data.AuthorizationRepositoryImpl
import com.silentspring.data.authorization.domain.AuthorizationRepository
import com.silentspring.data.authorization.utils.TokenValidationAuthenticator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AuthorizationModule {


    @Singleton
    @Binds
    internal abstract fun bindAuthorizationRepository(
        authorizationRepository: AuthorizationRepositoryImpl
    ): AuthorizationRepository

    @Singleton
    @Binds
    @TokenAuthenticator
    internal abstract fun bindAuthenticator(
        authenticator: TokenValidationAuthenticator
    ): Authenticator

    companion object {

        @Singleton
        @Provides
        internal fun provideAuthorizationApi(@UnauthInterceptorOkHttpClient retrofit: Retrofit): AuthorizationApi =
            retrofit.create(AuthorizationApi::class.java)

    }
}