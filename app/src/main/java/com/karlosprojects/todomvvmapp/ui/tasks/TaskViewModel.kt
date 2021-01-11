package com.karlosprojects.todomvvmapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.karlosprojects.todomvvmapp.data.TaskDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@ExperimentalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(
    taskDao: TaskDao
) : ViewModel() {

    /**
     * This is like MutableLiveData, it can hold a single value, not like a normal flow, but we can use it
     * as a flow. We pass a empty string because we don't want to filter anything immediately
     */
    val searchQuery = MutableStateFlow("")

    /**
     * flatMapLatest is a flow operator, which means that whenever the value of flow 'it' changes,
     * execute the code block, and the parameter will be passed as the current value of searchQuery.
     * This will return a flow and then assign the result to the taskFlow val. Finally this val will
     * be translated to be observed as livedata
     */
    private val taskFlow = searchQuery.flatMapLatest {
        taskDao.getAllTasks(it)
    }

    val taskList = taskFlow.asLiveData()
}