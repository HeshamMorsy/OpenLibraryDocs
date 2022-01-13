package com.task.openlibrarydocs.di

import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.ui.adapter.DocumentsAdapter
import com.task.openlibrarydocs.ui.adapter.ISBNsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.channels.Channel
import javax.inject.Qualifier

/**
 * This component is for providing the isbn adapter and it's stuff
 */
@Module
@InstallIn(ActivityComponent::class)
object ISBNAdapterModule {

    @Isbn
    @Provides
    @ActivityScoped
    fun provideDocumentList(): ArrayList<String>{
        return ArrayList()
    }

    @Provides
    @ActivityScoped
    fun provideISBNAdapter(
        @Isbn arrayList: ArrayList<String>
    ): ISBNsAdapter{
        return ISBNsAdapter(arrayList)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Isbn