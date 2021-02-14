package com.karlosprojects.todomvvmapp.ui.deleteallcompleted

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.karlosprojects.di.ApplicationScope
import com.karlosprojects.local.dao.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        taskDao.deleteCompletedTasks()
    }
}