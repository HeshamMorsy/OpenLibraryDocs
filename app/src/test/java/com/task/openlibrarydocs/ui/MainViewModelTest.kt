package com.task.openlibrarydocs.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.mock.MockResponseStatus
import com.task.openlibrarydocs.mock.MockedMainRepository
import com.task.openlibrarydocs.repository.MainRepo
import com.task.openlibrarydocs.ui.activities.MainViewModel
import com.task.openlibrarydocs.utils.DataState
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {
    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    private lateinit var repository: MainRepo

    @MockK
    lateinit var dataState: Observer<DataState<List<Document>>>

    private lateinit var viewModel: MainViewModel

    private val dummyException = "Unknown error"

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = MockedMainRepository()
        viewModel = MainViewModel(repository)
        viewModel.dataState.observeForever(dataState)
    }

    @Test
    fun `load docs change view state to Success if there is docs`() = runBlocking {
        (repository as MockedMainRepository).currentMockedResponse = MockResponseStatus.Success
        val result = repository.getDocuments(hashMapOf())
        result.collect {
            if (it is DataState.Success) {
                assertThat(it).isEqualTo(DataState.Success((repository as MockedMainRepository).dummyResponse))
            } else {
                assertThat(it).isEqualTo(DataState.Loading)
            }
        }
    }

    @Test
    fun `load docs change view state to EMPTY if there is no docs`() = runBlocking {
        (repository as MockedMainRepository).currentMockedResponse = MockResponseStatus.EMPTY
        val result = repository.getDocuments(hashMapOf())
        result.collect {
            if (it is DataState.Success) {
                assertThat(it.data.size).isEqualTo(0)
            } else {
                assertThat(it).isEqualTo(DataState.Loading)
            }
        }
    }

    @Test
    fun `load docs change view state to ERROR if repository throws an error`() = runBlocking {
        (repository as MockedMainRepository).currentMockedResponse = MockResponseStatus.Error
        val result = repository.getDocuments(hashMapOf())
        result.collect {
            if (it is DataState.Error) {
                assertThat(it.exception.message).isEqualTo(dummyException)
            } else {
                assertThat(it).isEqualTo(DataState.Loading)
            }
        }
    }
}