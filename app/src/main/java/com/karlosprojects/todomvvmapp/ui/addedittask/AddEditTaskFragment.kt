package com.karlosprojects.todomvvmapp.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.karlosprojects.todomvvmapp.R
import com.karlosprojects.todomvvmapp.databinding.FragmentAddEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel : AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            addTaskTxtTaskName.setText(viewModel.taskName)
            addTaskCbkImportant.isChecked = viewModel.taskImportance
            addTaskCbkImportant.jumpDrawablesToCurrentState()
            addTaskTxtDateCreated.isVisible = viewModel.task != null
            addTaskTxtDateCreated.text = "Created ${viewModel.task?.createdDateFormatted}"
        }
    }
}