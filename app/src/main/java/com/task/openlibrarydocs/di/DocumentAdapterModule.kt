package com.task.openlibrarydocs.di

import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.ui.adapter.DocumentsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.channels.Channel

/**
 * This component is for providing the document adapter and it's stuff
 */
@Module
@InstallIn(ActivityComponent::class)
object DocumentAdapterModule {

    @Provides
    @ActivityScoped
    fun provideDocumentList(): ArrayList<Document>{
        return ArrayList()
    }

    @Provides
    @ActivityScoped
    fun provideActionChannel(): Channel<Document> {
        return Channel()
    }

    @Provides
    @ActivityScoped
    fun provideDocumentAdapter(
        arrayList: ArrayList<Document>,
        actionChannel: Channel<Document>
    ): DocumentsAdapter{
        return DocumentsAdapter(arrayList, actionChannel)
    }
}