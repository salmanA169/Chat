package com.swalif.sa.features.main.explore.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.component.Gender
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchState = MutableStateFlow<SearchStateUI>(SearchStateUI())
    val searchState = _searchState.asStateFlow()

    init {

        viewModelScope.launch {
            delay(2000)
            _searchState.update {
                it.copy(
                    userInfo = UserInfo(
                        "salman",
                        "",
                        "https://i.pinimg.com/originals/b4/c1/fb/b4c1fbf0e913bf9365c8fa0dcc48c0c0.jpg",
                        Gender.Male
                    ),searchStateResult = SearchStateResult.FOUND_USER
                )
            }
            delay(5000)
            _searchState.update {
                it.copy(
                    searchStateResult = SearchStateResult.USER_LEFT
                )
            }
        }
    }
}