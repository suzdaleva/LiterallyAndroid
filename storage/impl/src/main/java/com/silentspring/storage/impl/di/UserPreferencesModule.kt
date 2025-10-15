package com.silentspring.storage.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import com.silentspring.storage.api.UserPreferencesRepository
import com.silentspring.storage.api.model.UserPreferences
import com.silentspring.storage.impl.UserPreferencesRepositoryImpl
import com.silentspring.storage.impl.encryption.UserPreferencesSerializer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesModule {

    @Singleton
    @Binds
    internal abstract fun provideUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    companion object {

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<UserPreferences> =
            MultiProcessDataStoreFactory.create(
                serializer = UserPreferencesSerializer,
                produceFile = {
                    File("${context.cacheDir.path}/literally.user_preferences")
                }
            )

    }
}