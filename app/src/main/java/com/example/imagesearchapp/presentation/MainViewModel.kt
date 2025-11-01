package com.example.imagesearchapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.imagesearchapp.model.usecase.GetImagesFromRemoteMediator
import com.example.imagesearchapp.model.usecase.GetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: GetImagesUseCase,
    private val remoteMediator: GetImagesFromRemoteMediator
) : ViewModel() {
    private val _query = MutableStateFlow("")

//    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
//    val images = _query.filter { it.isNotBlank() }
//        .debounce(1000)
//        .flatMapLatest { query ->
//            useCase.invoke(query).flow
//        }.cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val images = _query.filter { it.isNotBlank() }
        .debounce(1000)
        .flatMapLatest { query ->
            remoteMediator.invoke(query)
        }.cachedIn(viewModelScope)

    fun updateQuery(q: String) {
        _query.update { q }
    }
}