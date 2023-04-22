package com.swalif.sa.core.searchManager

import com.swalif.sa.features.main.explore.search.SearchStateResult

interface SearchManager<T> {
     var onSearchEventListener: SearchEvent<T>?
     fun addSearchEventListener(searchEventListener:SearchEvent<T>){
         onSearchEventListener = searchEventListener
     }
     fun registerSearchEvent()
     fun unregisterSearchEvent()
     fun reload()
     fun updateSearchState(searchStateResult:SearchStateEvent)
}