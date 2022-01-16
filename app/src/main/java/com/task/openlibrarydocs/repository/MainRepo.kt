package com.task.openlibrarydocs.repository

import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.utils.DataState
import kotlinx.coroutines.flow.Flow

interface MainRepo {

    suspend fun getDocuments(queryMap: HashMap<String, String>): Flow<DataState<List<Document>>>

}