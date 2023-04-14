package com.swalif.sa.features.main.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel:ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState())
    val searchState = _searchState.asStateFlow()
}