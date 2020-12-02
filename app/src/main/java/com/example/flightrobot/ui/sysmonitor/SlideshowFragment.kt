package com.example.flightrobot.ui.sysmonitor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightrobot.R

class SysmonitorFragment : Fragment() {

    private lateinit var sysmonitorViewModel: SysmonitorViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        sysmonitorViewModel =
                ViewModelProviders.of(this).get(SysmonitorViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sysmonitor, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        sysmonitorViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        return root
    }
}