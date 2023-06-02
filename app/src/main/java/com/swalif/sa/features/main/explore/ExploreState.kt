package com.swalif.sa.features.main.explore

import com.swalif.sa.model.UserInfo

data class ExploreState(
    val users:List<UserInfo> = emptyList()
)
