package com.swalif.sa.core.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATA_STORE_NAME = "settings"
private const val CURRENT_USER_UID_PREFERENCES_NAME = "current_user_uid"

private val isFirstTimePreferenceKey = stringPreferencesKey(CURRENT_USER_UID_PREFERENCES_NAME)

val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)


suspend fun DataStore<Preferences>.updateIsFirstTime(userUid: String) {
    edit {
        it[isFirstTimePreferenceKey] = userUid
    }
}

fun DataStore<Preferences>.getUserUidFlow(): Flow<String> {
    return data.map { it[isFirstTimePreferenceKey] ?: "" }
}