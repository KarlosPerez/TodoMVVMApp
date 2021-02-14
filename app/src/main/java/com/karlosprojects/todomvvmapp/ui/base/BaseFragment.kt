package com.karlosprojects.todomvvmapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    private var _binding : VB? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel : VM
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    fun showMessage(view : View, message : String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB?

}