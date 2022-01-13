package com.task.openlibrarydocs.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.task.openlibrarydocs.BuildConfig
import com.task.openlibrarydocs.data.api.OpenLibraryRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideInterceptor(): Interceptor {
        return Interceptor {chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            // adding Authorization access token and content type as these headers are common in all api calls too
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            println(chain.request().url)
            return@Interceptor chain.proceed(request)
        }
    }



    @Singleton
    @Provides
    fun provideOkHTTPClient(interceptor: Interceptor): OkHttpClient{
        // prepare okHttp client with request interceptor and connection time out
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            // this read time added as the open library api
            // sometimes take a long time to get response (to prevent socketTimeOut)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideDocsService(retrofit: Retrofit.Builder): OpenLibraryRetrofit {
        return retrofit
            .build()
            .create(OpenLibraryRetrofit::class.java)
    }


}