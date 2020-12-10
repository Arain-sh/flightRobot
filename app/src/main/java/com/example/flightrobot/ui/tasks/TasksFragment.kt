package com.example.flightrobot.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightrobot.R

class TasksFragment : Fragment() {

    private lateinit var tasksViewModel: TasksViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        tasksViewModel =
                ViewModelProviders.of(this).get(TasksViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tasks, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        tasksViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        return root
    }
}