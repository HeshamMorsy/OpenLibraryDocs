package com.task.openlibrarydocs.ui.activities

import androidx.lifecycle.*
import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.repository.MainRepository
import com.task.openlibrarydocs.utils.DataState
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel(){

    private val _dataState: MutableLiveData<DataState<List<Document>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Document>>>
        get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent){
        viewModelScope.launch {
            when(mainStateEvent){
                is MainStateEvent.GetDocumentEvent -> {
                    mainRepository.getDocuments(hashMapOf())
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

}

sealed class MainStateEvent{
    object GetDocumentEvent: MainStateEvent()
}