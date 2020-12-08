package com.example.flightrobot.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightrobot.FileViewActivity
import com.example.flightrobot.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
           //textView.text = it
        })
        val button: Button = root.findViewById(R.id.confirm_button)
        button.setOnClickListener {
            Toast.makeText(this.context, "请选择一个数据库 ", Toast.LENGTH_SHORT).show()
        }
        val imgButton: ImageView = root.findViewById(R.id.db737)
        imgButton.setOnClickListener {
            Toast.makeText(this.context, "确认选择", Toast.LENGTH_SHORT).show()
        }
        return root
    }
}