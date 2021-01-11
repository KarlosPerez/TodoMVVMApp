package com.karlosprojects.todomvvmapp.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.karlosprojects.todomvvmapp.R
import com.karlosprojects.todomvvmapp.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Don't need to inflate because the layout is already inflated. Note: Inflate means that a xml layout file is turned into objects
        val binding = FragmentTasksBinding.bind(view)

        val taskAdapter = TaskAdapter()

        //apply means that you don't need to call binding all the time to setup the views
        binding.apply {
            tasksRecyclerView.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        //submitList is a method of ListAdapter, and after we sent a new list, DiffUtil do the calculations (events and animations)
        viewModel.taskList.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
    }
}