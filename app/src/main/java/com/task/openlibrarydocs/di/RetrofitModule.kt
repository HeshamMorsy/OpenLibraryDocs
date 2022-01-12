package com.task.openlibrarydocs.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.task.openlibrarydocs.BuildConfig
import com.task.openlibrarydocs.data.api.OpenLibraryRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideDocsService(retrofit: Retrofit.Builder): OpenLibraryRetrofit {
        return retrofit
            .build()
            .create(OpenLibraryRetrofit::class.java)
    }


}