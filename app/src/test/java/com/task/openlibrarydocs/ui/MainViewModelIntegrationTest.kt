package com.task.openlibrarydocs.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.task.openlibrarydocs.data.api.OpenLibraryRetrofit
import com.task.openlibrarydocs.data.model.NetworkMapper
import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.data.model.response.ApiResponse
import com.task.openlibrarydocs.repository.MainRepo
import com.task.openlibrarydocs.repository.MainRepository
import com.task.openlibrarydocs.ui.activities.MainViewModel
import com.task.openlibrarydocs.utils.DataState
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class MainViewModelIntegrationTest {
    @get:Rule
    val mockWebServer = MockWebServer()

    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var stateObserver: Observer<DataState<List<Document>>>

    private lateinit var viewModel: MainViewModel
    lateinit var repository: MainRepo
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val dummyResponseJson = ClassLoader.getSystemResource("docs.json").readText()

    private val openLibraryService by lazy {
        retrofit.create(OpenLibraryRetrofit::class.java)
    }

    private val networkMapper by lazy {
        NetworkMapper()
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockWebServer.enqueue(
            MockResponse()
                .setBody(dummyResponseJson)
                .setResponseCode(200)
        )
        repository = MainRepository(openLibraryService, networkMapper)
        viewModel = MainViewModel(repository)
        viewModel.dataState.observeForever(stateObserver)
    }

    @Test
    fun `get docs change view state to Success if there is docs`() = runBlocking {
        val result = repository.getDocuments(hashMapOf())
        var finalDataState: DataState<List<Document>>? = null
        result.collect {
            finalDataState = it
        }
        delay(100)
        val response = Gson().fromJson(dummyResponseJson, ApiResponse::class.java)
        assertThat(finalDataState).isEqualTo(DataState.Success(NetworkMapper().mapFromEntityList(response.docs)))
    }

}
