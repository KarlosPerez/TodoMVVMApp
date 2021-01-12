package com.karlosprojects.todomvvmapp.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_PREFERENCES_DT_NAME : String = "user_preferences"

/**
 * This class is created as an abstraction layer, and will be helpful to save code in the viewModel,
 * so it'll keep it readable, and the viewModel already will only have data ready for it
 */
@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore(USER_PREFERENCES_DT_NAME)

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )
            val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
            FilterPreferences(sortOrder, hideCompleted)

        }

    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) =
        dataStore.edit { preferences -> preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name }

    suspend fun updateHideCompleted(hideCompleted: Boolean) =
        dataStore.edit { preferences -> preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted }


}

enum class SortOrder {
    BY_NAME, BY_DATE
}

data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted : Boolean)