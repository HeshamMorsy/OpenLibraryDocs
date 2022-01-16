package com.task.openlibrarydocs.di

import com.task.openlibrarydocs.data.api.OpenLibraryRetrofit
import com.task.openlibrarydocs.data.model.NetworkMapper
import com.task.openlibrarydocs.repository.MainRepo
import com.task.openlibrarydocs.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This component is for providing the main repository
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        retrofit: OpenLibraryRetrofit,
        networkMapper: NetworkMapper
    ): MainRepo {
        return MainRepository(retrofit, networkMapper)
    }
}