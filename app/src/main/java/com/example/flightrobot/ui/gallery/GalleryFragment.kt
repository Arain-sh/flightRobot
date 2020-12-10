package com.example.flightrobot.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.example.flightrobot.*
import com.tapadoo.alerter.Alerter

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        //val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        })

        var desButton: Button = root.findViewById(R.id.des_task1)
        var desButton2: Button = root.findViewById(R.id.des_task2)
        var desButton3: Button = root.findViewById(R.id.des_task3)
        desButton.setOnClickListener {
            val it = Intent(root.context, TaskActivity::class.java)
            val taskid: String = "task1"
            it.putExtra("id", taskid)
            startActivity(it)
        }
        desButton2.setOnClickListener {
            val it = Intent(root.context, TaskActivity::class.java)
            val taskid: String = "task1"
            it.putExtra("id", taskid)
            startActivity(it)
        }
        desButton3.setOnClickListener {
            val it = Intent(root.context, TaskActivity::class.java)
            val taskid: String = "task1"
            it.putExtra("id", taskid)
            startActivity(it)
        }

        return root
    }
}