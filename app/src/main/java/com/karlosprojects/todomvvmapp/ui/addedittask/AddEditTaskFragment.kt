package com.karlosprojects.todomvvmapp.ui.addedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karlosprojects.todomvvmapp.databinding.FragmentAddEditTaskBinding
import com.karlosprojects.todomvvmapp.ui.base.BaseFragment
import com.karlosprojects.todomvvmapp.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditTaskFragment : BaseFragment<FragmentAddEditTaskBinding, AddEditTaskViewModel>() {

    override val viewModel : AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initEvents()
    }

    private fun initEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        showMessage(requireView(), event.message)
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.addTaskTxtTaskName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result )
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }

    private fun initViews() {
        binding.apply {
            addTaskTxtTaskName.setText(viewModel.taskName)
            addTaskCbkImportant.isChecked = viewModel.taskImportance
            addTaskCbkImportant.jumpDrawablesToCurrentState()
            addTaskTxtDateCreated.isVisible = viewModel.task != null
            addTaskTxtDateCreated.text = "Created ${viewModel.task?.createdDateFormatted}"

            addTaskTxtTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            addTaskCbkImportant.setOnCheckedChangeListener {  _ , isChecked ->
                viewModel.taskImportance = isChecked
            }

            addTaskBtnSaveTask.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddEditTaskBinding.inflate(inflater, container, false)
}