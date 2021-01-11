package com.karlosprojects.todomvvmapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karlosprojects.todomvvmapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}