package com.task.openlibrarydocs.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.repository.MainRepository
import com.task.openlibrarydocs.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 100
const val DEFAULT_QUERY_KEY = "q"
const val TITLE_QUERY_KEY = "title"
const val AUTHOR_QUERY_KEY = "author"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    // live data for updating ui with the new list in responses
    private val _dataState: MutableLiveData<DataState<List<Document>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Document>>>
        get() = _dataState

    // handling pagination properties
    var page = 1
    private var documentScrollPosition = 0
    private var queryKey = ""
    private var queryValue = ""

    /**
     * function to take actions from UI to update the state
     */
    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetDocumentEvent -> {
                    if (queryValue.isNotEmpty()) {
                        mainRepository.getDocuments(
                            hashMapOf(
                                "page" to page.toString(),
                                queryKey to queryValue
                            )
                        )
                            .onEach { dataState ->
                                _dataState.value = dataState
                            }
                            .launchIn(viewModelScope)
                    }else{
                        _dataState.value = DataState.CLEAR
                    }
                }
                is MainStateEvent.RestartPaginationEvent ->{
                    restartPagination()
                }
                is MainStateEvent.NextPageEvent ->{
                    getNextPage()
                }

            }
        }
    }

    /**
     * function to restart pagination properties and get new values depending on query value
     */
    private fun restartPagination() {
        page = 1
        onChangeDocumentScrollPosition(0)
        _dataState.value = DataState.CLEAR
        setStateEvent(MainStateEvent.GetDocumentEvent)
    }

    private fun getNextPage() {
        // prevent duplicate events due to recompose happening too quickly
            if ((documentScrollPosition + 1) >= (page * PAGE_SIZE)) {
//                _dataState.value = DataState.Loading
                incrementPage()
                if (page > 1) {
                    // get the next page from API
                    setStateEvent(MainStateEvent.GetDocumentEvent)
                }

            }
    }

    private fun incrementPage() {
        page++
    }

    /**
     * function to track scroll position to manage pagination
     */
    fun onChangeDocumentScrollPosition(position: Int) {
        documentScrollPosition = position
    }

    /**
     * function to update the query value depending on the edit text input field
     */
    fun updateQueryValue(query: String){
        queryValue = query
    }

    /**
     * function to update the query key
     */
    fun updateQueryKey(key: String){
        queryKey = key
    }



}

sealed class MainStateEvent {
    object GetDocumentEvent : MainStateEvent()
    object RestartPaginationEvent : MainStateEvent()
    object NextPageEvent : MainStateEvent()
}