package com.swalif.sa.features.main.explore.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.component.Gender
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repository.searchRepository.SearchRepository
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchStateUI>(SearchStateUI())
    val searchState = _searchState.asStateFlow()

    init {
        addCloseable(searchRepository.getClosable())
        viewModelScope.launch {
           val getUser = userRepository.getCurrentUser()

            searchRepository.searchUser(getUser!!).collect{roomEvent->
                _searchState.update {
                    it.copy(
                        roomEvent,
                        getUser
                    )
                }
            }
        }
    }
}