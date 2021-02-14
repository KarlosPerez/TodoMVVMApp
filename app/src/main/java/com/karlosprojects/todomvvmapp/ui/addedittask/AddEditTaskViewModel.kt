package com.karlosprojects.todomvvmapp.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karlosprojects.local.entity.Task
import com.karlosprojects.local.dao.TaskDao
import com.karlosprojects.todomvvmapp.ui.ADD_TASK_RESULT_OK
import com.karlosprojects.todomvvmapp.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao : TaskDao,
    @Assisted private val state : SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")
    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    private val addEditTaskChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        if(task != null) {
            val updatedTask = task.copy(name = taskName, important = taskImportance)
            updateTask(updatedTask)
        } else {
            val newTask = Task(name = taskName, important = taskImportance)
            createTask(newTask)
        }
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    private fun createTask(newTask: Task) = viewModelScope.launch {
        taskDao.insertTask(newTask)
        addEditTaskChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(updatedTask: Task) = viewModelScope.launch {
        taskDao.updateTask(updatedTask)
        addEditTaskChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val message : String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result : Int) : AddEditTaskEvent()
    }
}