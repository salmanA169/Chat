package com.swalif.sa.features.main.home.message.previewImage

import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.swalif.sa.PREVIEW_IMAGE_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
):ViewModel() {
    val imageUri = savedStateHandle.get<String>(PREVIEW_IMAGE_ARG)!!
    private val _previewState = MutableStateFlow<String>(imageUri)
    val previewState = _previewState.asStateFlow()

    init {

    }
}