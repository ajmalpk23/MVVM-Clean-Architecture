package com.example.test.di

import com.example.test.repository.PostRepository
import com.example.test.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }

    @Provides
    fun providePostRepository(): PostRepository {
        return PostRepository()
    }
}