package com.task.openlibrarydocs.repository

import com.task.openlibrarydocs.data.api.OpenLibraryRetrofit
import com.task.openlibrarydocs.data.model.NetworkMapper
import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class MainRepository
@Inject
constructor(
    private val retrofit: OpenLibraryRetrofit,
    private val networkMapper: NetworkMapper
): MainRepo {

    override suspend fun getDocuments(queryMap: HashMap<String, String>): Flow<DataState<List<Document>>> = flow{
        emit(DataState.Loading)
        try {
            val apiResponse = retrofit.searchWithData(queryMap)
            val documents = networkMapper.mapFromEntityList(apiResponse.docs)
            emit(DataState.Success(documents))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }

}