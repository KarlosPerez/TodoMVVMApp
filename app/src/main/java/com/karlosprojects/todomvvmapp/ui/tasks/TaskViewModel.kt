package com.karlosprojects.todomvvmapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.karlosprojects.todomvvmapp.data.TaskDao

class TaskViewModel @ViewModelInject constructor(
    taskDao: TaskDao
) : ViewModel() {

    val taskList = taskDao.getAllTasks().asLiveData()
}