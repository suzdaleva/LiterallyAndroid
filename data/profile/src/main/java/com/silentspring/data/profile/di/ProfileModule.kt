package com.silentspring.data.profile.di

import com.silentspring.core.network.utils.AuthInterceptorOkHttpClient
import com.silentspring.core.network.utils.UnauthInterceptorOkHttpClient
import com.silentspring.data.profile.data.ProfileApi
import com.silentspring.data.profile.data.ProfileRepositoryImpl
import com.silentspring.data.profile.domain.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {


    @Singleton
    @Binds
    internal abstract fun bindsProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository

    companion object {

        @Singleton
        @Provides
        internal fun provideProfileApi(@AuthInterceptorOkHttpClient retrofit: Retrofit): ProfileApi =
            retrofit.create(ProfileApi::class.java)

    }
}