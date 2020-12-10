package com.example.flightrobot.ui.sysmonitor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val items = listOf(
            BasicGridItem(R.drawable.ic_menu_camera, "工作状态显示"),
            BasicGridItem(R.drawable.ic_menu_camera, "实时视频监控"),
            BasicGridItem(R.drawable.ic_menu_camera, "视频回放"),
        )
        var monitorButton: Button = root.findViewById(R.id.monitorButton)
        monitorButton.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                    cornerRadius(16f)
                    gridItems(items) { _, index, item ->
                        if (index == 0) {
                            val it = Intent(root.context, DeviceActivity::class.java)
                            startActivity(it)
                        } else if (index == 1) {
                            val it = Intent(root.context, CamActivity::class.java)
                            startActivity(it)
                        } else {
                            val it = Intent(root.context, VideoActivity::class.java)
                            startActivity(it)
                        }
                        Toast.makeText(this.context, "\"Selected item ${item.title} at index $index\"", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        return root
    }
}