package com.silentspring.data.books.di

import com.silentspring.data.books.domain.BooksRepository
import com.silentspring.data.books.data.BooksRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class BooksModule {

    @Binds
    internal abstract fun bindAnalyticsService(
        booksRepository: BooksRepositoryImpl
    ): BooksRepository

}