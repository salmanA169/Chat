package com.swalif.sa.features.main.explore.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import logcat.logcat

class SearchViewModel:ViewModel() {

    private val _searchState = MutableStateFlow<SearchStateUI>(SearchStateUI())
    val searchState = _searchState.asStateFlow()




}