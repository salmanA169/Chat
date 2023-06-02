package com.swalif.sa.features.main.explore

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(

):ViewModel() {

    private val _state = MutableStateFlow(ExploreState())
    val state = _state.asStateFlow()
}
