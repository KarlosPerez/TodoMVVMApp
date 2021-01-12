package com.karlosprojects.todomvvmapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.karlosprojects.todomvvmapp.data.PreferencesManager
import com.karlosprojects.todomvvmapp.data.SortOrder
import com.karlosprojects.todomvvmapp.data.TaskDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(
    taskDao: TaskDao,
    private val preferences: PreferencesManager
) : ViewModel() {

    /**
     * This is like MutableLiveData, it can hold a single value, not like a normal flow, but we can use it
     * as a flow. We pass a empty string because we don't want to filter anything immediately;
     * on the other variables, we pass an initial value
     */
    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferences.preferencesFlow

    /**
     * flatMapLatest is a flow operator, which means that whenever the value of flow 'it' changes,
     * execute the code block, and the parameter will be passed as the current value of searchQuery.
     * This will return a flow and then assign the result to the taskFlow val. Finally this val will
     * be translated to be observed as livedata
     *
     * Update: now we have a multiple livedata that holds multiple filters which can be changed anytime in the UI.
     * we use combine... to combine all of them in a single flow, which will emit a Triple value whenever of
     * these three values changes, no matter which one changes, we always get the latest value of all three of them.
     * With that latest value, we use the flatMapLatest to execute the query in the DAO
     *
     * Update 2: now we used DataStore to persist the value of sort by, and the check of hide completed, so in this
     * case we will emit a Pair value, but the logic remains the same
     */
    private val taskFlow = combine(
        searchQuery,
        preferencesFlow
    ) { searchQuery, filterPreferences ->
        Pair(searchQuery, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferences.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferences.updateHideCompleted(hideCompleted)
    }

    val taskList = taskFlow.asLiveData()
}