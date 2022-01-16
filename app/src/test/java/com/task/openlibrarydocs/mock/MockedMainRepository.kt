package com.task.openlibrarydocs.mock

import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.repository.MainRepo
import com.task.openlibrarydocs.utils.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

open class MockedMainRepository : MainRepo {
    val dummyResponse = listOf(
        Document(title = "Doc1"),
        Document(title = "Doc2"),
        Document(title = "Doc3"),
        Document(title = "Doc4"),
    )

    private val mockedSuccessFlow = flow {
        emit(DataState.Loading)
        delay(10)
        emit(DataState.Success(dummyResponse))
    }

    private val mockedEmptyListFlow = flow {
        emit(DataState.Loading)
        delay(10)
        emit(DataState.Success(listOf<Document>()))
    }

    private val mockedErrorFlow = flow {
        emit(DataState.Loading)
        delay(10)
        emit(DataState.Error(Exception("Unknown error")))
    }


    lateinit var currentMockedResponse: MockResponseStatus

    override suspend fun getDocuments(queryMap: HashMap<String, String>): Flow<DataState<List<Document>>> =
        when (currentMockedResponse) {
            is MockResponseStatus.Success -> mockedSuccessFlow
            is MockResponseStatus.EMPTY -> mockedEmptyListFlow
            is MockResponseStatus.Error -> mockedErrorFlow
        }

}

sealed class MockResponseStatus {
    object Success : MockResponseStatus()
    object Error : MockResponseStatus()
    object EMPTY : MockResponseStatus()
}