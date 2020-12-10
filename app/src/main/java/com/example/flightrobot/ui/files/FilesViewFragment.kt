package com.example.flightrobot.ui.files

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightrobot.FileViewActivity
import com.example.flightrobot.LoginActivity
import com.example.flightrobot.R

class FilesFragment : Fragment() {

    private lateinit var filesViewModel: FilesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        filesViewModel =
                ViewModelProviders.of(this).get(FilesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_files, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        filesViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        val button: Button = root.findViewById(R.id.files_search)
        /*button.setOnClickListener {
            val it = Intent(root.context, FileViewActivity::class.java)
            //val it = Intent()
            //it.setAction("android.intent.action.FileView")
            var url: String = "http://www.baidu.com"
            it.putExtra("url", url)
            startActivity(it)
        }*/
        return root
    }
}